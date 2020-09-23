package com.maho_ya.learningkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    // Stringに対し拡張プロパティを追加。private宣言なのでクラス内のみ適用される。
    private val String.isJapan: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nullSafety(savedInstanceState)
        checkEnum()
        extensionFunctionAndProperties()
    }

    // Null安全
    private fun nullSafety(savedInstanceState: Bundle?) {

        // 基本的にすべてをオプショナル型で宣言するのではなく、設計に合わせ意図がわかる形にするべき。
        // nullを許容すべきでない場合は使用しない

        // ?が付くものはオプショナル型でnullを許容する
        val item: String? = "item"
        // アンラップ
        item?.contains("sword")
        // 強制アンラップ。強制的にnullでないものとして扱う。itemにnullが入っていた場合はクラッシュする
        item!!.contains("shield")

        val name = null

        // nullは許容されないためこの指定はビルドエラーになる
        // Log.d("MainActivity", name)

        // nullの場合、空文字を返す拡張関数
        Log.d("MainActivity", name.orEmpty())

        // Safe Calls. nullSafetyTestがNullでなければdisplayLogが実行される
        val nullSafetyTest: NullSafetyTest? = null
        nullSafetyTest?.displayLog("test")

        if (nullSafetyTest != null) {
            // Smart Cast. ifでnullでないことを保証しているため .?無しで利用できる
            val formatLog = nullSafetyTest.formatLog("Test")
        }

        // エルビス演算子（三項演算子）によって、nullの場合はBundleを生成する。これによりこれ以降bundleはnullでないことが保証される
        val bundle = savedInstanceState ?: Bundle()
        bundle.containsKey("Test")

        // let内はnullでない時に実行される
        savedInstanceState?.let {
            it.containsKey("Test")
        }

        // Guard構文。これ以降期待する値を保証するために用いられる。ifのネストを深くしないためにも用いられる
        // これ以降、nullでないことが保証される
        savedInstanceState ?: return

        savedInstanceState.containsKey("Test")

        val formatLog2: String? = nullSafetyTest?.formatLog("Test2")
        formatLog2.equals("3")
    }

    // enumの使用
    private fun checkEnum() {

        val networkConnection = NetworkConnection(Status.OK, ConnectionStatus.CANCEL)

        // RETRYがない警告がwhenに出る
        when (networkConnection.status) {
            Status.OK -> "OK"
            Status.CANCEL -> "Cancel"
        }

        // non nullを強制することで、すべてのenumの値の入力を強制できる。この場合、値が足りないとビルドエラーになる。
        val status  = when (networkConnection.status) {
            Status.OK -> "OK"
            Status.CANCEL -> "Cancel"
            Status.RETRY -> "Retry"
        }

        // この場合は文字列型なので警告はでない。必要に迫られなければ上記のようにenumで制御する
        when (networkConnection.connectionStatus.rawString) {
            "ok" -> "It's ok"
        }
    }

    // 拡張関数・拡張プロパティ。既存の様々なクラスを拡張できる。例:Stringなど
    private fun extensionFunctionAndProperties() {

        val extension = Extension()

        // 拡張関数はIDE上で黄色で表示される
        Log.d("extensionFunction", extension.send())
        Log.d("extensionProperties", extension.count.toString())

        val foo: String = "foo"
        Log.d("extensionProperties", foo.isJapan.toString())
    }
}

// 拡張関数。トップレベルで宣言した場合は全体で使用可能
fun Extension.send() =
    "Extension Function send"

// 拡張プロパティ。getが必須
val Extension.count: Int
    get() = 3

