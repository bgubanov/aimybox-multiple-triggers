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

    val regexList = listOf(
//        Regex("\\{.+\\}"), //json regex
        Regex("AimyboxBotResponse\\(.+\\)")
    )

    private val TextReply.hasJson: Boolean
        get() = (tts ?: text).run { regexList.any { regex -> this.matches(regex) } }

    private fun TextReply.createReplyWithoutJson(): TextReply {
        var newTts = (tts ?: text)
        regexList.forEach { newTts = newTts.replace(it, "") }
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