package com.justai.aimybox.assistant

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.justai.aimybox.Aimybox
import com.justai.aimybox.api.aimybox.AimyboxDialogApi
import com.justai.aimybox.assistant.skills.*
import com.justai.aimybox.components.AimyboxProvider
import com.justai.aimybox.core.Config
import com.justai.aimybox.speechkit.google.platform.GooglePlatformSpeechToText
import com.justai.aimybox.speechkit.google.platform.GooglePlatformTextToSpeech
import com.justai.aimybox.speechkit.kaldi.KaldiAssets
import com.justai.aimybox.speechkit.kaldi.KaldiVoiceTrigger
import com.justai.aimybox.voicetrigger.VoiceTrigger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.*
import kotlin.coroutines.CoroutineContext

class AimyboxApplication : Application(), AimyboxProvider, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Default + Job()

    companion object {
        private const val AIMYBOX_API_KEY = ""
        private const val AIMYBOX_WEBHOOK_URL_1 =
            "https://bot.jaicp.com/chatapi/webhook/zenbox/RFJNzBSV:558ae815755abc93cf9da60d2258d8366c57edee"
        private const val AIMYBOX_WEBHOOK_URL_2 = AIMYBOX_WEBHOOK_URL_1

    }

    var marusyaTriggers = listOf("маруся")
        private set

    var solarTriggers = listOf("солар", "соло", "сама", "самар", "салат", "с авар", "со лар")
        private set

    private val listTriggers
        get() = marusyaTriggers + solarTriggers

    fun updateTriggerWords(marusya: List<String>, solar: List<String>) = launch {
        aimybox.mute().join()
        marusyaTriggers = marusya
        solarTriggers = solar
        kaldiVoiceTrigger.updateTriggers(listTriggers)
        aimybox.unmute()
    }

    private val setOfSkills by lazy {
        linkedSetOf(
            AlarmSkill(this), ReminderSkill(this),
            SettingsSkill(this), TimerSkill(this),
            ChangeBotSkill(this)
        )
    }

    private val speechToText = GooglePlatformSpeechToText(this, Locale("RU"))

    private val marusyaTextToSpeech by lazy {
        GooglePlatformTextToSpeech(this, Locale("Ru"))
    }

    private val solarTextToSpeech by lazy {
        GooglePlatformTextToSpeech(this, Locale("Ru"))
    }

    private val kaldiVoiceTrigger by lazy {
        val kaldiAssets = KaldiAssets.fromApkAssets(this, "kaldi/ru")
        KaldiVoiceTrigger(kaldiAssets, listTriggers)
    }

    private val currentVoiceTrigger by lazy { kaldiVoiceTrigger }

    val firstAimyboxConfig by lazy {
        val unitId = UUID.randomUUID().toString()

        val dialogApi = AimyboxDialogApi(
            AIMYBOX_API_KEY, unitId, AIMYBOX_WEBHOOK_URL_1, setOfSkills
        )

        Config.create(speechToText, marusyaTextToSpeech, dialogApi) {
            this.voiceTrigger = currentVoiceTrigger
        }
    }

    val secondAimyboxConfig by lazy {
        val unitId = UUID.randomUUID().toString()

        val dialogApi = AimyboxDialogApi(
            AIMYBOX_API_KEY, unitId, AIMYBOX_WEBHOOK_URL_2, setOfSkills
        )

        Config.create(speechToText, solarTextToSpeech, dialogApi) {
            voiceTrigger = currentVoiceTrigger
        }
    }

    @ExperimentalCoroutinesApi
    @SuppressLint("MissingPermission")
    fun listenTriggers() {
        launch {
            aimybox.voiceTriggerEvents.asFlow()
                .distinctUntilChanged().collect {
                    try {
                        if (it is VoiceTrigger.Event.Triggered) {
                            Log.e("ChangeTrigger", "Triggered: \"${it.phrase}\", ${it.hashCode()}")
                            if (it.phrase?.trim() in marusyaTriggers) {
                                if (aimybox.config !== firstAimyboxConfig)
                                    aimybox.updateConfiguration(firstAimyboxConfig)
                            } else {
                                if (aimybox.config !== secondAimyboxConfig)
                                    aimybox.updateConfiguration(secondAimyboxConfig)
                            }
                        }
                    } catch (t: Throwable) {
                        Log.e("listenTrigger Exception", t.stackTrace.toString())
                        aimybox.standby().join()
                    }
                }
        }
    }

    override val aimybox by lazy { Aimybox(firstAimyboxConfig) }
}