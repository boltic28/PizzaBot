package com.boltic28.pizzabot.domain.logging

interface Logger {

    fun log(msg: String): Boolean
    fun getLogs(): List<String>
    fun clear()
}