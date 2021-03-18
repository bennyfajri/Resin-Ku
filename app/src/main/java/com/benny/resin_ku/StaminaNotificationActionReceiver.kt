package com.benny.resin_ku

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.benny.resin_ku.util.ResinNotificationUtil
import com.benny.resin_ku.util.UtilResin

class StaminaNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            StaminaConstants.ACTION_STOP -> {

                UtilResin.setTimerState(Fragment_Resin.ResinState.Stopped, context)
                ResinNotificationUtil.hideTimerNotification(context)
            }
            ResinConstants.ACTION_START -> {
                val minuteRemaining = UtilResin.getTimerLengt(context)
                val secondsRemaining = minuteRemaining * 60L
                val wakeUpTime = Fragment_Resin.setAlarm(context, Fragment_Resin.nowSeconds, secondsRemaining)
                UtilResin.setTimerState(Fragment_Resin.ResinState.Running, context)
                UtilResin.setSecondsRemaining(secondsRemaining,context)
                ResinNotificationUtil.showResinRunning(context, wakeUpTime)
            }

        }
    }
}