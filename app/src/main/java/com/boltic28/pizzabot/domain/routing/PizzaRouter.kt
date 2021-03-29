package com.boltic28.pizzabot.domain.routing

import com.boltic28.pizzabot.domain.ordering.PizzaOrder
import com.boltic28.pizzabot.data.dto.Position
import com.boltic28.pizzabot.domain.ordering.Order
import com.boltic28.pizzabot.domain.ordering.OrdersKeeper

class PizzaRouter(private var orders: OrdersKeeper<Order>): Route<Order> {

    override fun getNearestTo(position: Position): Order =
        orders.getNotFinished().minByOrNull {
            getDistanceBetween(it.getPosition(), position)
        } ?: PizzaOrder(Position())

    private fun getDistanceBetween(p1: Position, p2: Position): Float =
        kotlin.math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y).toFloat())
}