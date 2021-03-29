package com.boltic28.pizzabot.domain.delivery

import com.boltic28.pizzabot.data.dto.Position
import com.boltic28.pizzabot.domain.logging.Logger
import com.boltic28.pizzabot.domain.movement.Movable
import com.boltic28.pizzabot.domain.ordering.Order
import com.boltic28.pizzabot.domain.ordering.OrdersKeeper
import com.boltic28.pizzabot.domain.routing.Route
import kotlinx.coroutines.flow.StateFlow

abstract class DeliveryImpl: Delivery {

    abstract var deliver: Movable
    abstract var logger: Logger
    abstract var router: Route<Order>
    abstract var ordersKeeper: OrdersKeeper<Order>

    override fun observePath(): StateFlow<List<Position>> = deliver.observePath()
    override fun observeLogs(): StateFlow<List<String>> = logger.observeLogs()
    override fun observeOrders(): StateFlow<List<Order>> = ordersKeeper.observeFinishedOrders()

    fun changeDeliveryTypeTo(deliver: Movable) {
        this.deliver = deliver
    }

    fun changeLoggerTo(logger: Logger) {
       this.logger = logger
    }

    fun changeRouter(router: Route<Order>){
        this.router = router
    }

    fun changeOrderKeeper(keeper: OrdersKeeper<Order>){
        this.ordersKeeper = keeper
    }

    protected fun getNextOrder(): Order = router.getNearestTo(deliver.getPosition())

    protected suspend fun makeStepTo(order: Order) {
        val ord = order.getPosition()
        val pos = deliver.getPosition()
        when(true){
            pos.x == ord.x && pos.y < ord.y -> {
                deliver.moveNorth()
                log("moveNorth -> ${deliver.getPosition().x}:${deliver.getPosition().y}")
            }
            pos.x == ord.x && pos.y > ord.y -> {
                deliver.moveSouth()
                log("moveSouth -> ${deliver.getPosition().x}:${deliver.getPosition().y}")
            }
            pos.x < ord.x && pos.y == ord.y -> {
                deliver.moveEast()
                log("moveEast -> ${deliver.getPosition().x}:${deliver.getPosition().y}")
            }
            pos.x < ord.x && pos.y < ord.y -> {
                deliver.moveNE()
                log("moveNorthEast -> ${deliver.getPosition().x}:${deliver.getPosition().y}")
            }
            pos.x < ord.x && pos.y > ord.y -> {
                deliver.moveSE()
                log("moveSouthEast -> ${deliver.getPosition().x}:${deliver.getPosition().y}")
            }
            pos.x > ord.x && pos.y == ord.y -> {
                deliver.moveWest()
                log("moveWest -> ${deliver.getPosition().x}:${deliver.getPosition().y}")
            }
            pos.x > ord.x && pos.y < ord.y -> {
                deliver.moveNW()
                log("moveNorthWest -> ${deliver.getPosition().x}:${deliver.getPosition().y}")
            }
            pos.x > ord.x && pos.y > ord.y -> {
                deliver.moveSW()
                log("moveSouthWest -> ${deliver.getPosition().x}:${deliver.getPosition().y}")
            }
            else -> finishOrdersOnPosition(order.getPosition())
        }
    }

    protected fun finishOrdersOnPosition(position: Position){
        ordersKeeper.getOrders()
            .filter { it.getPosition() == position }
            .forEach {
                log("pizza ${ordersKeeper.getOrders().indexOf(it) + 1} dropped")
                ordersKeeper.closeOrder(it)
            }
    }

    protected fun log(msg: String){
        logger.log(msg)
    }
}