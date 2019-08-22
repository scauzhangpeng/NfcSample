package com.ppy.nfcsample

import android.util.Log
import com.ppy.nfclib.Printer

/**
 * Created by ZP on 2019-08-22.
 */
class LoggerImpl : Printer {
    override fun println(tag: String, message: String, exception: Exception) {
        exception.printStackTrace()
        Log.d(tag, message)
    }

    override fun println(tag: String, message: String) {
        Log.d(tag, message)
    }
}