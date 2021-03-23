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

        binding.startDelivery.setOnClickListener {
            startDelivery()
        }
    }

    private fun startDelivery() {
        val data = binding.inputField.text.toString()
        val map = binding.deliveryView

        viewModel.startDelivery(data, map).logger.onEach {
            if (it == WORK_IS_FAILED) showAttention()
            val log = "${binding.loggerView.text} \n $it"
            binding.loggerView.text = log
        }.launchWhenStarted(lifecycleScope)

        binding.loggerView.movementMethod = ScrollingMovementMethod()
    }

    private fun showAttention() {
        Toast.makeText(this, resources.getString(R.string.wrong_input), Toast.LENGTH_LONG).show()
    }

}