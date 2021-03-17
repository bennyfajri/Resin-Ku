package com.benny.resin_ku

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import com.benny.resin_ku.util.ResinNotificationUtil
import com.benny.resin_ku.util.UtilResin
import kotlinx.android.synthetic.main.fragment_resin.*
import java.util.*


class Fragment_Resin : Fragment() {

    companion object {
        fun newInstance(): Fragment{
            val fragment = Fragment_Resin()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

        fun setAlarm(context: Context?, nowSecond: Long, secondsRemaining: Long): Long{
            val wakeUpTime = (nowSecond + secondsRemaining) * 1000
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, ResinExpireReceiver::class.java )
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            UtilResin.setAlarmSetTime(nowSecond,context)
            return  wakeUpTime
        }
        fun removeAlarm(context: Context?){
            val intent = Intent(context, ResinExpireReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0,intent,0)
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            UtilResin.setAlarmSetTime(0, context)
        }
        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000

    }

    enum class ResinState{
        Stopped,Running
    }

    private lateinit var timer: CountDownTimer
    private var r_waktu_detik = 0L
    private var r_state =  ResinState.Stopped

    private var secondsRemaining = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rsn_btnSubmit.setOnClickListener { v ->
            startTimer()
            r_state = ResinState.Running
            updateButtons()
        }

        rsn_btnReset.setOnClickListener { v ->
            timer.cancel()
            onTimerFinished()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onResume() {
        super.onResume()

        initTimer()

        removeAlarm(context)
//        ResinNotificationUtil.hideTimerNotification(context)
    }

    override fun onPause() {
        super.onPause()

        if(r_state == ResinState.Running){
            timer.cancel()
            val wakeUpTime = setAlarm(context, nowSeconds, secondsRemaining)
//            ResinNotificationUtil.showResinRunning(context, wakeUpTime)
        }
        UtilResin.setPreviousTimerLengthSeconds(r_waktu_detik, context)
        UtilResin.setSecondsRemaining(secondsRemaining, context)
        UtilResin.setTimerState(r_state, context!!)
    }

    private fun initTimer() {
        r_state =  UtilResin.getTimerState(context)

        if(r_state == ResinState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if(r_state == ResinState.Running)
            UtilResin.getSecondsRemaining(context)
        else
            r_waktu_detik

        val alarmSetTime = UtilResin.getAlarmSetTime(context)
        if(alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if(secondsRemaining <= 0)
            onTimerFinished()
        else if (r_state == ResinState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        r_state = ResinState.Stopped

        setNewTimerLength()

        progress_resin.progress = 0

        UtilResin.setSecondsRemaining(r_waktu_detik, context)
        secondsRemaining = r_waktu_detik

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        r_state = ResinState.Running

        timer = object  : CountDownTimer(secondsRemaining * 1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }

            override fun onFinish() = onTimerFinished()
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = UtilResin.getTimerLengt(context)
        r_waktu_detik = (lengthInMinutes * 60L)
        progress_resin.max = r_waktu_detik.toInt()
    }

    private fun setPreviousTimerLength() {
        r_waktu_detik = UtilResin.getPreviousTimerLengthSeconds(context)
        progress_resin.max = r_waktu_detik.toInt()
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondStr = secondsInMinuteUntilFinished.toString()
        resinCountdown.text = "$minutesUntilFinished : ${
            if(secondStr.length == 2) secondStr
            else "0" + secondStr}"
        progress_resin.progress = (r_waktu_detik - secondsRemaining).toInt()
    }

    private fun updateButtons() {
        when(r_state){
            ResinState.Running -> {
                rsn_btnSubmit.isEnabled = false
                rsn_btnReset.isEnabled = true
            }
            ResinState.Stopped -> {
                rsn_btnSubmit.isEnabled = true
                rsn_btnReset.isEnabled = false
            }
        }
    }
}