package com.boltic28.pizzabot.domain.movement

import com.boltic28.pizzabot.Constants
import com.boltic28.pizzabot.data.dto.Position
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseDeliver(var startPosition: Position): Movable {

    protected val pathPoints = mutableListOf(startPosition)
    protected val pauseBetweenSteps = Constants.ONE_STEP_TIME

    private  val _path = MutableStateFlow<List<Position>>(listOf())
    override fun observePath(): StateFlow<List<Position>> = _path.asStateFlow()

    override fun getPosition(): Position = startPosition

    override fun getPath(): List<Position> = pathPoints

    override fun clear() { pathPoints.clear() }

    override suspend fun cancelStep(): List<Position> {
        pathPoints.remove(pathPoints.last())
        delay(pauseBetweenSteps)
        emitPathData()
        return pathPoints.apply { startPosition = this.last() }
    }

    override suspend fun moveSouth(): List<Position> {
        pathPoints.add(startPosition.copy(y = startPosition.y - 1))
        delay(pauseBetweenSteps)
        emitPathData()
        return pathPoints.apply { startPosition = this.last() }
    }

    override suspend fun moveNorth(): List<Position> {
        pathPoints.add(startPosition.copy(y = startPosition.y + 1))
        delay(pauseBetweenSteps)
        emitPathData()
        return pathPoints.apply { startPosition = this.last() }
    }
    override suspend fun moveWest(): List<Position> {
        pathPoints.add(startPosition.copy(x = startPosition.x - 1))
        delay(pauseBetweenSteps)
        emitPathData()
        return pathPoints.apply { startPosition = this.last() }
    }
    override suspend fun moveEast(): List<Position> {
        pathPoints.add(startPosition.copy(x = startPosition.x + 1))
        delay(pauseBetweenSteps)
        emitPathData()
        return pathPoints.apply { startPosition = this.last() }
    }

    abstract override suspend fun moveSW(): List<Position>
    abstract override suspend fun moveSE(): List<Position>
    abstract override suspend fun moveNW(): List<Position>
    abstract override suspend fun moveNE(): List<Position>

    protected fun emitPathData(){
        _path.value = listOf(*pathPoints.toTypedArray())
    }
}