package com.justai.aimybox.assistant.skills

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.CalendarContract
import androidx.annotation.RequiresApi
import com.justai.aimybox.Aimybox
import com.justai.aimybox.api.aimybox.AimyboxRequest
import com.justai.aimybox.api.aimybox.AimyboxResponse
import com.justai.aimybox.core.CustomSkill
import com.justai.aimybox.model.Response


class ReminderSkill(context_: Context) : CustomSkill<AimyboxRequest, AimyboxResponse> {

    override fun canHandle(response: AimyboxResponse) = response.action == "setReminder"
    private var context: Context = context_

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun onResponse(
        response: AimyboxResponse,
        aimybox: Aimybox,
        defaultHandler: suspend (Response) -> Unit
    ) {
        val message = response.data?.asJsonObject?.get("text")?.asString ?: "Some important reminder!"
        val timestamp = response.data?.asJsonObject?.get("timestamp")?.asLong ?: 0

        this.createReminder(message, timestamp)
    }

    private fun createReminder(title: String, ts: Long) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, title)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, ts)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, ts + 12000)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        callImplicitIntent(intent, context)
    }

}

