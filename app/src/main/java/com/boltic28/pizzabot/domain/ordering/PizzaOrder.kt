package com.boltic28.pizzabot.domain.ordering

import com.boltic28.pizzabot.data.dto.Position

class PizzaOrder(
    private val position: Position,
    private val number: Int = 0,
    private var isFinished: Boolean = false
): Order {
    override fun finish() { isFinished = true }
    override fun isFinished() = isFinished
    override fun getPosition(): Position = position
    override fun getId(): Int = number
}