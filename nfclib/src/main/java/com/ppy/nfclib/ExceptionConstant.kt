package com.ppy.nfclib

import android.util.SparseArray

/**
 * Created by ZP on 2017/9/27.
 */

object ExceptionConstant {

    internal val mNFCException = SparseArray<String>()
    const val NFC_NOT_EXIT = 0
    const val NFC_NOT_ENABLE = 1

    init {
        mNFCException.put(NFC_NOT_EXIT, "do not support nfc")
        mNFCException.put(NFC_NOT_ENABLE, "do not open nfc")
    }
}
