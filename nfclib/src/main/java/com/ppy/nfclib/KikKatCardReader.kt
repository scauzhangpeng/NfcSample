package com.ppy.nfclib

import android.annotation.TargetApi
import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle

/**
 * API 大于等于 19 NFC读卡器模式.
 * Created by ZP on 2017/9/20.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
class KikKatCardReader(activity: Activity) : CardReader(activity) {

    private val mReaderCallback = NfcAdapter.ReaderCallback { tag -> dispatchTag(tag) }

    private var extra: Bundle? = null

    override fun enableCardReader() {
        super.enableCardReader()
        if (extra != null && extra!!.getInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY) > 0) {
            mDefaultAdapter?.enableReaderMode(mActivity, mReaderCallback, READER_FLAG, extra)
        } else {
            mDefaultAdapter?.enableReaderMode(mActivity, mReaderCallback, READER_FLAG, null)
        }
    }

    override fun disableCardReader() {
        super.disableCardReader()
        mDefaultAdapter?.disableReaderMode(mActivity)
    }

    override fun dispatchTag(tag: Tag) {
        super.dispatchTag(tag)
    }

    override fun enablePlatformSound(enableSound: Boolean) {
        if (!enableSound) {
            READER_FLAG = READER_FLAG or PLATFORM_SOUND
        }
    }

    override fun setReaderPresenceCheckDelay(delay: Int) {
        if (extra == null) {
            extra = Bundle()
        }
        extra?.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, delay)
    }

    companion object {

        private const val PLATFORM_SOUND = NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS

        private var READER_FLAG = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
    }
}
