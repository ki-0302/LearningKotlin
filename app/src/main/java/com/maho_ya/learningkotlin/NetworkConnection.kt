package com.maho_ya.learningkotlin

class NetworkConnection(
    val status: Status,
    val connectionStatus: ConnectionStatus
)

enum class Status {
    OK,
    CANCEL,
    RETRY
}

// Enumの値を定義する場合
enum class ConnectionStatus(
    val rawString: String
) {
    OK("ok"),
    CANCEL("cancel"),
    RETRY("retry"),
    UNKNOWN("unknown")  // フォールバック用。意図しない値がある場合などにこれを使用するようにコーディングで調整する。
}
