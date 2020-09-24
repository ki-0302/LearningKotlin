package com.maho_ya.learningkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.maho_ya.learningkotlin.Result

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
        toUseDataClass()
        toUseSealedClass()
        toUseSamConversions()
        toUseLamda()
        toUseObjectClass()
        toUseCompanionObject()
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

    // data class。マスターのデータ等に使用する。変更の必要がなければプロパティは原則valで宣言すると安全
    private fun toUseDataClass() {

        val user = User("Tanaka", "Taro", 70.5f)

        // copy()でインスタンスの内容をコピーして、別インスタンスを作成。
        val modify = user.copy()
        modify.weight = 80f

        Log.d("toUseDataClass",
            (user == modify).toString() + " user:" + user.weight.toString() + " modify:" + modify.weight.toString())
    }

    // Sealed class。非同期のデータの取得処理など状態が変わる処理が書きやすくなる
    private fun toUseSealedClass() {

        val result :Result<User> =
            Result.Success(User("Tanaka", "Taro", 50f))

        val resultMessage = when (result) {
            is Result.Loading -> "Now Loading..."
            is Result.Success -> result.data.firstName
            is Result.Error -> result.exception.message.orEmpty()
        }

        Log.d("toUseSealedClass", resultMessage)
    }

    // SAM変換（Single Abstract Method）
    // 名前の通り、interfaceに1つの抽象メソッドがある場合のみに適用される
    private fun toUseSamConversions() {

        val interfaceTest = InterfaceTest()

        // JavaはSAM変換可能
        interfaceTest.setJavaOnClickListener {
            Log.d("I/F Java", "onEvent")
        }

        // 1.3までのKotlinのInterfaceはSAM変換ができず、object式で無名クラスのオブジェクト（オブジェクト宣言）する必要があった
        interfaceTest.setKotlinOnClickListener(object : KotlinInterface {
            override fun onEvent() {
                Log.d("I/F Kotlin", "onEvent")
            }
        })

        // 1.4からはfun interfaceを使用するとSAM変換できるようになった
        interfaceTest.setKotlinOnClickListener1_4 {
            Log.d("I/F 1_4", "onEvent")
        }

        // 引数あり。発火する側でパラメータはセットする
        interfaceTest.setKotlinOnClickListener1_4a { count ->
            Log.d("I/F 1_4a", "onEventArg $count")
        }

        interfaceTest.runEvent(5)
    }

    // KotlinでのLamda
    private fun toUseLamda() {

        // lamdaで型を宣言する。例では (Int, Int) -> Int)
        val lamda: ((Int, Int) -> Int) = { value, value2 ->
            value + value2
        }

        // 呼び出し方法
        Log.d("toUseLamda", lamda(10, 50).toString())

        // Lamdaでlistnerを持つようなNullableな場合、Lamdaの後にオプショナルを付ける
        var lamdaListener: ((Int) -> Unit)? = null
        lamdaListener = {
            Log.d("toUseLamda", "lamdaListener: $it")
        }

        lamdaListener(3)
    }

    // Objectクラスの利用
    private fun toUseObjectClass() {
        Log.d("toUseObjectClass", ObjectClass.Common.APP_NAME)
    }

    // Companion Object
    private fun toUseCompanionObject() {
        // データバインディングをXMLレイアウト上で使用するには@JvmStaticを使用する必要がある
        MyUtils.convertName("test")

        val cat = MyUtils.factory()
    }
}

// 拡張関数。トップレベルで宣言した場合は全体で使用可能
fun Extension.send() =
    "Extension Function send"

// 拡張プロパティ。getが必須
val Extension.count: Int
    get() = 3

