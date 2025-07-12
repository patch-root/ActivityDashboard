package activity.dashboard.common.logging

import activity.dashboard.common.Env

object Logger {
    fun log(message: String) {
        if (Env.IS_DEBUG) {
            println("[Debug]: $message")
        }
    }

    fun warn(message: String) {
        if (Env.IS_DEBUG) {
            println("[Warn]: $message")
        }
    }

    fun error(
        message: String,
        throwable: Throwable? = null,
    ) {
        println("[Error]: $message")
        throwable?.let { println(it.stackTraceToString()) }
    }
}
