package com.benny.resin_ku

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.benny.resin_ku.util.ResinNotificationUtil
//import com.benny.resin_ku.util.ResinNotificationUtil
import com.benny.resin_ku.util.UtilResin

class ResinExpireReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        ResinNotificationUtil.showResinExpired(context)

        UtilResin.setTimerState(Fragment_Resin.ResinState.Stopped, context)
        UtilResin.setAlarmSetTime(0,context)
    }
}