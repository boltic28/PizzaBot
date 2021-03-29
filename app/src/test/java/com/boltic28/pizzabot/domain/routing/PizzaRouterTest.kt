package com.boltic28.pizzabot.domain.routing

import com.boltic28.pizzabot.data.dto.Position
import com.boltic28.pizzabot.domain.ordering.Order
import com.boltic28.pizzabot.domain.ordering.OrdersKeeper
import com.boltic28.pizzabot.domain.ordering.PizzaOrder
import com.boltic28.pizzabot.domain.ordering.PizzaOrdersKeeper
import junit.framework.TestCase
import org.junit.Before

class PizzaRouterTest : TestCase() {

    companion object{
        val orders = listOf(
            PizzaOrder(Position(1,1)),
            PizzaOrder(Position(2,4)),
            PizzaOrder(Position(4,5)),
            PizzaOrder(Position(5,2)),
            PizzaOrder(Position(4,4))
        )
    }

    private lateinit var router: Route<Order>
    private lateinit var ordersKeeper: OrdersKeeper<Order>

    @Before
    override fun setUp() {
        ordersKeeper = PizzaOrdersKeeper(orders)
        router = PizzaRouter(ordersKeeper)
    }

    fun testGetNearestTo() {
        assertEquals(Position(1,1).x, router.getNearestTo(Position()).getPosition().x)
        assertEquals(Position(4,4).x, router.getNearestTo(Position(4,3)).getPosition().x)
        assertEquals(Position(4,5).x, router.getNearestTo(Position(5,5)).getPosition().x)
        assertEquals(Position(2,4).x, router.getNearestTo(Position(2,5)).getPosition().x)
    }
}