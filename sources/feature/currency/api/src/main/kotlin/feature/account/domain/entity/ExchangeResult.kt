package feature.account.domain.entity

import java.math.BigDecimal

data class ExchangeResult(
    val fromCurrency: String,
    val toCurrency: String,
    val fromAmount: BigDecimal,
    val toAmount: BigDecimal,
    val commission: BigDecimal,
)
