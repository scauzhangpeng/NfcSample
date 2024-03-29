package com.ppy.nfcsample.card.reader

import com.ppy.nfclib.NfcManagerCompat
import com.ppy.nfcsample.card.DefaultCardInfo
import java.io.IOException
import java.util.*

/**
 * Created by ZP on 2018/1/5.
 */

class CardClient private constructor(builder: Builder) {

    private val readers: List<IReader>
    private val chain: IReader.Chain
    private var mNfcCardReaderManager: NfcManagerCompat


    init {
        this.readers = builder.readers
        this.mNfcCardReaderManager = builder.mNfcCardReaderManager
        chain = CardReaderChain(this.readers)
    }

    @Throws(IOException::class)
    fun execute(): DefaultCardInfo? {
        return chain.proceed(mNfcCardReaderManager)
    }


    class Builder {
        val readers = ArrayList<IReader>()
        lateinit var mNfcCardReaderManager: NfcManagerCompat

        fun addReader(reader: IReader) = apply {
            readers.add(reader)
        }

        fun nfcManager(manager: NfcManagerCompat) = apply {
            mNfcCardReaderManager = manager
        }

        fun build(): CardClient {
            return CardClient(this)
        }
    }
}
