package com.boltic28.pizzabot.domain.delivery

import com.boltic28.pizzabot.Constants
import com.boltic28.pizzabot.data.dto.Order
import com.boltic28.pizzabot.data.dto.Position
import com.boltic28.pizzabot.domain.logging.Logger
import com.boltic28.pizzabot.domain.movement.Movable
import com.boltic28.pizzabot.domain.ordering.OrderKeeper
import com.boltic28.pizzabot.domain.routing.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class DeliveryImpl: Delivery {

    abstract var deliver: Movable
    abstract var logger: Logger
    abstract var router: Route<Order>
    abstract var orderKeeper: OrderKeeper<Order>

    private val _path = MutableStateFlow(listOf(Position()))
    override fun observePath(): StateFlow<List<Position>> = _path.asStateFlow()

    private val _logger = MutableStateFlow(listOf<String>())
    override fun observeLogs(): StateFlow<List<String>> = _logger.asStateFlow()

    private val _finishedOrders = MutableStateFlow(listOf<Order>())
    override fun observeOrders(): StateFlow<List<Order>> = _finishedOrders.asStateFlow()

    fun changeDeliveryTypeTo(deliver: Movable) {
        this.deliver = deliver
    }

    fun changeLoggerTo(logger: Logger) {
       this.logger = logger
    }

    fun changeRouter(router: Route<Order>){
        this.router = router
    }

    fun changeOrderKeeper(keeper: OrderKeeper<Order>){
        this.orderKeeper = keeper
    }

    protected fun getNextOrder(): Order = router.getNearestTo(deliver.getPosition())

    protected suspend fun makeStepTo(order: Order) {
        val ord = order.position
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
            else -> finishOrdersOnPosition(order.position)
        }
        _path.value = listOf(*deliver.getPath().toTypedArray())
    }

    protected fun finishOrdersOnPosition(position: Position){
        orderKeeper.getOrders()
            .filter { it.position == position }
            .forEach {
                log("pizza ${orderKeeper.getOrders().indexOf(it) + 1} dropped")
                orderKeeper.closeOrder(it)
            }
        _finishedOrders.value = listOf(*orderKeeper.getFinished().toTypedArray())
    }

    protected fun log(msg: String){
        logger.log(msg)
        _logger.value = listOf(*logger.getLogs().toTypedArray())
    }
}