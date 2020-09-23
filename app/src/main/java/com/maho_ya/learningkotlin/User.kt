package com.maho_ya.learningkotlin

// data classはデータを保持するためだけのクラスに使用する
data class User(
    val firstName: String,
    val lastName: String,
    var weight : Float
) {
    // 関数やプロパティも宣言可能
    fun formatOnamae() =
        "お名前: $lastName"

    val fullName: String
        get() = "$lastName $firstName"
}