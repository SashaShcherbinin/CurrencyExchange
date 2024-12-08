package feature.dashboard.domain.usecase

import base.domain.extention.combineResult
import feature.account.domain.usecase.GetAccountListUseCase
import feature.account.domain.usecase.GetCurrencyListUseCase
import feature.dashboard.domain.entity.Dashboard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface GetDashboardDataUseCase : () -> Flow<Result<Dashboard>>

class GetDashboardDataUseCaseImpl(
    val getCurrencyListUseCase: GetCurrencyListUseCase,
    val getAccountListUseCase: GetAccountListUseCase,
) : GetDashboardDataUseCase {
    override fun invoke(): Flow<Result<Dashboard>> {
        return getCurrencyListUseCase()
            .combine(getAccountListUseCase()) { currencyListResult, accountListResult ->
                combineResult(accountListResult, currencyListResult) { accountList, currencyList ->
                    Dashboard(
                        accountList = accountList,
                        accountCurrencyList = accountList.map { it.currency },
                        currencyList = currencyList
                    )
                }
            }
    }
}
