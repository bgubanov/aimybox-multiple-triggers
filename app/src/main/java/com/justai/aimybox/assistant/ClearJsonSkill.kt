package com.justai.aimybox.assistant

import com.justai.aimybox.Aimybox
import com.justai.aimybox.api.aimybox.AimyboxRequest
import com.justai.aimybox.api.aimybox.AimyboxResponse
import com.justai.aimybox.core.CustomSkill
import com.justai.aimybox.model.Response
import com.justai.aimybox.model.reply.TextReply

object ClearJsonSkill : CustomSkill<AimyboxRequest, AimyboxResponse> {
    override fun canHandle(response: AimyboxResponse) = response.replies.any {
        it is TextReply && it.hasJson
    }

    override suspend fun onResponse(
        response: AimyboxResponse,
        aimybox: Aimybox,
        defaultHandler: suspend (Response) -> Unit
    ) = defaultHandler(clearResponse(response))

    private val jsonRegex = Regex("\\{.+}")

    private val TextReply.hasJson: Boolean
        get() = (tts ?: text).contains(jsonRegex)

    private fun TextReply.createReplyWithoutJson(): TextReply {
        val newTts = (tts ?: text).replace(jsonRegex, "")
        return TextReply(text, newTts, language)
    }

    private fun clearResponse(response: AimyboxResponse): AimyboxResponse =
        AimyboxResponse(
            json = response.json,
            replies = response.replies.map {
                if (it is TextReply)
                    it.createReplyWithoutJson()
                else it
            }
        )
}