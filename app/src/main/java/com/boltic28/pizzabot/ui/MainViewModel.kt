package com.boltic28.pizzabot.ui

import androidx.lifecycle.ViewModel
import com.boltic28.pizzabot.data.MapView
import com.boltic28.pizzabot.logic.PizzaBot

class MainViewModel: ViewModel() {

    fun startDelivery(data: String, map: MapView): PizzaBot {
        return PizzaBot(data, map).apply { init()}
    }
}