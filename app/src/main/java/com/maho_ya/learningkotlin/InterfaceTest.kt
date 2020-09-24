package com.maho_ya.learningkotlin

import android.util.Log

class InterfaceTest {

    private var listener: KotlinInterface1_4a? = null

    fun runEvent(count: Int) {
        // オプショナルにすることでlistenerがnullの場合は発火しないようにできる。
        listener?.onEventArg(count)
    }

    // 実際のコードでは発火させるタイミングは検討すること。
    fun setKotlinOnClickListener(kotlinInterface: KotlinInterface) {
        Log.d("I/F Kotlin", "Failed SAM conversions")
        kotlinInterface.onEvent()
    }

    fun setKotlinOnClickListener1_4(kotlinInterface1_4: KotlinInterface1_4) {
        Log.d("I/F 1_4", "Success SAM conversions")
        kotlinInterface1_4.onEvent()
    }

    fun setKotlinOnClickListener1_4a(kotlinInterface1_4a: KotlinInterface1_4a) {
        Log.d("I/F 1_4a", "Success SAM conversions")
        listener = kotlinInterface1_4a
    }

    fun setJavaOnClickListener(javaInterface: JavaInterface) {
        Log.d("I/F Java", "Success SAM conversions")
        javaInterface.onEvent()
    }
}