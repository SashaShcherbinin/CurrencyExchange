package feature.account.domain.entity

import java.math.BigDecimal

data class Transfer(
    val fromCurrency: String,
    val fromAmount: BigDecimal,
    val toCurrency: String,
    val toAmount: BigDecimal,
    val commission: BigDecimal,
)
