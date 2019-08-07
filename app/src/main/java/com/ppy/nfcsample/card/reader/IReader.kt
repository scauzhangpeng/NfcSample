package com.ppy.nfcsample.card.reader

import com.ppy.nfclib.NfcCardReaderManager
import com.ppy.nfcsample.card.DefaultCardInfo

import java.io.IOException

/**
 * Created by ZP on 2018/1/4.
 */

interface IReader {

    val type: Int

    @Throws(IOException::class)
    fun readCard(nfcCardReaderManager: NfcCardReaderManager): DefaultCardInfo?

    interface Chain {
        @Throws(IOException::class)
        fun proceed(nfcCardReaderManager: NfcCardReaderManager): DefaultCardInfo?
    }
}
