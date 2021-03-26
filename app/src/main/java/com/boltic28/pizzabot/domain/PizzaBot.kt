package com.boltic28.pizzabot.domain

import com.boltic28.pizzabot.Constants.EMPTY_TEXT
import com.boltic28.pizzabot.Constants.NEIGHBORHOOD_COORDINATES_SEPARATOR
import com.boltic28.pizzabot.Constants.ONE_STEP_TIME
import com.boltic28.pizzabot.Constants.ORDER_COORDINATES_SEPARATOR
import com.boltic28.pizzabot.Constants.ORDER_PREFIX
import com.boltic28.pizzabot.Constants.ORDER_SUFFIX
import com.boltic28.pizzabot.Constants.READY_TO_DELIVERY
import com.boltic28.pizzabot.Constants.WORK_IS_DONE
import com.boltic28.pizzabot.Constants.WORK_IS_FAILED
import com.boltic28.pizzabot.data.dto.NeighborHood
import com.boltic28.pizzabot.data.dto.Order
import com.boltic28.pizzabot.data.dto.Position
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PizzaBot(private val data: String) {

    private lateinit var neighborHood: NeighborHood
    private lateinit var orders: List<Order>

    private var deliverManPosition: Position = Position()

    private val _logger = MutableStateFlow(EMPTY_TEXT)
    val logger: StateFlow<String> = _logger.asStateFlow()

    private val _init = MutableStateFlow(NeighborHood())
    val init: StateFlow<NeighborHood> = _init.asStateFlow()

    private val pathStorage = mutableListOf<Position>()
    private val _path = MutableStateFlow(listOf(Position()))
    val path: StateFlow<List<Position>> = _path.asStateFlow()

    private val finishedOrdersStorage = mutableListOf<Order>()
    private val _finishedOrders = MutableStateFlow(listOf<Order>())
    val finishedOrders: StateFlow<List<Order>> = _finishedOrders.asStateFlow()

    fun checkData(): String =
        if (initData()) {
            startDelivery()
            READY_TO_DELIVERY
        } else {
            WORK_IS_FAILED
        }

    private fun initData(): Boolean {
        try {
            val result = mutableListOf<Order>()
            data.split(ORDER_PREFIX).toMutableList().apply {
                neighborHood = provideNeighborHood(first()) ?: return false
                removeAt(0)
                pizzaNumber = 1
                forEach { orderData ->
                    val order = getOrder(orderData)
                    if (order != null) result.add(order) else return false
                }
            }
            logEvent("${result.size} pizza(s) in a bag")
            logEvent("neighborhood size is: ${neighborHood.width}:${neighborHood.height}")
            orders = result
            _init.value = neighborHood
            return true
        } catch (e: Exception) {
            logEvent("order list is not configured: $e")
            return false
        }
    }

    private fun provideNeighborHood(data: String): NeighborHood? {
        try {
            with(data.trim().split(NEIGHBORHOOD_COORDINATES_SEPARATOR)) {
                return NeighborHood(
                    first().trim().toInt(),
                    last().trim().toInt()
                )
            }
        } catch (e: NumberFormatException) {
            logEvent("bad neighborhood size: $e")
            return null
        }
    }

    private var pizzaNumber = 1
    private fun getOrder(orderData: String): Order? {
        try {
            with(
                orderData
                    .trim()
                    .removeSuffix(ORDER_SUFFIX)
                    .split(ORDER_COORDINATES_SEPARATOR)
                    .map { it.trim().toInt() }
            ) {
                if (this.size < 2) return null
                return Order(
                    Position(first(), last()),
                    pizzaNumber++
                )
            }
        } catch (e: NumberFormatException) {
            logEvent("bad order position: $e")
            return null
        }
    }

    private fun startDelivery() {
        CoroutineScope(Dispatchers.Default).launch {
            while (orders.any { !it.isFinished() }) {
                val nextOrder = findNearestOrder()
                while (nextOrder.position != deliverManPosition) {
                    makeStepTo(nextOrder)
                }
                dropPizza()
            }
            logEvent(WORK_IS_DONE)
        }
    }

    private fun findNearestOrder() = orders.filter { !it.isFinished() }.minByOrNull {
        getDistanceBetween(it.position, deliverManPosition)
    } ?: Order(Position())

    private fun getDistanceBetween(p1: Position, p2: Position): Float =
        kotlin.math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y).toFloat())

    private suspend fun makeStepTo(order: Order) {
        // method delay is calls in each if block because it's can calls two of them
        // and we have to make a pause after each step
        if (deliverManPosition.y < order.position.y) {
            moveNorth()
            delay(ONE_STEP_TIME)
        }
        if (deliverManPosition.y > order.position.y) {
            moveSouth()
            delay(ONE_STEP_TIME)
        }
        if (deliverManPosition.x < order.position.x) {
            moveEast()
            delay(ONE_STEP_TIME)
        }
        if (deliverManPosition.x > order.position.x) {
            moveWest()
            delay(ONE_STEP_TIME)
        }
    }

    private fun moveNorth() {
        val newY = deliverManPosition.y + 1
        if (newY <= neighborHood.height) {
            updateDeliverManPosition(null, newY)
            logEvent("move North: ${deliverManPosition.x}:${deliverManPosition.y}")
        } else {
            logEvent("try to go out of neighborhood")
        }
    }

    private fun moveSouth() {
        val newY = deliverManPosition.y - 1
        if (newY > 0) {
            updateDeliverManPosition(null, newY)
            logEvent("move South: ${deliverManPosition.x}:${deliverManPosition.y}")
        } else {
            logEvent("try to go out of neighborhood")
        }
    }

    private fun moveWest() {
        val newX = deliverManPosition.x - 1
        if (newX > 0) {
            updateDeliverManPosition(newX, null)
            logEvent("move West: ${deliverManPosition.x}:${deliverManPosition.y}")
        } else {
            logEvent("try to go out of neighborhood")
        }
    }

    private fun moveEast() {
        val newX = deliverManPosition.x + 1
        if (newX <= neighborHood.width) {
            updateDeliverManPosition(newX, null)
            logEvent("move East: ${deliverManPosition.x}:${deliverManPosition.y}")
        } else {
            logEvent("try to go out of neighborhood")
        }
    }

    private fun dropPizza() {
        try {
            orders.filter { order ->
                order.position == deliverManPosition && !order.isFinished()
            }.forEach {
                it.finish()
                logEvent("pizza ${orders.indexOf(it) + 1} dropped")
                finishedOrdersStorage.add(it)
                _finishedOrders.value = listOf(*finishedOrdersStorage.toTypedArray())
            }
        } catch (e: Exception) {
            logEvent("bad coordinates for deliver")
        }
    }

    private fun updateDeliverManPosition(x: Int?, y: Int?) {
        x?.let { deliverManPosition = deliverManPosition.copy(x = x) }
        y?.let { deliverManPosition = deliverManPosition.copy(y = y) }
        pathStorage.add(deliverManPosition)
        _path.value = listOf(*pathStorage.toTypedArray())
    }

    private fun logEvent(msg: String) {
        _logger.value = msg
    }
}