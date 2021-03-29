package com.boltic28.pizzabot.domain.ordering

import com.boltic28.pizzabot.data.dto.Position

interface Order {
    fun finish()
    fun isFinished(): Boolean
    fun getPosition(): Position
    fun getId(): Int
}