package com.example.myburgerapp.util

object Price {
    fun format(amount: Double): String = "$" + String.format("%.2f", amount)
}