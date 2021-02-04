package com.justai.aimybox.assistant.skills

import android.content.Context
import android.content.Intent
import android.provider.Settings.ACTION_SETTINGS
import android.provider.Settings.ACTION_WIFI_SETTINGS
import com.justai.aimybox.Aimybox
import com.justai.aimybox.api.aimybox.AimyboxRequest
import com.justai.aimybox.api.aimybox.AimyboxResponse
import com.justai.aimybox.core.CustomSkill
import com.justai.aimybox.model.Response

class SettingsSkill(private val context: Context): CustomSkill<AimyboxRequest, AimyboxResponse> {
    override fun canHandle(response: AimyboxResponse) =
        response.action == "openSettings"

    override suspend fun onResponse(
        response: AimyboxResponse,
        aimybox: Aimybox,
        defaultHandler: suspend (Response) -> Unit
    ) {
        val intent = Intent(ACTION_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        aimybox.standby()
        context.startActivity(intent)
    }
}