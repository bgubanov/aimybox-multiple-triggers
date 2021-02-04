package com.justai.aimybox.assistant.skills
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.justai.aimybox.components.L


class AlarmReceiver: BroadcastReceiver() {
    @SuppressLint("ShowToast")
    override fun onReceive(context: Context?, intent: Intent?) {
        L.d("Alarm!!!!!!")
        Toast.makeText(context, "whats up bitch", Toast.LENGTH_LONG).show()
        val v = getSystemService(context!!, Vibrator::class.java)

        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v?.vibrate(VibrationEffect.createOneShot(1500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v?.vibrate(1500)
        }
    }

}