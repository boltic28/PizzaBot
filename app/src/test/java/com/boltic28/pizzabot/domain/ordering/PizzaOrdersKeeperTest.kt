package com.boltic28.pizzabot.domain.ordering

import com.boltic28.pizzabot.data.dto.Position
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class PizzaOrdersKeeperTest : TestCase() {

    companion object{
        val orders = listOf(
            PizzaOrder(Position(1,1)),
            PizzaOrder(Position(2,4)),
            PizzaOrder(Position(4,5)),
            PizzaOrder(Position(5,2)),
            PizzaOrder(Position(4,4))
        )
    }

    private lateinit var ordersKeeper: OrdersKeeper<Order>

    @Before
    public override fun setUp() {
        ordersKeeper = PizzaOrdersKeeper(orders)
    }

    @Test
    fun testGetFinished() {
        ordersKeeper.closeOrder(orders[2])
        assertEquals(orders[2], ordersKeeper.getFinished().first())
        ordersKeeper.closeAll(orders.filter { it.getPosition().x == 4 })
        assertEquals(2, ordersKeeper.getFinished().size)
    }

    @Test
    fun testGetOrders() {
        assertEquals(orders, ordersKeeper.getOrders())
    }

    @Test
    fun testCloseOrder() {
        val order = orders[3]
        ordersKeeper.closeOrder(order)
        assertEquals(order, ordersKeeper.getFinished().first())
    }

    @Test
    fun testCloseAll() {
        val ordersForClose = orders.filter { it.getPosition().x == 4 }
        ordersKeeper.closeAll(ordersForClose)
        assertEquals(2, ordersKeeper.getFinished().size)
        assertEquals(3, ordersKeeper.getNotFinished().size)
    }

    @Test
    fun testGetNotFinished() {
        ordersKeeper.closeAll(orders.filter { it.getPosition().x == 4 })
        assertEquals(2, ordersKeeper.getFinished().size)
        assertEquals(3, ordersKeeper.getNotFinished().size)
    }
}