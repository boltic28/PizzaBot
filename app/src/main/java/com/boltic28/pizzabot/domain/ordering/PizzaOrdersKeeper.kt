package com.boltic28.pizzabot.domain.ordering

import com.boltic28.pizzabot.data.dto.Order

class PizzaOrdersKeeper(private val orders: List<Order>): OrderKeeper<Order> {

    override fun getFinished(): List<Order> = orders.filter { it.isFinished() }

    override fun getOrders(): List<Order> = orders

    override fun closeOrder(order: Order): List<Order> {
        orders.filter { it.position == order.position }.forEach { it.finish() }
        return getFinished()
    }

    override fun closeAll(orders: List<Order>): List<Order> {
        orders.forEach { closeOrder(it) }
        return getFinished()
    }

    override fun getNotFinished(): List<Order> =
        orders.toMutableList().filter { !it.isFinished() }

}