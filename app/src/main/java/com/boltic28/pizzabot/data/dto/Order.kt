package com.boltic28.pizzabot.data.dto

class Order(val position: Position, val number: Int = 0, private var isFinished: Boolean = false) {

    fun finish() { isFinished = true }

    fun isFinished() = isFinished
}