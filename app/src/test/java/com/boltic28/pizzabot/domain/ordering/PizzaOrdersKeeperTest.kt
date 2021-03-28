package com.boltic28.pizzabot.domain.ordering

import com.boltic28.pizzabot.data.dto.Order
import com.boltic28.pizzabot.data.dto.Position
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class PizzaOrdersKeeperTest : TestCase() {

    companion object{
        val orders = listOf(
            Order(Position(1,1)),
            Order(Position(2,4)),
            Order(Position(4,5)),
            Order(Position(5,2)),
            Order(Position(4,4))
        )
    }

    private lateinit var ordersKeeper: OrderKeeper<Order>

    @Before
    public override fun setUp() {
        ordersKeeper = PizzaOrdersKeeper(orders)
    }

    @Test
    fun testGetFinished() {
        ordersKeeper.closeOrder(orders[2])
        assertEquals(orders[2], ordersKeeper.getFinished().first())
        ordersKeeper.closeAll(orders.filter { it.position.x == 4 })
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
        val ordersForClose = orders.filter { it.position.x == 4 }
        ordersKeeper.closeAll(ordersForClose)
        assertEquals(2, ordersKeeper.getFinished().size)
        assertEquals(3, ordersKeeper.getNotFinished().size)
    }

    @Test
    fun testGetNotFinished() {
        ordersKeeper.closeAll(orders.filter { it.position.x == 4 })
        assertEquals(2, ordersKeeper.getFinished().size)
        assertEquals(3, ordersKeeper.getNotFinished().size)
    }
}