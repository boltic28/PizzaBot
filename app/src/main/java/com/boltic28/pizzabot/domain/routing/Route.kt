package com.boltic28.pizzabot.domain.routing

import com.boltic28.pizzabot.data.dto.Position

interface Route<T> {

    fun getNearestTo(position: Position): T
}