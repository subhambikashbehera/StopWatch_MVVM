package com.onlineexam.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt


class MainActivityViewModel :ViewModel(){
    var buttonText=MutableLiveData<String>()
    var isBoolean=MutableLiveData<Boolean>()
    var isButtonClickedBoolean=MutableLiveData<String>()
    var currentTime=MutableLiveData<Double>()


    private lateinit var brodCastReceiver: BroadcastReceiver
    private val time=0.0
    private val stopWatchTextEdit=MutableLiveData<String>()
    init {
        buttonText.value="start"
        stopWatchTextEdit.value= getFormattedTime(time)
        isBoolean.value=true
        isButtonClickedBoolean.value="running"
        currentTime.value=time
    }


    val stopWatchText:LiveData<String>
    get()=stopWatchTextEdit

    fun startOrStop(){
        if (isBoolean.value!!){
            startStopWatch()
        }else{
            pauseStopWatch()
        }
    }

    private fun startStopWatch(){
        isBoolean.value=false
        buttonText.value="pause"
        isButtonClickedBoolean.value="start"
        isButtonClickedBoolean.value="running"
    }

    private fun pauseStopWatch(){
        isBoolean.value=true
        buttonText.value="start"
        isButtonClickedBoolean.value="pause"
        isButtonClickedBoolean.value="running"
    }






    fun resetStopWatch(){
        if (!stopWatchTextEdit.value.isNullOrEmpty()){
            isBoolean.value=true
            buttonText.value="start"
            isButtonClickedBoolean.value="stop"
            isButtonClickedBoolean.value="running"
            stopWatchTextEdit.value=getFormattedTime(time)
            currentTime.value=time
        }
    }


   fun startBrodCastReceiver():BroadcastReceiver{
        brodCastReceiver=object : BroadcastReceiver(){
            override fun onReceive(context: Context, intent: Intent) {
                currentTime.value = intent.getDoubleExtra(StopWatchService.UPDATED_TIME,0.0)
                stopWatchTextEdit.value=getFormattedTime(currentTime.value!!)
            }
        }
     return brodCastReceiver
    }

   private fun getFormattedTime(time:Double):String
    {
        val timeInt=time.roundToInt()
        val hours=timeInt % 86400 / 3600
        val min=timeInt % 86400 % 3600 /60
        val sec=timeInt % 86400 % 3600 % 60 % 60
        return String.format("%02d:%02d:%02d",hours,min,sec)
    }
}