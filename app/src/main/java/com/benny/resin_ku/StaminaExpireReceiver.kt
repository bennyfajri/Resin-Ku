package com.benny.resin_ku

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.benny.resin_ku.util.ResinNotificationUtil
import com.benny.resin_ku.util.StaminaNotificationUtil
import com.benny.resin_ku.util.UtilResin
import com.benny.resin_ku.util.UtilStamina

class StaminaExpireReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        StaminaNotificationUtil.showStaminaExpired(context)

        UtilStamina.setTimerState(Fragment_Stamina.StaminaState.Stopped, context)
        UtilStamina.setAlarmSetTime(0,context)
    }
}