package com.ppy.nfclib;

import android.util.SparseArray;

/**
 * Created by ZP on 2017/9/27.
 */

public class ExceptionConstant {

    protected static final SparseArray<String> mNFCException = new SparseArray<>();
    protected static final int NFC_NOT_EXIT = 0;
    protected static final int NFC_NOT_ENABLE = 1;

    static {
        mNFCException.put(NFC_NOT_EXIT, "do not support nfc");
        mNFCException.put(NFC_NOT_ENABLE, "do not open nfc");
    }
}
