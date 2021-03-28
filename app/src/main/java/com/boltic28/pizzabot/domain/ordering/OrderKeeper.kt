package com.boltic28.pizzabot.domain.ordering

interface OrderKeeper<T> {

    fun closeOrder(order: T): List<T>
    fun closeAll(orders: List<T>): List<T>
    fun getFinished(): List<T>
    fun getNotFinished(): List<T>
    fun getOrders(): List<T>
}