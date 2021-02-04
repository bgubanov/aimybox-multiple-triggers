package com.justai.aimybox.assistant.skills

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.AlarmClock
import androidx.annotation.RequiresApi
import com.justai.aimybox.Aimybox
import com.justai.aimybox.api.aimybox.AimyboxRequest
import com.justai.aimybox.api.aimybox.AimyboxResponse
import com.justai.aimybox.core.CustomSkill
import com.justai.aimybox.model.Response
import java.time.LocalDateTime
import java.time.ZoneOffset


class AlarmSkill(context_: Context) : CustomSkill<AimyboxRequest, AimyboxResponse> {

    override fun canHandle(response: AimyboxResponse) = response.action == "setAlarm"
    private var context: Context = context_

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun onResponse(
        response: AimyboxResponse,
        aimybox: Aimybox,
        defaultHandler: suspend (Response) -> Unit
    ) {
        val message = response.data?.asJsonObject?.get("text")?.asString ?: "Wake up, buddy!"
        val timestamp = response.data?.asJsonObject?.get("timestamp")?.asLong ?: 0

        this.createAlarm(message, timestamp)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAlarm(message: String, timestamp: Long) {
        val t: ZoneOffset = ZoneOffset.of("+03:00")

        val time = LocalDateTime.ofEpochSecond(timestamp/1000, 0, t)

        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_HOUR, time.hour)
            putExtra(AlarmClock.EXTRA_MINUTES, time.minute)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        callImplicitIntent(intent, context)
    }
}

