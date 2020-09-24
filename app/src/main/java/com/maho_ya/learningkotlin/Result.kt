package com.maho_ya.learningkotlin

import java.lang.Exception

// 直訳すると封印されたクラス。ジェネリクスはつけてもつけなくても作成可能。
// enumのようにwhenで処理を分けることができ、それぞれをobjectやdata classで持つことができる。
// パラメータが必要ない場合はobjectを、必要な場合はdata classを使用する。
// これにより時間のかかる処理や、レスポンスにより取得するデータを変えるなど処理によって必要なデータが変わるようなものが
// シンプルに書けるようになる。

// Sealed classは同ファイル内のみ継承可能。基本は自身内に宣言したdata classなどに継承させる
sealed class Result<out R> {
    object Loading: Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception): Result<Nothing>()

    // メソッドやインタフェースを実装することも可能
    fun foo(): String =
        "foo"
}

// Resultのインスタンスから直接dataを取得できる拡張プロパティを作成。
// セーフキャストにより ResultがSuccessでない場合やnullの場合はnullを返す。
// Successのプロパティのため、通常は is Success -> result.data のようにしか取得できない。
// 例: val data = result.data
val <T> Result<T>.data: T?
    get() = (this as? Result.Success)?.data
