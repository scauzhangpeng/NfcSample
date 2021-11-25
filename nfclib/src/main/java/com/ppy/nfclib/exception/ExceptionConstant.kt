package com.ppy.nfclib.exception

import android.util.SparseArray

/**
 * 异常类常量.
 * Created by ZP on 2017/9/27.
 */

object ExceptionConstant {

    internal val mNFCException = SparseArray<String>()
    /**
     * 手机不支持NFC.
     */
    const val NFC_NOT_EXIT = 0
    /**
     * 手机NFC未开启.
     */
    const val NFC_NOT_ENABLE = 1

    /**
     * 连接卡片失败.
     */
    const val CONNECT_TAG_FAIL = 2

    const val NFC_PERMISSION_NOT_GRANTED = 3

    const val NFC_PERMISSION_UNKNOWN = 4

    const val NFC_PERMISSION_ASK = 5

    init {
        mNFCException.put(NFC_NOT_EXIT, "this device does not have NFC support")
        mNFCException.put(NFC_NOT_ENABLE, "this device does not have NFC enable")
        mNFCException.put(NFC_PERMISSION_NOT_GRANTED, "this app does not have NFC permission")
        mNFCException.put(NFC_PERMISSION_UNKNOWN, "this app does not have NFC permission")
        mNFCException.put(NFC_PERMISSION_ASK, "this app does not have NFC permission")
    }
}
