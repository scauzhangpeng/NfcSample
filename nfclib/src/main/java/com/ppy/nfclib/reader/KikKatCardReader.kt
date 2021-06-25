package com.ppy.nfclib.reader

import android.annotation.TargetApi
import android.app.Activity
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import com.ppy.nfclib.CardReaderInnerCallback

/**
 * API 大于等于 19 NFC读卡器模式.
 * Created by ZP on 2017/9/20.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
class KikKatCardReader(
    override val activity: Activity,
    override val mCardReaderInnerCallback: CardReaderInnerCallback?
) : BaseCardReader(activity, mCardReaderInnerCallback) {

    private val mReaderCallback by lazy {
        NfcAdapter.ReaderCallback { tag -> dispatchTag(tag) }
    }

    private val extra: Bundle by lazy {
        Bundle()
    }

    override fun enableCardReader() {
        val delay = extra.getInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, -1)
        if (delay > 0) {
            mDefaultNfcAdapter?.enableReaderMode(activity, mReaderCallback, READER_FLAG, extra)
        } else {
            mDefaultNfcAdapter?.enableReaderMode(activity, mReaderCallback, READER_FLAG, null)
        }
    }

    override fun disableCardReader() {
        super.disableCardReader()
        mDefaultNfcAdapter?.disableReaderMode(activity)
    }

    override fun enablePlatformSound(enableSound: Boolean) {
        if (!enableSound) {
            READER_FLAG = READER_FLAG or PLATFORM_SOUND
        }
    }

    override fun setReaderPresenceCheckDelay(delay: Int) {
        extra.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, delay)
    }

    companion object {

        private const val PLATFORM_SOUND = NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS

        private var READER_FLAG = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
    }
}
