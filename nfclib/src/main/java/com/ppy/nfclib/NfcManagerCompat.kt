package com.ppy.nfclib

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import com.ppy.nfclib.exception.ConnectTagException
import com.ppy.nfclib.exception.ExceptionConstant
import com.ppy.nfclib.reader.BaseCardReader
import com.ppy.nfclib.reader.NfcCardReader
import com.ppy.nfclib.util.Logger
import com.ppy.nfclib.util.Printer
import com.ppy.nfclib.util.Util
import java.io.IOException
import java.lang.Exception

class NfcManagerCompat(activity: Activity, private var delay: Int = 0, enableSound: Boolean = true, val printer: Printer, val cardOperatorListener: CardOperatorListener? = null): INfcManagerCompat {

    private val mCallback = object : CardReaderInnerCallback {
        override fun onNfcNotExit() {
            cardOperatorListener?.onException(
                ExceptionConstant.NFC_NOT_EXIT,
                ExceptionConstant.mNFCException.get(ExceptionConstant.NFC_NOT_EXIT))
        }

        override fun onNfcNotEnable() {
            cardOperatorListener?.onException(
                ExceptionConstant.NFC_NOT_ENABLE,
                ExceptionConstant.mNFCException.get(ExceptionConstant.NFC_NOT_ENABLE))
        }

        override fun onCardConnected(isConnected: Boolean) {
            cardOperatorListener?.onCardConnected(isConnected)
        }

        override fun onException(exception: Exception) {
            Util.MainThreadExecutor().execute {
                if (exception is ConnectTagException) {
                    cardOperatorListener?.onException(ExceptionConstant.CONNECT_TAG_FAIL, exception.message!!)
                }
            }

        }
    }

    private var mCardReader: BaseCardReader = NfcCardReader.Factory(activity, mCallback)


    init {
        Logger.get().setUserPrinter(printer)
        if (delay < 0) {
            delay = 0
        }
        mCardReader.enablePlatformSound(enableSound)
        mCardReader.setReaderPresenceCheckDelay(delay)
    }


    override fun isCardConnect(): Boolean {
       return mCardReader.isCardConnected
    }

    private fun dispatchIntent(intent: Intent?) {
        intent?.let {
            val tag = it.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            if (tag != null) {
                mCardReader.dispatchTag(tag)
            } else {
                Logger.get().println("dispatchIntent: tag is null")
            }
        }
    }

    override fun onCreate(intent: Intent) {
        dispatchIntent(intent)
    }

    override fun onResume() {
        if (mCardReader.checkNfc()) {
            mCardReader.enableCardReader()
        }
    }

    override fun onPause() {
        mCardReader.disableCardReader()
    }

    override fun onDestroy() {
        mCardReader.stopCheckThread()
    }

    override fun onNewIntent(intent: Intent) {
        dispatchIntent(intent)
    }

    @Throws(IOException::class)
    override fun sendData(data: ByteArray): String {
        mCardReader.let {
            return Util.byteArrayToHexString(it.transceive(data))
        }
    }

    @Throws(IOException::class)
    override fun tranceive(data: ByteArray): ByteArray {
        mCardReader.let {
            return it.transceive(data)
        }
    }

}