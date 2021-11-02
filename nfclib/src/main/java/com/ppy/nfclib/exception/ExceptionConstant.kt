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

    init {
        mNFCException.put(NFC_NOT_EXIT, "this device does not have NFC support")
        mNFCException.put(NFC_NOT_ENABLE, "this device does not have NFC enable")
    }
}
