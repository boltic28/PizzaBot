package com.boltic28.pizzabot.domain.routing

import com.boltic28.pizzabot.data.dto.Order
import com.boltic28.pizzabot.data.dto.Position
import com.boltic28.pizzabot.domain.ordering.OrderKeeper
import com.boltic28.pizzabot.domain.ordering.PizzaOrdersKeeper
import junit.framework.TestCase
import org.junit.Before

class PizzaBotRouterTest : TestCase() {

    companion object{
        val orders = listOf(
            Order(Position(1,1)),
            Order(Position(2,4)),
            Order(Position(4,5)),
            Order(Position(5,2)),
            Order(Position(4,4))
        )
    }

    private lateinit var router: Route<Order>
    private lateinit var orderKeeper: OrderKeeper<Order>

    @Before
    override fun setUp() {
        orderKeeper = PizzaOrdersKeeper(orders)
        router = PizzaBotRouter(orderKeeper)
    }

    fun testGetNearestTo() {
        assertEquals(Position(1,1).x, router.getNearestTo(Position()).position.x)
        assertEquals(Position(4,4).x, router.getNearestTo(Position(4,3)).position.x)
        assertEquals(Position(4,5).x, router.getNearestTo(Position(5,5)).position.x)
        assertEquals(Position(2,4).x, router.getNearestTo(Position(2,5)).position.x)
    }
}