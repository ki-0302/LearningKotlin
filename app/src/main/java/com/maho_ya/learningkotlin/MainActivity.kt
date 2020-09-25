package com.maho_ya.learningkotlin

import android.app.Activity
import android.graphics.Color
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
        toUseLambda()
        toUseObjectClass()
        toUseCompanionObject()
        toUseScope()
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

    // KotlinでのLambda
    private fun toUseLambda() {

        // lamdaで型を宣言する。例では (Int, Int) -> Int)
        val lamda: ((Int, Int) -> Int) = { value, value2 ->
            value + value2
        }

        // 呼び出し方法
        Log.d("toUseLamda", lamda(10, 50).toString())

        // Lambdaでlistnerを持つようなNullableな場合、Lamdaの後にオプショナルを付ける
        var lambdaListener: ((Int) -> Unit)? = null
        lambdaListener = {
            Log.d("toUseLamda", "lamdaListener: $it")
        }

        lambdaListener(3)
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

    // Scope functions スコープ関数
    // プロジェクトによっては、thisの扱いがスコープ内部と外で変わるため with, run, applyは使用しないルールになっている場合もある。
    private fun toUseScope() {

        // * 各戻り値の違い。
        // let, run, with : lambda resultを返す。そのためContextオブジェクトとは違う値を返せる
        // apply, also : Contextオブジェクト自身を返す

        // * let
        // non-nullの場合のみ処理するコードブロックとして使用されるのが一般的。
        // ?.let {} のようにする。?はnull以外処理するため、このような記述になる。
        val letString: String? = "letString"
        letString?.let {
          Log.d("toUseScope-let", it)
        }

        // itを別な変数名に変更することで可読性をあげるような使い道もある。
        val numberList = listOf(1, 2, 3)
        val fistResult: Int = numberList.first().let {first ->
            first + 5   // lambdaの戻り値が返る
        }

        // 引数が1つの関数の場合、 .let(::関数名) でitを引数に渡せる。.let{}でないことに注意
        val letValue = "letValue"
        letValue.let (::println)
        letValue.let (MyUtils::convertName)

        // * with
        // 非拡張関数（non-extension function）。レシーバーはthisになる。

        // 戻り値はlambda result。
        // context objectのメソッドの呼び出しをしたいだけの場合が主な利用方法。
        val textValue = with(findViewById<TextView>(R.id.textView)) {
            text = "with"
            setTextColor(Color.BLUE)
            println(this.text)
            // クラスのインスタンスを呼び出したい時は@クラス名を付ける
            println(this@MainActivity.toString())
            text.toString()
        }
        Log.d("toUseScope-with", textValue)

        // * run
        // レシーバーはthisになる。
        // 基本はwithと同じ動作だが、拡張関数としてletが実行される
        // letで初期化処理を行いたい場合が主な用途。
        // withとほぼ同じ動きだが、withは非拡張関数のためコードの統一性を考えるとこちらのほうが一般的。
        val textRunValue = findViewById<TextView>(R.id.textView).run {
            text = "run"
            setTextColor(Color.RED)
            println(this.text)
            "$text Label"
        }

        // letでrunと同じことをしようとした場合の例。itが必要になる
        findViewById<TextView>(R.id.textView).let {
            it.text = "Example of using let instead of run"
            it.setTextColor(Color.RED)
        }

        // 非拡張関数でもrunは使用できる。戻り値はlambda result。
        // コードブロックを分けたい場合に使用する。
        run {
            val a1 = 3
            val a2 = 5
            Log.d("toUseScope-run non-ex", (a1 + a2).toString())
        }

        // * apply
        // context objectに適用するという意味。
        // レシーバーはthis。
        // 戻り値はcontext object自身となる。
        // オブジェクトのメソッドやメンバーをまとめて操作する場合に使用する。
        val textViewApply = findViewById<TextView>(R.id.textView).apply {
            text = "apply"
            setTextColor(Color.RED)
            println(this.text)
        }
        println(textViewApply.text)


        // * also
        // オブジェクトを使って次のようにするという意味。
        // 戻り値はcontext object自身となる。
        // 引数はitになる。
        // 関心がオブジェクトのメソッドやプロパティの変更より、オブジェクト自身にある場合に使用する。
        val textViewAlso = findViewById<TextView>(R.id.textView).also {
            it.text = "let"
            it.setTextColor(Color.RED)
        }
        println(textViewAlso.text)

        // letと同じ用に引数の変数名は変更できる。
        val textViewAlsoTextView = findViewById<TextView>(R.id.textView).also { textView ->
            textView.text = "let"
            textView.setTextColor(Color.RED)
        }
        println(textViewAlsoTextView.text)
    }
}

// 拡張関数。トップレベルで宣言した場合は全体で使用可能
fun Extension.send() =
    "Extension Function send"

// 拡張プロパティ。getが必須
val Extension.count: Int
    get() = 3

