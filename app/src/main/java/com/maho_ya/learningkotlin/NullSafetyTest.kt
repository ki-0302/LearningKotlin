package com.maho_ya.learningkotlin

import android.util.Log

class NullSafetyTest {

    fun displayLog(message: String) {
        Log.d("NullSafetyTest", message)
    }
    fun formatLog(message: String) =
        "Log: $message"
}
