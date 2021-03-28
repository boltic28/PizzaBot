package com.boltic28.pizzabot.domain.delivery

import com.boltic28.pizzabot.Constants
import com.boltic28.pizzabot.data.dto.Order
import com.boltic28.pizzabot.domain.logging.Logger
import com.boltic28.pizzabot.domain.movement.Movable
import com.boltic28.pizzabot.domain.ordering.OrderKeeper
import com.boltic28.pizzabot.domain.routing.Route

class BotSlice(
    override var deliver: Movable,
    override var logger: Logger,
    override var router: Route<Order>,
    override var orderKeeper: OrderKeeper<Order>
) : DeliveryImpl() {

    override suspend fun startDelivery(){
        while (orderKeeper.getOrders().any { !it.isFinished() }) {
            val nextOrder = getNextOrder()
            while (nextOrder.position != deliver.getPosition()) {
                makeStepTo(nextOrder)
            }
            finishOrdersOnPosition(deliver.getPosition())
        }
        log(Constants.WORK_IS_DONE)
    }
}