package com.ppy.nfcsample.card

import com.ppy.nfclib.util.Util

/**
 * Created by ZP on 2017/9/29.
 */

class ShenZhenTong : DefaultCardInfo() {

    /**
     * 解析卡片信息,未判断9000.
     *
     */
    fun parseCardInfo(src: ByteArray): Boolean {
        return try {
            val number = Util.hexToIntLittleEndian(src, 19, 4)
            cardNumber = number.toString()
            true
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }

    }

    /**
     * 解析卡片余额.
     *
     * @param src 原始16进制数据
     */
    fun parseCardBalance(src: ByteArray): Boolean {
        return try {
            balance = Util.hexToInt(src, 1, src.size - 2 - 1).toLong()
            true
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }

    }
}
