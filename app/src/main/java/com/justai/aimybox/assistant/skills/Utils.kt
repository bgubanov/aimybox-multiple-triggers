package com.justai.aimybox.assistant.skills

import android.content.Context
import android.content.Intent

fun callImplicitIntent(intent: Intent, context: Context) {
    if (intent.resolveActivity(context.packageManager) != null)
        context.applicationContext.startActivity(intent)
}
