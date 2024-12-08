package feature.account.domain.entity

import java.math.BigDecimal

data class ExchangeRequest(
    val fromCurrency: String,
    val toCurrency: String,
    val amount: BigDecimal,
)
