package feature.account.domain.entity

import java.math.BigDecimal

data class Account(
    val currency: String,
    val amount: BigDecimal,
)
