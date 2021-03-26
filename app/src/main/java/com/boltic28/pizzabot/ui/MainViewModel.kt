package com.boltic28.pizzabot.ui

import androidx.lifecycle.ViewModel
import com.boltic28.pizzabot.domain.PizzaBot

class MainViewModel: ViewModel() {

    lateinit var pizzaBot: PizzaBot
    var isBotStarted = false


    fun createDeliveryBot(data: String): String {
        isBotStarted = true
        pizzaBot = PizzaBot(data)
        return pizzaBot.checkData()
    }
}