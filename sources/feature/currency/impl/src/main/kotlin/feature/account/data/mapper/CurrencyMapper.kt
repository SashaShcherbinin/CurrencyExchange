package feature.account.data.mapper

import feature.account.domain.entity.ExchangeResult
import feature.account.data.dto.ExchangeDto
import java.math.BigDecimal
import java.math.RoundingMode

fun ExchangeDto.toDomain(
    fromCurrency: String,
    toCurrency: String,
    amount: BigDecimal
): ExchangeResult {
    val fromRate = rates[fromCurrency] ?: error("Rate for $fromCurrency not found")
    val toRate = rates[toCurrency] ?: error("Rate for $toCurrency not found")
    val toAmount = amount
        .multiply(toRate.toBigDecimal())
        .divide(fromRate.toBigDecimal(), RoundingMode.HALF_EVEN)

    val commissionRate = BigDecimal("0.007")
    val commission = amount.multiply(commissionRate)

    return ExchangeResult(
        fromCurrency = fromCurrency,
        toCurrency = toCurrency,
        fromAmount = amount,
        toAmount = toAmount,
        commission = commission
    )
}