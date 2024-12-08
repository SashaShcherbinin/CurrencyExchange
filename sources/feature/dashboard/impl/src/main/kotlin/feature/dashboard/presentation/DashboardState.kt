package feature.dashboard.presentation

import base.compose.mvi.Effect
import base.compose.mvi.Event
import base.compose.mvi.Intent
import base.compose.mvi.State
import feature.account.domain.entity.Account
import feature.dashboard.domain.entity.Dashboard
import java.math.BigDecimal

data class DashboardState(
    val contentState: ContentState = ContentState.Loading,
    val accountList: List<Account> = emptyList(),
    val toCurrencyList: List<String> = emptyList(),
    val fromCurrencyList: List<String> = emptyList(),
    val fromCurrency: String? = null,
    val toCurrency: String? = null,
    val amount: BigDecimal = 0.toBigDecimal(),
    val toAmount: BigDecimal = 0.toBigDecimal(),
    val commission: BigDecimal = 0.toBigDecimal(),
) : State

sealed interface DashboardIntent : Intent {
    data object Init : DashboardIntent
    data object Exchange : DashboardIntent
    data class ChangeToCurrency(val currency: String) : DashboardIntent
    data class ChangeFromCurrency(val currency: String) : DashboardIntent
    data class ChangeAmount(val amount: String) : DashboardIntent
}

sealed class DashboardEffect : Effect {
    data class UpdateState(val contentState: ContentState) : DashboardEffect()
    data class DataLoaded(val dashboard: Dashboard) : DashboardEffect()
    data class UpdateAmount(val amount: BigDecimal) : DashboardEffect()
    data class UpdateToAmount(val amount: BigDecimal) : DashboardEffect()
    data class UpdateCommission(val commission: BigDecimal) : DashboardEffect()
    data class UpdateToCurrency(val currency: String) : DashboardEffect()
    data class UpdateFromCurrency(val currency: String) : DashboardEffect()
    data class ExchangeSucceed(
        val fromCurrency: String,
        val toCurrency: String,
        val fromAmount: BigDecimal,
        val toAmount: BigDecimal,
        val commission: BigDecimal
    ) : DashboardEffect()

    data class ShowError(val message: String) : DashboardEffect()
}

sealed class DashboardEvent : Event {
    data class ShowMessage(val message: String) : DashboardEvent()
}

sealed class ContentState {
    data object Loading : ContentState()
    data object Content : ContentState()
    data class Error(val messageResId: Int) : ContentState()
}
