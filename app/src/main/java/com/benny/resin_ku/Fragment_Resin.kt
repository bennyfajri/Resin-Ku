package com.benny.resin_ku

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.benny.resin_ku.util.ResinNotificationUtil
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
            val wakeUpTime = (nowSecond + secondsRemaining) * 60000
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
            UtilResin.setAlarmSetTime( 0, context)
        }
        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 60000
    }

    enum class ResinState{
        Stopped,Running
    }

    private var jumlahResin = 0L
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

        rsn_btnSet.setOnClickListener { v ->
            val resin = etInputResin.text.toString()
            if(resin.isEmpty()){
                etInputResin.error = "Input first"
            }else{
                jumlahResin = etInputResin.text.toString().toLong()

                UtilResin.setTimerLength(jumlahResin,context)
                setNewTimerLength()

                UtilResin.setSecondsRemaining(r_waktu_detik, context)
                secondsRemaining = r_waktu_detik

                updateCountdownUI()
                updateButtons()
                tvResin.text = jumlahResin.toString()
                etInputResin.clearFocus()

            }
        }
        rsn_btnSubmit.setOnClickListener { v ->
            startTimer()
            r_state = ResinState.Running
            updateButtons()
        }

        rsn_btnReset.setOnClickListener { v ->
            timer.cancel()
            onTimerFinished()
            tvResin.text = "0"
        }
    }

    override fun onResume() {
        super.onResume()

        initTimer()

        removeAlarm(context)
        ResinNotificationUtil.hideTimerNotification(context)
    }

    override fun onPause() {
        super.onPause()

        if(r_state == ResinState.Running){
            timer.cancel()
            val wakeUpTime = setAlarm(context, nowSeconds, secondsRemaining)
            ResinNotificationUtil.showResinRunning(context, wakeUpTime)
        }
        UtilResin.setPreviousTimerLengthSeconds(r_waktu_detik, context)
        UtilResin.setSecondsRemaining(secondsRemaining+1, context)
        UtilResin.setTimerState(r_state, context)
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
        else if(r_state == ResinState.Running)
            startTimer()


        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        r_state = ResinState.Stopped

        progress_resin.progress = 0

        secondsRemaining = r_waktu_detik
        UtilResin.setSecondsRemaining(secondsRemaining, context)

        updateButtons()
        updateCountdownUI()
        tvResin.text = "160"
        resinCountdown.text = "0:00"
    }

    private fun startTimer() {
        r_state = ResinState.Running

        timer = object  : CountDownTimer(secondsRemaining * 60000, 60000){
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 60000
                updateCountdownUI()
            }
        }.start()

    }

    private fun setNewTimerLength() {
        val waktuResin = UtilResin.getTimerLengt(context) * 8
        val lengthInMinutes = (160 * 8) - waktuResin
        r_waktu_detik = lengthInMinutes
        progress_resin.max = r_waktu_detik.toInt()
    }

    private fun setPreviousTimerLength() {
        r_waktu_detik = UtilResin.getPreviousTimerLengthSeconds(context)
        progress_resin.max = r_waktu_detik.toInt()
    }

    @SuppressLint("SetTextI18n")
    private fun updateCountdownUI() {
        val totalResin = 159
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondStr = secondsInMinuteUntilFinished.toString()
        val resin = totalResin - secondsRemaining / 8
        tvResin.text = resin.toInt().toString()
        resinCountdown.text = "$minutesUntilFinished : ${
            if(secondStr.length == 2) secondStr
            else "0" + secondStr}"
        progress_resin.progress = (r_waktu_detik - secondsRemaining).toInt()
    }

    private fun updateButtons() {
        when(r_state){
            ResinState.Running -> {
                rsn_btnSet.isEnabled = false
                rsn_btnSubmit.isEnabled = false
                rsn_btnReset.isEnabled = true
            }
            ResinState.Stopped -> {
                rsn_btnSet.isEnabled = true
                rsn_btnSubmit.isEnabled = true
                rsn_btnReset.isEnabled = false
            }
        }
    }
}