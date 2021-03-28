package com.boltic28.pizzabot.domain.movement

import com.boltic28.pizzabot.data.dto.Position

interface Movable {

    fun getPosition(): Position
    fun getPath(): List<Position>
    fun clear()

    suspend fun cancelStep(): List<Position>
    suspend fun moveSouth(): List<Position>
    suspend fun moveNorth(): List<Position>
    suspend fun moveWest(): List<Position>
    suspend fun moveEast(): List<Position>
    suspend fun moveSW(): List<Position>
    suspend fun moveSE(): List<Position>
    suspend fun moveNW(): List<Position>
    suspend fun moveNE(): List<Position>
}