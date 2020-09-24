package com.maho_ya.learningkotlin

class MyUtils {

    // クラス内のシングルトンとして companion objectを宣言できる。
    // Kotlinではstaticメソッドの宣言ができないため、companion objectを使用するなどの代替案になる
    companion object {

        // 定数宣言、ビルダーパターンの使用が一般的
        private const val START_COUNT = 1

        fun factory(): Cat {
            return Cat("tama")
        }

        // Javaと違いcompanion objectは実行時にインスタンス化されるのでJavaのstaticのように呼べない。
        // 回避方法として@JvmStaticアノテーションを付けることによって、Javaのstaticと同じ用に生成させることができる。
        @JvmStatic
        fun convertName(name: String): String {
            return "Kimiko"
        }

    }
}

class Cat(val name: String)
