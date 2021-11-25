package com.ppy.nfclib.reader

import android.annotation.TargetApi
import android.app.Activity
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import com.ppy.nfclib.CardReaderInnerCallback
import com.ppy.nfclib.util.Logger

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
        super.enableCardReader()
        val initDelay = extra.getLong(ENABLE_INIT_DELAY, DEFAULT_INIT_DELAY)
        try {
            Logger.get().println("delay $initDelay ms to fix \"NfcService error: setReaderMode: Caller is not in foreground and is not system process.\"")
            Thread.sleep(initDelay)
        } catch (e: Exception) {
            Logger.get().println(e.message ?: "delay $initDelay ms failure", e)
        }
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

    override fun setEnableCardReaderDelay(delay: Long) {
        extra.putLong(ENABLE_INIT_DELAY, if (delay < 0) DEFAULT_INIT_DELAY else delay)
    }

    override fun setReaderPresenceCheckDelay(delay: Int) {
        extra.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, delay)
    }

    companion object {

        private const val DEFAULT_INIT_DELAY = 150L

        private const val ENABLE_INIT_DELAY = "enable_init_delay"

        private const val PLATFORM_SOUND = NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS

        private var READER_FLAG = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
    }
}
