package com.benny.resin_ku

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.benny.resin_ku.util.StaminaNotificationUtil
import com.benny.resin_ku.util.UtilStamina
import kotlinx.android.synthetic.main.fragment__stamina.*
import java.util.*

class Fragment_Stamina : Fragment() {

    companion object {
        fun newInstance(): Fragment{
            val fragment = Fragment_Stamina()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

        fun setAlarm(context: Context?, nowSecond: Long, secondsRemaining: Long): Long{
            val wakeUpTime = (nowSecond + secondsRemaining) * 60000
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, StaminaExpireReceiver::class.java )
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            UtilStamina.setAlarmSetTime(nowSecond,context)
            return  wakeUpTime
        }
        fun removeAlarm(context: Context?){
            val intent = Intent(context, StaminaExpireReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0,intent,0)
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            UtilStamina.setAlarmSetTime( 0, context)
        }
        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 60000
    }

    enum class StaminaState{
        Stopped,Running
    }

    private var jumlahStamina = 0L
    private lateinit var timer: CountDownTimer
    private var s_waktu_detik = 0L
    private var s_state =  StaminaState.Stopped

    private var secondsRemaining = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__stamina, container, false)

        activity?.setTitle("Stamina")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stm_btnSet.setOnClickListener { v ->
            val inputStamina1 = inputStamina.text.toString()
            if(inputStamina1.equals("") || TextUtils.isEmpty(inputStamina1) || inputStamina1.length == 0){
                Toast.makeText(context,"Fill last stamina  first", Toast.LENGTH_LONG).show()
            }else{
                jumlahStamina = inputStamina.text.toString().toLong()
                UtilStamina.setTimeLength(jumlahStamina,context)
                setNewTimerLength()

                UtilStamina.setSecondRemaining(s_waktu_detik, context)
                secondsRemaining = s_waktu_detik

                updateCountdownUI()
                updateButtons()
            }
        }
        stm_btnSubmit.setOnClickListener { v ->
                startTimer()
                s_state = Fragment_Stamina.StaminaState.Running
                updateButtons()
        }

        stm_btnReset.setOnClickListener { v ->
            timer.cancel()
            onTimerFinished()
        }
    }

    override fun onResume() {
        super.onResume()

        initTimer()

        Fragment_Stamina.removeAlarm(context)
        StaminaNotificationUtil.hideTimerNotification(context)
    }

    override fun onPause() {
        super.onPause()

        if(s_state == StaminaState.Running){
            timer.cancel()
            val wakeUpTime = Fragment_Stamina.setAlarm(context, nowSeconds, secondsRemaining)
            StaminaNotificationUtil.showStaminaRunning(context, wakeUpTime)
        }
        UtilStamina.setPreviousTimerLengthSeconds(s_waktu_detik, context)
        UtilStamina.setSecondRemaining(secondsRemaining+1, context)
        UtilStamina.setTimerState(s_state, context)
    }

    private fun initTimer() {
        s_state =  UtilStamina.getTimerState(context)

        if(s_state == StaminaState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if(s_state == StaminaState.Running)
            UtilStamina.getSecondRemaining(context)
        else
            s_waktu_detik

        val alarmSetTime = UtilStamina.getAlarmSetTime(context)
        if(alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if(secondsRemaining <= 0)
            onTimerFinished()
        else if (s_state == StaminaState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        s_state = StaminaState.Stopped

        setNewTimerLength()

        progress_stamina.progress = 0

       UtilStamina.setSecondRemaining(s_waktu_detik,context)
        secondsRemaining = s_waktu_detik

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        s_state = StaminaState.Running

        timer = object  : CountDownTimer(secondsRemaining * 60000, 60000){
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 60000
                updateCountdownUI()
            }
        }.start()

    }

    private fun setNewTimerLength() {
        val waktuStamina = UtilStamina.getTimeLength(context) * 8
        var lengthInMinutes = (160 * 8) - waktuStamina
        s_waktu_detik = lengthInMinutes
        progress_stamina.max = s_waktu_detik.toInt()
    }

    private fun setPreviousTimerLength() {
        s_waktu_detik = UtilStamina.getPreviousTimerLengthSeconds(context)
        progress_stamina.max = s_waktu_detik.toInt()
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondStr = secondsInMinuteUntilFinished.toString()
        staminaCountdown.text = "$minutesUntilFinished : ${
            if(secondStr.length == 2) secondStr
            else "0" + secondStr}"
        progress_stamina.progress = (s_waktu_detik - secondsRemaining).toInt()
    }

    private fun updateButtons() {
        when(s_state){
            StaminaState.Running -> {
                stm_btnSet.isEnabled = false
                stm_btnSubmit.isEnabled = false
                stm_btnReset.isEnabled = true
            }
            StaminaState.Stopped -> {
                stm_btnSet.isEnabled = true
                stm_btnSubmit.isEnabled = true
                stm_btnReset.isEnabled = false
            }
        }
    }


}