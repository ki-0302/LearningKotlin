package com.maho_ya.learningkotlin

// シングルトンで利用や定数クラスとしての利用が主
// Utilクラスとしても利用できるが拡張関数で代用できないか考えること
object ObjectClass {

    // 定位としてして使用する方法
    object Common {
        const val APP_NAME = "Super App Name"
        const val SITE_URL = "https://www.example.com/"
    }
}