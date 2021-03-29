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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        with(binding) {
            loggerView.movementMethod = ScrollingMovementMethod()
            byCar.setOnClickListener {
                deliverByCar(true)
                startDelivery()
            }
            byFeet.setOnClickListener {
                deliverByCar(false)
                startDelivery()
            }
        }

        if (viewModel.isBotStarted) {
            initMap()
            observeDeliveryProcess()
        }
    }

    private fun deliverByCar(isCar: Boolean) {
        viewModel.isCarDelivery = isCar
    }

    private fun startDelivery() {
        clearMap()
        val data = binding.inputField.text.toString()
        val neighborHood = parseNeighborhood(data)

        if (neighborHood != null) {
            binding.deliveryView.initGrill(neighborHood)
            deliverPizza()
            observeDeliveryProcess()
        } else {
            showLogs(listOf(WORK_IS_FAILED))
            showAttention()
        }
    }

    private fun clearMap(){
        binding.deliveryView.clearMap()
    }

    private fun initMap(){
        with(binding.deliveryView){
            initGrill(viewModel.parser.getDistrictModel())
            invalidate()
        }
    }

    private fun parseNeighborhood(data: String) = viewModel.createBot(data)

    private fun deliverPizza() =
        CoroutineScope(Dispatchers.Default).launch {
            viewModel.pizzaBot.startDelivery()
        }

    private fun observeDeliveryProcess() {
        val map = binding.deliveryView
        with(viewModel.pizzaBot) {
            observeOrders().onEach {
                map.loadOrders(it)
            }.launchWhenStarted(lifecycleScope)
            observePath().onEach {
                map.loadPath(it)
            }.launchWhenStarted(lifecycleScope)
            observeLogs().onEach {
                showLogs(it)
            }.launchWhenStarted(lifecycleScope)
        }
    }

    private fun showLogs(logs: List<String>) {
        val log = StringBuilder(resources.getString(R.string.logging))
        logs.forEach {
            log.append("\n $it")
        }
        binding.loggerView.text = log.toString()
    }

    private fun showAttention() {
        Toast.makeText(this, resources.getString(R.string.wrong_input), Toast.LENGTH_LONG).show()
    }
}