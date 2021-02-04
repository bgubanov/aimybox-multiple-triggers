//package com.justai.aimybox.assistant.pocketsphinx
//
//import com.justai.aimybox.speechkit.pocketsphinx.PocketsphinxRecognizerProvider
//import com.justai.aimybox.voicetrigger.VoiceTrigger
//import edu.cmu.pocketsphinx.Hypothesis
//import edu.cmu.pocketsphinx.RecognitionListener
//import java.io.File
//import java.lang.Exception
//
//class PocketSphinxMultipleVoiceTrigger(
//    recognizerProvider: PocketsphinxRecognizerProvider,
//    val phrases: Collection<Pair<String, String>>
//) : VoiceTrigger {
//
//    companion object {
//        private const val TRIGGER_SEARCH = "trigger"
//    }
//
//    private val recognizer = recognizerProvider.recognizer
//
//    init {
//        val phrasesString = phrases.joinToString(separator = "\n") { "${it.first} /${it.second}/" }
//        val phrasesFile = File.createTempFile("keywords", ".txt")
//        phrasesFile.writeText(phrasesString)
//        recognizer.addKeywordSearch(TRIGGER_SEARCH, phrasesFile)
//    }
//
//    private lateinit var listener: TriggerListener
//
//    override fun destroy() {
//        recognizer.cancel()
//        recognizer.shutdown()
//    }
//
//    override suspend fun startDetection(
//        onTriggered: (phrase: String?) -> Unit,
//        onException: (e: Throwable) -> Unit
//    ) {
//        listener = TriggerListener(onTriggered, onException)
//        recognizer.addListener(listener)
//        recognizer.startListening(TRIGGER_SEARCH)
//    }
//
//    override suspend fun stopDetection() {
//        recognizer.cancel()
//        recognizer.removeListener(listener)
//    }
//
//    private class TriggerListener(
//        private val onTriggered: (phrase: String?) -> Unit,
//        private val onException: (e: Throwable) -> Unit
//    ) : RecognitionListener {
//        override fun onResult(hyp: Hypothesis?) {}
//
//        override fun onPartialResult(hyp: Hypothesis?) {
//            hyp?.let { onTriggered(hyp.hypstr) }
//        }
//
//        override fun onTimeout() {}
//
//        override fun onBeginningOfSpeech() {}
//
//        override fun onEndOfSpeech() {}
//
//        override fun onError(e: Exception?) {
//            e?.let(onException)
//        }
//
//    }
//}