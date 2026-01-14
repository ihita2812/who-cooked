package com.ihita.wholetthemcook.ui.export

object QuantityFormatter {
    private const val EPSILON = 0.0001f

    fun format(qty: Float): String =
        if (kotlin.math.abs(qty - qty.toInt()) < EPSILON)
            qty.toInt().toString()
        else
            qty.toString().trimEnd('0').trimEnd('.')
}
