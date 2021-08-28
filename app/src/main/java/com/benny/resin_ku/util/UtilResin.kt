package com.benny.resin_ku.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.benny.resin_ku.Fragment_Resin

class UtilResin {
    companion object {

        private const val TIMER_LENGTH_ID = "com.benny.timer.timer_length"
        fun getTimerLengt(context: Context?): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(TIMER_LENGTH_ID, 10)
        }

        fun setTimerLength(seconds: Long, context: Context?){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(TIMER_LENGTH_ID, seconds)
            editor.apply()
        }
        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.benny.resin.previous_timer_length"

        fun getPreviousTimerLengthSeconds(context: Context?): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return  preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context?){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        private const val TIMER_STATE_ID = "com.benny.resin.timer_state"

        fun getTimerState(context: Context?): Fragment_Resin.ResinState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val  ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return  Fragment_Resin.ResinState.values()[ordinal]
        }

        fun setTimerState(state: Fragment_Resin.ResinState, context: Context?){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID,ordinal)
            editor.apply()
        }


        private const val SECONDS_REMAINING_ID = "com.benny.resin.seconds_remaining"

        fun getSecondsRemaining(context: Context?): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return  preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context?){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID = "com.benny.resin.backgrounded_time"

        fun getAlarmSetTime(context: Context?): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME_ID,0)
        }

        fun setAlarmSetTime(time: Long, context: Context?){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}