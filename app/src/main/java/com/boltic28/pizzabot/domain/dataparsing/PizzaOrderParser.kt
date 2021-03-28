package com.boltic28.pizzabot.domain.dataparsing

import com.boltic28.pizzabot.Constants
import com.boltic28.pizzabot.data.dto.NeighborHood
import com.boltic28.pizzabot.data.dto.Order
import com.boltic28.pizzabot.data.dto.Position

class PizzaOrderParser: OrderParser<Order, NeighborHood> {

    private val orders = mutableListOf<Order>()
    private var map = NeighborHood()

    override fun getOrders(): List<Order> = orders

    override fun getDistrictModel(): NeighborHood = map

    override fun isDataCorrect(data: String): Boolean {
        try {
            data.split(Constants.ORDER_PREFIX).toMutableList().apply {
                map = provideNeighborHood(first()) ?: return false
                removeAt(0)
                pizzaNumber = 1
                forEach { orderData ->
                    val order = getOrder(orderData)
                    if (order != null) orders.add(order) else return false
                }
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    private fun provideNeighborHood(data: String): NeighborHood? {
        try {
            with(data.trim().split(Constants.NEIGHBORHOOD_COORDINATES_SEPARATOR)) {
                return NeighborHood(
                    first().trim().toInt(),
                    last().trim().toInt()
                )
            }
        } catch (e: NumberFormatException) {
            return null
        }
    }

    private var pizzaNumber = 1
    private fun getOrder(orderData: String): Order? {
        try {
            with(
                orderData
                    .trim()
                    .removeSuffix(Constants.ORDER_SUFFIX)
                    .split(Constants.ORDER_COORDINATES_SEPARATOR)
                    .map { it.trim().toInt() }
            ) {
                if (this.size < 2) return null
                return Order(
                    Position(
                        first(),
                        last()
                    ),
                    pizzaNumber++
                )
            }
        } catch (e: NumberFormatException) {
            return null
        }
    }
}