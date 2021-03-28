package com.boltic28.pizzabot.domain.routing

import com.boltic28.pizzabot.data.dto.Order
import com.boltic28.pizzabot.data.dto.Position
import com.boltic28.pizzabot.domain.ordering.OrderKeeper

class PizzaBotRouter(private var orders: OrderKeeper<Order>): Route<Order> {

    override fun getNearestTo(position: Position): Order =
        orders.getNotFinished().minByOrNull {
            getDistanceBetween(it.position, position)
        } ?: Order(Position())

    private fun getDistanceBetween(p1: Position, p2: Position): Float =
        kotlin.math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y).toFloat())
}