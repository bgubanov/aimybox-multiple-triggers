package com.justai.aimybox.assistant.skills

import com.justai.aimybox.Aimybox
import com.justai.aimybox.api.aimybox.AimyboxRequest
import com.justai.aimybox.api.aimybox.AimyboxResponse
import com.justai.aimybox.assistant.AimyboxApplication
import com.justai.aimybox.core.CustomSkill
import com.justai.aimybox.model.Response

class ChangeBotSkill(private val application: AimyboxApplication): CustomSkill<AimyboxRequest, AimyboxResponse> {
    override fun canHandle(response: AimyboxResponse) =
        response.action == "changeBot"

    override suspend fun onResponse(
        response: AimyboxResponse,
        aimybox: Aimybox,
        defaultHandler: suspend (Response) -> Unit
    ) {
        response.action //todo ловить бота, которого позвать
        aimybox.updateConfiguration(application.firstAimyboxConfig)
    }
}