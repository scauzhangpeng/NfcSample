package com.ppy.nfclib

import android.content.Intent

import java.io.IOException

/**
 * NFC调用相关接口.
 * Created by ZP on 2017/9/20.
 */

interface INfcCardReader {

    fun isCardConnect(): Boolean

    fun onCreate(intent: Intent)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy()

    fun onNewIntent(intent: Intent)

    @Throws(IOException::class)
    fun sendData(data: ByteArray): String

    @Throws(IOException::class)
    fun sendData(hexData: String): String

    @Throws(IOException::class)
    fun tranceive(data: ByteArray): ByteArray

    @Throws(IOException::class)
    fun tranceive(hexData: String): ByteArray

}
