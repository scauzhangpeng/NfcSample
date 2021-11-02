package com.ppy.nfclib

import android.content.Intent
import com.ppy.nfclib.util.Util
import java.io.IOException

/**
 * Nfc管理类，提供一系列外部调用接口.
 */
interface INfcManagerCompat {
    /**
     * 卡片是否已连接.
     */
    fun isCardConnect(): Boolean

    fun onNewIntent(intent: Intent)

    @Throws(IOException::class)
    fun sendData(data: ByteArray): String

    @Throws(IOException::class)
    fun sendData(hexData: String): String {
        return sendData(Util.hexStringToByteArray(hexData))
    }

    @Throws(IOException::class)
    fun tranceive(data: ByteArray): ByteArray

    @Throws(IOException::class)
    fun tranceive(hexData: String): ByteArray {
        return tranceive(Util.hexStringToByteArray(hexData))
    }
}