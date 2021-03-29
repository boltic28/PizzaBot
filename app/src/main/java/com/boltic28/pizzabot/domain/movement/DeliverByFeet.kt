package com.boltic28.pizzabot.domain.movement

import com.boltic28.pizzabot.data.dto.Position
import kotlinx.coroutines.delay

class DeliverByFeet(position: Position) : BaseDeliver(position) {

    override suspend fun moveSW(): List<Position> {
        pathPoints.add(startPosition.copy(y = startPosition.y - 1, x = startPosition.x - 1))
        delay(pauseBetweenSteps)
        emitPathData()
        return pathPoints.apply { startPosition = this.last() }
    }

    override suspend fun moveSE(): List<Position> {
        pathPoints.add(startPosition.copy(y = startPosition.y - 1, x = startPosition.x + 1))
        delay(pauseBetweenSteps)
        emitPathData()
        return pathPoints.apply { startPosition = this.last() }
    }

    override suspend fun moveNW(): List<Position> {
        pathPoints.add(startPosition.copy(y = startPosition.y - 1, x = startPosition.x - 1))
        delay(pauseBetweenSteps)
        emitPathData()
        return pathPoints.apply { startPosition = this.last() }
    }

    override suspend fun moveNE(): List<Position> {
        pathPoints.add(startPosition.copy(y = startPosition.y + 1, x = startPosition.x + 1))
        delay(pauseBetweenSteps)
        emitPathData()
        return pathPoints.apply { startPosition = this.last() }
    }
}