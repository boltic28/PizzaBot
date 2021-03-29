package com.boltic28.pizzabot.domain.movement

import com.boltic28.pizzabot.data.dto.Position

class DeliverByCar(position: Position) : BaseDeliver(position) {

    override suspend fun moveSW(): List<Position> {
        moveSouth()
        moveWest()
        return pathPoints
    }

    override suspend fun moveSE(): List<Position> {
        moveSouth()
        moveEast()
        return pathPoints
    }

    override suspend fun moveNW(): List<Position> {
        moveNorth()
        moveWest()
        return pathPoints
    }

    override suspend fun moveNE(): List<Position> {
        moveNorth()
        moveEast()
        return pathPoints
    }
}