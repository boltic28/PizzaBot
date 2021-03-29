package com.boltic28.pizzabot.domain.delivery

import com.boltic28.pizzabot.Constants
import com.boltic28.pizzabot.domain.logging.Logger
import com.boltic28.pizzabot.domain.movement.Movable
import com.boltic28.pizzabot.domain.ordering.Order
import com.boltic28.pizzabot.domain.ordering.OrdersKeeper
import com.boltic28.pizzabot.domain.routing.Route

class PizzaSliceBot(
    override var deliver: Movable,
    override var logger: Logger,
    override var router: Route<Order>,
    override var ordersKeeper: OrdersKeeper<Order>
) : DeliveryImpl() {

    override suspend fun startDelivery(){
        while (ordersKeeper.getOrders().any { !it.isFinished() }) {
            val nextOrder = getNextOrder()
            while (nextOrder.getPosition() != deliver.getPosition()) {
                makeStepTo(nextOrder)
            }
            finishOrdersOnPosition(deliver.getPosition())
        }
        log(Constants.WORK_IS_DONE)
    }
}