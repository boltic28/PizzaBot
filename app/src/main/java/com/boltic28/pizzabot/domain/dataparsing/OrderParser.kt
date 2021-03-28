package com.boltic28.pizzabot.domain.dataparsing

interface OrderParser<T, R> {

    fun isDataCorrect(data: String): Boolean
    fun getOrders(): List<T>
    fun getDistrictModel(): R
}