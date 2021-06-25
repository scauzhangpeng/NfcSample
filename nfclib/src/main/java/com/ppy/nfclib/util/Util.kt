package com.ppy.nfclib.util

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import java.util.*
import java.util.concurrent.Executor

/**
 * NFC相关工具类.
 * Created by ZP on 2017/9/20.
 */

object Util {

    /**
     * Utility class to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    fun byteArrayToHexString(bytes: ByteArray): String {
        val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
        val hexChars = CharArray(bytes.size * 2)
        var v: Int
        for (j in bytes.indices) {
            v = (bytes[j].toInt() and 0xFF)
            hexChars[j * 2] = hexArray[v.ushr(4)]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    /**
     * Utility class to convert a hexadecimal string to a byte string.
     *
     *
     * Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     */
    fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    /**
     * 金额单位转换，分转元.
     *
     * @param value 金额，单位分
     * @return 金额， 单位元
     */
    fun toAmountString(value: Long): String {
        return String.format(Locale.CHINA, "%.2f", value / 100.0f)
    }

    /**
     * 16进制转10进制.
     *
     * @param hex        16进制
     * @param startIndex 起始下标
     * @param len        长度, 按字符算
     * @return 10进制
     */
    fun hexToInt(hex: ByteArray, startIndex: Int, len: Int): Int {
        var ret = 0

        val e = startIndex + len
        for (i in startIndex until e) {
            ret = ret shl 8
            ret = ret or ((hex[i].toInt() and 0xFF))
        }
        return ret
    }

    /**
     * 16进制转10进制 (小端模式).
     *
     * @param b 16进制
     * @param s 起始下标
     * @param n 长度, 按字符算
     * @return 10进制
     */
    fun hexToIntLittleEndian(b: ByteArray, s: Int, n: Int): Int {
        var n = n
        var ret = 0
        var i = s
        while (i >= 0 && n > 0) {
            ret = ret shl 8
            ret = ret or ((b[i].toInt() and 0xFF))
            --i
            --n
        }
        return ret
    }


    /**
     * 判断手机是否具备NFC功能.
     *
     * @param context [Context]
     * @return `true`: 具备 `false`: 不具备
     */
    fun isNfcExits(context: Context): Boolean {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        return nfcAdapter != null
    }

    /**
     * 判断手机NFC是否开启.
     *
     * OPPO A37m 发现必须同时开启NFC以及Android Beam才可以使用
     * 20180108 发现OPPO单独打开NFC即可读取标签，不清楚是否是系统更新
     *
     * @param context [Context]
     * @return `true`: 已开启 `false`: 未开启
     */
    fun isNfcEnable(context: Context): Boolean {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        //        if (Build.MANUFACTURER.toUpperCase().contains("OPPO")) {
        //            return nfcAdapter.isEnabled() && isAndroidBeamEnable(context);
        //        }
        return nfcAdapter != null && nfcAdapter.isEnabled
    }

    /**
     * 判断手机NFC的Android Beam是否开启，在API 16之后才有，在API 29被废除.
     *
     * @param context [Context]
     * @return `true`: 已开启 `false`: 未开启
     */
    fun isAndroidBeamEnable(context: Context): Boolean {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && nfcAdapter != null && nfcAdapter.isNdefPushEnabled
    }

    /**
     * 判断手机是否具备Android Beam.
     *
     * @param context [Context]
     * @return `true`:具备 `false`:不具备
     */
    fun isAndroidBeamExits(context: Context): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && isNfcExits(context)
    }

    /**
     * 跳转至系统NFC设置界面.
     *
     * @param context [Context]
     * @return `true` 跳转成功 <br></br> `false` 跳转失败
     */
    fun intentToNfcSetting(context: Context): Boolean {
        if (isNfcExits(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                return toIntent(context, Settings.ACTION_NFC_SETTINGS)
            }
        }
        return false

    }

    /**
     * 跳转至系统NFC Android Beam设置界面，同页面基本都有NFC开关.
     *
     * @param context [Context]
     * @return `true` 跳转成功 <br></br> `false` 跳转失败
     */
    fun intentToNfcShare(context: Context): Boolean {
        return if (isAndroidBeamExits(context)) {
            toIntent(context, Settings.ACTION_NFCSHARING_SETTINGS)
        } else false
    }

    /**
     * 跳转方法.
     *
     * @param context [Context]
     * @param action  意图
     * @return 是否跳转成功 `true ` 成功<br></br>`false`失败
     */
    private fun toIntent(context: Context, action: String): Boolean {
        try {
            val intent = Intent(action)
            context.startActivity(intent)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return false
        }

        return true
    }

    internal class MainThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())
        override fun execute(r: Runnable) {
            handler.post(r)
        }
    }

}
