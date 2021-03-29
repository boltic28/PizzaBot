package com.boltic28.pizzabot.domain.logging

import kotlinx.coroutines.flow.StateFlow

interface Logger {
    fun observeLogs(): StateFlow<List<String>>
    fun getLogs(): List<String>
    fun log(msg: String)
    fun clear()
}