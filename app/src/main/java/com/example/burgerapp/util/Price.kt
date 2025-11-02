package com.example.burgerapp.util

object Price {
    fun format(amount: Double): String = "$" + String.format("%.2f", amount)
}