package com.ppy.nfcsample.card.reader

import com.ppy.nfclib.NfcManagerCompat
import com.ppy.nfcsample.card.DefaultCardInfo

import java.io.IOException

/**
 * Created by ZP on 2018/1/4.
 */

interface IReader {

    val type: Int

    @Throws(IOException::class)
    fun readCard(nfcCardReaderManager: NfcManagerCompat): DefaultCardInfo?

    interface Chain {
        @Throws(IOException::class)
        fun proceed(nfcCardReaderManager: NfcManagerCompat): DefaultCardInfo?
    }
}
