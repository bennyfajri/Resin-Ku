package com.benny.resin_ku.util

import android.content.Context
import android.preference.PreferenceManager
import com.benny.resin_ku.Fragment_Stamina

class UtilStamina {
    companion object {

        private const val TIME_LENGTH_ID = "com.benny.time.timer_length"
        fun getTimeLength(context: Context?): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(TIME_LENGTH_ID, 10)
        }

        fun setTimeLength(seconds: Long, context: Context?){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(TIME_LENGTH_ID, seconds)
            editor.apply()
        }
        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.benny.stamina.previous_stamina_length"

        fun getPreviousTimerLengthSeconds(context: Context?): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return  preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context?){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

//        private const val PREVIOUS_RESIN_REMAINING = "com.benny.stamina.stamina_remaining"
//
//        fun getPreviousResinRemaining(context: Context?): Long{
//            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
//            return  preferences.getLong(PREVIOUS_RESIN_REMAINING, 0)
//        }
//
//        fun setPreviousResinRemaining(resin: Long, context: Context?){
//            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
//            editor.putLong(PREVIOUS_RESIN_REMAINING, resin)
//            editor.apply()
//        }

        private const val TIME_STATE_ID = "com.benny.stamina.timer_state"

        fun getTimerState(context: Context?): Fragment_Stamina.StaminaState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val  ordinal = preferences.getInt(TIME_STATE_ID, 0)
            return  Fragment_Stamina.StaminaState.values()[ordinal]
        }

        fun setTimerState(state: Fragment_Stamina.StaminaState, context: Context?){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIME_STATE_ID,ordinal)
            editor.apply()
        }


        private const val SECOND_REMAINING_ID = "com.benny.stamina.seconds_remaining"

        fun getSecondRemaining(context: Context?): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return  preferences.getLong(SECOND_REMAINING_ID, 0)
        }

        fun setSecondRemaining(seconds: Long, context: Context?){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECOND_REMAINING_ID, seconds)
            editor.apply()
        }

        private const val RESIN_REMAINING_ID = "com.benny.stamina.stamina_remaining"

        fun getResinRemaining(context: Context?): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return  preferences.getLong(RESIN_REMAINING_ID, 0)
        }

        fun setResinRemaining(seconds: Long, context: Context?){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(RESIN_REMAINING_ID, seconds)
            editor.apply()
        }


        private const val ALARM_SET_TIME_ID = "com.benny.stamina.backgrounded_time"

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