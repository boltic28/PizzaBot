package com.boltic28.pizzabot.domain.delivery

import com.boltic28.pizzabot.data.dto.Order
import com.boltic28.pizzabot.data.dto.Position
import kotlinx.coroutines.flow.StateFlow

interface Delivery {
    suspend fun startDelivery()

    fun observePath(): StateFlow<List<Position>>
    fun observeOrders(): StateFlow<List<Order>>
    fun observeLogs(): StateFlow<List<String>>
}