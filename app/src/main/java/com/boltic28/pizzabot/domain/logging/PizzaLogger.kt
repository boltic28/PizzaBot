package com.boltic28.pizzabot.domain.logging

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PizzaLogger: Logger {

    private val log = mutableListOf<String>()

    private val _logs = MutableStateFlow<List<String>>(listOf())
    override fun observeLogs(): StateFlow<List<String>> = _logs.asStateFlow()

    override fun getLogs(): List<String> = log

    override fun log(msg: String) {
        log.add(msg)
        emitLogs()
    }

    override fun clear() {
        log.clear()
        emitLogs()
    }

    private fun emitLogs(){
        _logs.value = listOf(*log.toTypedArray())
    }
}