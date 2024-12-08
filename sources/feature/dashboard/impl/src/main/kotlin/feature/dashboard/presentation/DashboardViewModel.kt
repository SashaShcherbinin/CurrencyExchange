package feature.dashboard.presentation

import base.compose.mvi.MviViewModel
import base.compose.mvi.Processor
import base.compose.mvi.Publisher
import base.compose.mvi.Reducer
import base.compose.mvi.Repeater
import base.logger.AppLog
import feature.account.domain.entity.ExchangeRequest
import feature.account.domain.entity.Transfer
import feature.account.domain.usecase.ExchangeCurrencyUseCase
import feature.account.domain.usecase.TransferMoneyUseCase
import feature.dashboard.R
import feature.dashboard.domain.usecase.GetDashboardDataUseCase
import feature.dashboard.presentation.DashboardEffect.DataLoaded
import feature.dashboard.presentation.DashboardEffect.UpdateState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import java.math.BigDecimal

class DashboardViewModel(
    processor: DashboardProcessor,
    reducer: DashboardReducer,
    publisher: DashboardPublisher,
    repeater: DashboardRepeater,
) : MviViewModel<DashboardIntent, DashboardEffect, DashboardEvent, DashboardState>(
    defaultState = DashboardState(),
    processor = processor,
    reducer = reducer,
    publisher = publisher,
    repeater = repeater,
) {
    init {
        process(DashboardIntent.Init)
    }
}

class DashboardProcessor(
    private val getDashboardUseCase: GetDashboardDataUseCase,
    private val exchangeCurrencyUseCase: ExchangeCurrencyUseCase,
    private val transferMoneyUseCase: TransferMoneyUseCase,
    private val appLog: AppLog,
) : Processor<DashboardIntent, DashboardEffect, DashboardState> {

    override fun process(intent: DashboardIntent, state: DashboardState): Flow<DashboardEffect> =
        when (intent) {
            is DashboardIntent.Init -> {
                observerData()
            }

            is DashboardIntent.ChangeFromCurrency -> {
                flowOf(DashboardEffect.UpdateFromCurrency(intent.currency))
            }

            is DashboardIntent.ChangeToCurrency -> {
                flowOf(DashboardEffect.UpdateToCurrency(intent.currency))
            }

            is DashboardIntent.ChangeAmount -> {
                val amount = intent.amount.formatedBigDecimal()
                val exchangeFlow: Flow<DashboardEffect> = if (amount == BigDecimal.ZERO) {
                    flowOf(DashboardEffect.UpdateToAmount(BigDecimal.ZERO))
                } else {
                    exchangeCurrencyUseCase(
                        ExchangeRequest(
                            fromCurrency = state.fromCurrency ?: "",
                            toCurrency = state.toCurrency ?: "",
                            amount = amount
                        )
                    ).transform { result ->
                        result.fold(
                            onSuccess = {
                                emit(DashboardEffect.UpdateToAmount(it.toAmount))
                                emit(DashboardEffect.UpdateCommission(it.commission))
                            },
                            onFailure = { appLog.e(it) }
                        )
                    }
                }

                merge(
                    flowOf(
                        DashboardEffect.UpdateAmount(
                            intent.amount.toBigDecimalOrNull() ?: BigDecimal.ZERO
                        )
                    ),
                    exchangeFlow,
                )
            }

            DashboardIntent.Exchange -> {
                flow {
                    val result = transferMoneyUseCase(
                        Transfer(
                            fromCurrency = state.fromCurrency!!,
                            toCurrency = state.toCurrency!!,
                            fromAmount = state.amount,
                            toAmount = state.toAmount,
                            commission = state.commission,
                        )
                    )
                    result.fold(
                        onSuccess = {
                            emit(UpdateState(ContentState.Content))
                            emit(
                                DashboardEffect.ExchangeSucceed(
                                    fromCurrency = state.fromCurrency,
                                    toCurrency = state.toCurrency,
                                    fromAmount = state.amount,
                                    toAmount = state.toAmount,
                                    commission = state.commission
                                )
                            )
                        },
                        onFailure = {
                            appLog.e(it)
                            emit(DashboardEffect.ShowError("Not enough money"))
                        }
                    )
                }
            }
        }

    private fun observerData() = getDashboardUseCase()
        .onStart { UpdateState(ContentState.Loading) }
        .transform { dashboardResult ->
            dashboardResult.fold(
                onSuccess = {
                    emit(DataLoaded(it))
                    emit(UpdateState(ContentState.Content))
                },
                onFailure = {
                    appLog.e(it)
                    emit(UpdateState(ContentState.Error(R.string.dashboard_network_error)))
                }
            )
        }
}

private fun String.formatedBigDecimal(): BigDecimal {
    return try {
        val cleanedString = this.replace(Regex("[^\\d.]"), "")
        BigDecimal(cleanedString)
    } catch (e: NumberFormatException) {
        BigDecimal.ZERO
    }
}

class DashboardReducer : Reducer<DashboardEffect, DashboardState> {
    override fun reduce(effect: DashboardEffect, state: DashboardState): DashboardState? {
        return when (effect) {
            is UpdateState -> {
                state.copy(contentState = effect.contentState)
            }

            is DataLoaded -> {
                mapToState(state, effect)
            }

            is DashboardEffect.UpdateAmount -> {
                state.copy(amount = effect.amount)
            }

            is DashboardEffect.UpdateToAmount -> {
                state.copy(toAmount = effect.amount)
            }

            is DashboardEffect.UpdateFromCurrency -> {
                state.copy(fromCurrency = effect.currency)
            }

            is DashboardEffect.UpdateToCurrency -> {
                state.copy(toCurrency = effect.currency)
            }

            is DashboardEffect.UpdateCommission -> {
                state.copy(commission = effect.commission)
            }

            is DashboardEffect.ShowError,
            is DashboardEffect.ExchangeSucceed -> null
        }
    }

    private fun mapToState(
        state: DashboardState,
        effect: DataLoaded
    ) = state.copy(
        accountList = effect.dashboard.accountList,
        fromCurrency = state.fromCurrency ?: effect.dashboard.accountCurrencyList.first(),
        toCurrency = state.toCurrency ?: effect.dashboard.currencyList.first(),
        fromCurrencyList = effect.dashboard.accountCurrencyList,
        toCurrencyList = effect.dashboard.currencyList,
    )
}

class DashboardPublisher : Publisher<DashboardEffect, DashboardEvent> {
    override fun publish(effect: DashboardEffect): DashboardEvent? {
        return when (effect) {
            is UpdateState,
            is DataLoaded,
            is DashboardEffect.UpdateAmount,
            is DashboardEffect.UpdateFromCurrency,
            is DashboardEffect.UpdateToCurrency,
            is DashboardEffect.UpdateCommission,
            is DashboardEffect.UpdateToAmount -> null

            is DashboardEffect.ExchangeSucceed -> {
                DashboardEvent.ShowMessage(
                    "You have converted ${effect.fromAmount} ${effect.fromCurrency} to " +
                            "${effect.toAmount} ${effect.toCurrency}." +
                            " Commission Fee - ${effect.commission} ${effect.fromCurrency}."
                )
            }

            is DashboardEffect.ShowError -> DashboardEvent.ShowMessage(effect.message)
        }
    }
}

class DashboardRepeater : Repeater<DashboardEffect, DashboardIntent, DashboardState> {
    override fun repeat(effect: DashboardEffect, state: DashboardState): DashboardIntent? {
        return when (effect) {
            is DashboardEffect.UpdateFromCurrency -> {
                DashboardIntent.ChangeAmount(state.amount.toString())
            }

            is DashboardEffect.UpdateToCurrency -> {
                DashboardIntent.ChangeAmount(state.amount.toString())
            }

            is DashboardEffect.ExchangeSucceed -> {
                DashboardIntent.ChangeAmount("0")
            }

            else -> null
        }
    }
}
