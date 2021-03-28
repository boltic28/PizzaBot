package com.boltic28.pizzabot.domain.delivery

import com.boltic28.pizzabot.Constants.WORK_IS_DONE
import com.boltic28.pizzabot.data.dto.NeighborHood
import com.boltic28.pizzabot.data.dto.Order
import com.boltic28.pizzabot.data.dto.Position
import com.boltic28.pizzabot.domain.dataparsing.OrderParser
import com.boltic28.pizzabot.domain.dataparsing.PizzaOrderParser
import com.boltic28.pizzabot.domain.logging.DeliveryLogger
import com.boltic28.pizzabot.domain.logging.Logger
import com.boltic28.pizzabot.domain.movement.MoveByCar
import com.boltic28.pizzabot.domain.ordering.OrderKeeper
import com.boltic28.pizzabot.domain.ordering.PizzaOrdersKeeper
import com.boltic28.pizzabot.domain.routing.PizzaBotRouter
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test

class DeliveryImplTest : TestCase() {

    companion object{
        const val startData = "15x15 (4,5)(6,8)(10,4)(4,13)(14,12)"
    }

    lateinit var parser: OrderParser<Order, NeighborHood>
    lateinit var delivery: Delivery
    lateinit var logger: Logger
    lateinit var orderKeeper: OrderKeeper<Order>
    lateinit var dispatcher: CoroutineDispatcher

    @Before
    public override fun setUp() {
        dispatcher = Dispatchers.Unconfined

        parser = PizzaOrderParser().apply { isDataCorrect(startData) }
        orderKeeper = PizzaOrdersKeeper(parser.getOrders())
        logger = DeliveryLogger()
        delivery = BotSlice(
            MoveByCar(Position()),
            logger,
            PizzaBotRouter(orderKeeper),
            orderKeeper)
    }

    @Test
    fun testStartDelivery() {
        CoroutineScope(dispatcher).launch {
            delivery.startDelivery()
            assertEquals(WORK_IS_DONE, logger.getLogs().last())
        }
    }
}