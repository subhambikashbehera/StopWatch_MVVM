package com.onlineexam.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.onlineexam.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var brodCastReceiver: BroadcastReceiver
    private lateinit var serviceIntent: Intent
    private var time = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding.viewModel = viewModel
        brodCastReceiver = viewModel.startBrodCastReceiver()
        registerReceiver(brodCastReceiver, IntentFilter(StopWatchService.UPDATED_TIME))
        serviceIntent = Intent(this, StopWatchService::class.java)

        viewModel.isBoolean.observe(this) {
            if (it) {
                binding.btnStart.backgroundTintList =
                    AppCompatResources.getColorStateList(this, R.color.on)
            } else {
                binding.btnStart.backgroundTintList =
                    AppCompatResources.getColorStateList(this, R.color.off)
            }
        }

        viewModel.isButtonClickedBoolean.observe(this) {
            if (it == "start") startStopWatch() else if (it == "stop") closeStopWatch() else if (it == "pause") resumeStopWatch()
        }

        viewModel.currentTime.observe(this) {
            time = it
        }


        binding.lifecycleOwner = this
    }


    private fun startStopWatch() {
        serviceIntent.putExtra(StopWatchService.CURRENT_TIME, time)
        startService(serviceIntent)
    }

    private fun closeStopWatch() {
        stopService(serviceIntent)
    }


    private fun resumeStopWatch() {
        stopService(serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(brodCastReceiver)
    }

}