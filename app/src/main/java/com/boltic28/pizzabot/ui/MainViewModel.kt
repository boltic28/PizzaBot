package com.boltic28.pizzabot.ui

import androidx.lifecycle.ViewModel
import com.boltic28.pizzabot.data.dto.NeighborHood
import com.boltic28.pizzabot.data.dto.Position
import com.boltic28.pizzabot.domain.dataparsing.OrderParser
import com.boltic28.pizzabot.domain.dataparsing.PizzaOrderParser
import com.boltic28.pizzabot.domain.delivery.Delivery
import com.boltic28.pizzabot.domain.delivery.PizzaSliceBot
import com.boltic28.pizzabot.domain.logging.PizzaLogger
import com.boltic28.pizzabot.domain.movement.DeliverByCar
import com.boltic28.pizzabot.domain.movement.DeliverByFeet
import com.boltic28.pizzabot.domain.ordering.Order
import com.boltic28.pizzabot.domain.ordering.PizzaOrdersKeeper
import com.boltic28.pizzabot.domain.routing.PizzaRouter

class MainViewModel : ViewModel() {

    lateinit var pizzaBot: Delivery
    lateinit var parser: OrderParser<Order, NeighborHood>
    var isCarDelivery = false
    var isBotStarted = false

    fun createBot(data: String): NeighborHood? {
        val parser = PizzaOrderParser()
        return if (parser.isDataCorrect(data)) {
            val orderKeeper = PizzaOrdersKeeper(parser.getOrders())
            pizzaBot = PizzaSliceBot(
                deliver = if (isCarDelivery) DeliverByCar(Position()) else DeliverByFeet(Position()),
                logger = PizzaLogger(),
                ordersKeeper = orderKeeper,
                router = PizzaRouter(orderKeeper)
            )
            isBotStarted = true
            parser.getDistrictModel()
        } else {
            null
        }
    }
}