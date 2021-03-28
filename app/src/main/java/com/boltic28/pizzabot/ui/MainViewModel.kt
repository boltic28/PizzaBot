package com.boltic28.pizzabot.ui

import androidx.lifecycle.ViewModel
import com.boltic28.pizzabot.data.dto.NeighborHood
import com.boltic28.pizzabot.data.dto.Order
import com.boltic28.pizzabot.data.dto.Position
import com.boltic28.pizzabot.domain.dataparsing.OrderParser
import com.boltic28.pizzabot.domain.dataparsing.PizzaOrderParser
import com.boltic28.pizzabot.domain.delivery.BotSlice
import com.boltic28.pizzabot.domain.delivery.Delivery
import com.boltic28.pizzabot.domain.logging.DeliveryLogger
import com.boltic28.pizzabot.domain.movement.MoveByCar
import com.boltic28.pizzabot.domain.movement.MoveByFeet
import com.boltic28.pizzabot.domain.ordering.PizzaOrdersKeeper
import com.boltic28.pizzabot.domain.routing.PizzaBotRouter

class MainViewModel : ViewModel() {

    lateinit var pizzaBot: Delivery
    lateinit var parser: OrderParser<Order, NeighborHood>
    var isCarDelivery = false
    var isBotStarted = false

    fun createBot(data: String): NeighborHood? {
        parser = PizzaOrderParser()
        return if (parser.isDataCorrect(data)) {
            val orderKeeper = PizzaOrdersKeeper(parser.getOrders())
            pizzaBot = BotSlice(
                deliver = if (isCarDelivery) MoveByCar(Position()) else MoveByFeet(Position()),
                logger = DeliveryLogger(),
                orderKeeper = orderKeeper,
                router = PizzaBotRouter(orderKeeper)
            )
            isBotStarted = true
            parser.getDistrictModel()
        } else {
            null
        }
    }
}