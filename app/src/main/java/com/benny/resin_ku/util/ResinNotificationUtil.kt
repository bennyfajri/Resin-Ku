package com.benny.resin_ku.util

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.benny.resin_ku.*
import java.text.SimpleDateFormat
import java.util.*

class ResinNotificationUtil {
    companion object {
        private const val CHANNEL_ID_TIMER = "menu_resin"
        private const val CHANNEL_NAME_TIMER = "Time Resin Timer"
        var rnds = (1..1000).random()
        private var RESIN_ID = rnds

        fun showResinExpired(context: Context?) {
            val startIntent = Intent(context, ResinNotificationActionReceiver::class.java)
            startIntent.action = ResinConstants.ACTION_START
            val startPendingIntent = PendingIntent.getBroadcast(context,
                    0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle("Ad Astra Abyssosque!")
                    .setContentText("Petualang, resin telah penuh.")
                    .addAction(R.drawable.resin, "Dismiss", startPendingIntent)
                    .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))

            val nManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)

            nManager.notify(RESIN_ID, nBuilder.build())
        }

        fun showResinRunning(context: Context?, wakeUpTime: Long) {
            val stopIntent = Intent(context, ResinNotificationActionReceiver::class.java)
            stopIntent.action = ResinConstants.ACTION_STOP
            val stopPendingIntent = PendingIntent.getBroadcast(context,
                    0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)

            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, false)
            nBuilder.setContentTitle("Ad Astra Abyssosque!")
                    .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
                    .addAction(R.drawable.resin, "Stop", stopPendingIntent)
                    .setContentText("Resin telah diset, penuh pada pukul: ${df.format(Date(wakeUpTime))}")

            val nManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, false)

            nManager.notify(RESIN_ID, nBuilder.build())
        }

        fun hideTimerNotification(context: Context?) {
            val nManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.cancel(RESIN_ID)
        }

        private fun getBasicNotificationBuilder(context: Context?, channelId: String, playSound: Boolean): NotificationCompat.Builder {
            val notificationSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val nBuilder = NotificationCompat.Builder(context!!, channelId)
                    .setSmallIcon(R.drawable.resin)
                    .setAutoCancel(true)
                    .setDefaults(0)
            if (playSound) nBuilder.setSound(notificationSound)
            return nBuilder
        }

        private fun <T> getPendingIntentWithStack(context: Context?, javaClass: Class<T>): PendingIntent {
            val resultIntent = Intent(context, javaClass)
            resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(javaClass)
            stackBuilder.addNextIntent(resultIntent)

            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        @TargetApi(26)
        private fun NotificationManager.createNotificationChannel(channelID: String, channelName: String, playSound: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelImportance = if (playSound) NotificationManager.IMPORTANCE_DEFAULT
                else NotificationManager.IMPORTANCE_LOW
                val nChannel = NotificationChannel(channelID, channelName, channelImportance)
                nChannel.enableLights(true)
                nChannel.lightColor = Color.BLUE
                this.createNotificationChannel(nChannel)
            }
        }
    }
}