package feature.dashboard.domain.entity

import feature.account.domain.entity.Account

data class Dashboard(
    val accountList: List<Account>,
    val accountCurrencyList: List<String>,
    val currencyList: List<String>,
)