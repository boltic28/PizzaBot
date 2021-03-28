package com.boltic28.pizzabot.domain.logging

class DeliveryLogger: Logger {

    private val log = mutableListOf<String>()

    override fun log(msg: String): Boolean = log.add(msg)

    override fun getLogs(): List<String> = log

    override fun clear() = log.clear()
}