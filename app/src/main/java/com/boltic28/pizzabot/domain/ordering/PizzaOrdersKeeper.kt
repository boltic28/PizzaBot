package com.boltic28.pizzabot.domain.ordering

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PizzaOrdersKeeper(private val orders: List<Order>): OrdersKeeper<Order> {

    private val _finished = MutableStateFlow<List<Order>>(listOf())
    override fun observeFinishedOrders(): StateFlow<List<Order>> = _finished.asStateFlow()

    override fun getFinished(): List<Order> = orders.filter { it.isFinished() }

    override fun getOrders(): List<Order> = orders

    override fun closeOrder(order: Order): List<Order> {
        orders.filter { it.getPosition() == order.getPosition() }.forEach { it.finish() }
        emitFinishedOrders()
        return getFinished()
    }

    override fun closeAll(orders: List<Order>): List<Order> {
        orders.forEach { closeOrder(it) }
        emitFinishedOrders()
        return getFinished()
    }

    override fun getNotFinished(): List<Order> =
        orders.toMutableList().filter { !it.isFinished() }

    private fun emitFinishedOrders(){
        _finished.value = listOf(*getFinished().toTypedArray())
    }
}