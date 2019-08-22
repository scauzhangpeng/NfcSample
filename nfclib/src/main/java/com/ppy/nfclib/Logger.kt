package com.ppy.nfclib

/**
 * Created by ZP on 2019-08-22.
 */
class Logger private constructor() {
    private var printer: Printer? = null

    fun println(tag: String, message: String) {
        printer?.println(tag, message)
    }

    fun println(tag: String, message: String, exception: Exception) {
        printer?.println(tag, message, exception)
    }

    fun setUserPrinter(printer: Printer?) {
        this.printer = printer
    }

    companion object {
        fun get(): Logger {
            return Inner.INSTANCE
        }
    }

    private object Inner {
        val INSTANCE = Logger()
    }
}