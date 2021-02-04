package com.justai.aimybox.assistant

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.justai.aimybox.components.AimyboxAssistantFragment
import com.justai.aimybox.components.extensions.isPermissionGranted
import com.justai.aimybox.core.Config
import kotlinx.android.synthetic.main.layout_activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        const val REQUEST_CODE_AUDIO = 100
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Default + Job()

    val Activity.app
        get() = (application as AimyboxApplication)

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        withAudioPermission {
            initAimybox()
        }
    }


    private fun initTextFields() {
        triggers_1.setText(app.marusyaTriggers.joinToString())
        triggers_2.setText(app.solarTriggers.joinToString())
        button_submit.setOnClickListener {
            val marusya = triggers_1.text.split(", ", " ", ",").filter { it.isNotBlank() }
            val solar = triggers_2.text.split(", ", " ", ",").filter { it.isNotBlank() }
            launch {
                app.updateTriggerWords(marusya, solar).join()
                Toast.makeText(this@MainActivity, "Триггеры обновлены", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun initAimybox() {
        val assistantFragment = AimyboxAssistantFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.assistant_container, assistantFragment)
            commit()
        }
        (application as? AimyboxApplication)?.listenTriggers()
        initTextFields()
    }

    override fun onBackPressed() {
        val assistantFragment = (supportFragmentManager.findFragmentById(R.id.assistant_container)
                as? AimyboxAssistantFragment)
        if (assistantFragment?.onBackPressed() != true) super.onBackPressed()
    }

    private fun withAudioPermission(action: () -> Unit) {
        if (this.isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
            action()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_CODE_AUDIO)
            }
        }
    }

    private val snackbarRequestPermissions by lazy {
        Snackbar.make(
            findViewById(R.id.assistant_container),
            "Работа приложения невозможна без предоставления разрешений", Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("Предоставить") {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQUEST_CODE_AUDIO
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_AUDIO
            && permissions.firstOrNull() == Manifest.permission.RECORD_AUDIO
        ) {
            if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                initAimybox()
                snackbarRequestPermissions.dismiss()
            } else {
                snackbarRequestPermissions.show()
            }
        }
    }

}