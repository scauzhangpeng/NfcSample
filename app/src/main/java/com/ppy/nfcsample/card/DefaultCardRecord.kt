package com.ppy.nfcsample.card

import com.ppy.nfclib.util.Util

/**
 * Created by ZP on 2017/9/21.
 *
 *
 * 城市一卡通卡片交易记录实体
 *
 */

class DefaultCardRecord {
    /**
     * 交易类型编码.
     */
    var typeCode: String? = null
    /**
     * 交易类型名称.
     */
    var typeName: String? = null
    /**
     * 交易金额，单位：分.
     */
    var price: Long = 0
    /**
     * 交易日期：yyyyMMddHHmmss.
     */
    var date: String? = null
    /**
     * 交易序列号.
     */
    var serialNumber: String? = null

    @Throws(Exception::class)
    fun readRecord(resp: ByteArray) {
        val record = Util.byteArrayToHexString(resp)
        serialNumber = record.substring(0, 4)
        price = Integer.parseInt(record.substring(10, 18), 16).toLong()
        typeCode = record.substring(18, 20)
        typeName = if ("09" == typeCode) {
            "消费"
        } else {
            "充值"
        }
        date = record.substring(36, 46)
    }
}
