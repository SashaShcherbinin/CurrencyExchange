package feature.dashboard.presentation

import android.icu.text.NumberFormat
import java.math.BigDecimal
import java.util.Locale

object NumberFormatter {

    fun BigDecimal.formatCurrency(currency: String): String {
        val formatter = NumberFormat.getInstance(Locale.getDefault())
        val formattedNumber = formatter.format(this)
        return "$formattedNumber $currency"
    }

    fun BigDecimal.formatNumber(): String {
        val formatter = NumberFormat.getInstance(Locale.getDefault())
        val formattedNumber = formatter.format(this)
        return formattedNumber
    }
}