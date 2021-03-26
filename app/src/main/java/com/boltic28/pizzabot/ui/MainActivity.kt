package com.boltic28.pizzabot.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.boltic28.pizzabot.Constants.WORK_IS_FAILED
import com.boltic28.pizzabot.R
import com.boltic28.pizzabot.databinding.ActivityMainBinding
import com.boltic28.pizzabot.launchWhenStarted
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        with(binding){
            loggerView.movementMethod = ScrollingMovementMethod()
            startDelivery.setOnClickListener { startDelivery() }
        }

        if (viewModel.isBotStarted){ observeData() }
    }

    private fun startDelivery() {
        val data = binding.inputField.text.toString()

        if (isDataRight(data)) {
            deliverPizza()
            observeData()
        } else {
            writeLog(WORK_IS_FAILED)
            showAttention()
        }
    }

    private fun isDataRight(data: String) = viewModel.createBot(data)

    private fun deliverPizza() = viewModel.pizzaBot.startDelivery()

    private fun observeData(){
        val map = binding.deliveryView

        with(viewModel.pizzaBot) {
            init.onEach {
                map.initGrill(it) }.launchWhenStarted(lifecycleScope)
            finishedOrders.onEach {
                map.loadOrders(it) }.launchWhenStarted(lifecycleScope)
            path.onEach {
                map.loadPath(it) }.launchWhenStarted(lifecycleScope)
            logger.onEach {
                writeLog(it) }.launchWhenStarted(lifecycleScope)
        }
    }

    private fun writeLog(msg: String) {
        val log = "${binding.loggerView.text} \n $msg"
        binding.loggerView.text = log
    }

    private fun showAttention() {
        Toast.makeText(this, resources.getString(R.string.wrong_input), Toast.LENGTH_LONG).show()
    }
}