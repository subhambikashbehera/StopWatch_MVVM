package com.onlineexam.stopwatch

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class StopWatchService : Service() {

    private val timer = Timer()
    override fun onBind(p0: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val timerAt=intent.getDoubleExtra(CURRENT_TIME,0.0)
        timer.scheduleAtFixedRate(SetUpdatedTime(timerAt),0,100)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    private inner class SetUpdatedTime(private var time: Double):TimerTask() {

        override fun run() {
            val intent=Intent(UPDATED_TIME)
            time++
            intent.putExtra(UPDATED_TIME,time)
            sendBroadcast(intent)
        }
    }

    companion object{
        const val CURRENT_TIME="current time"
        const val UPDATED_TIME="updated time"
    }

}