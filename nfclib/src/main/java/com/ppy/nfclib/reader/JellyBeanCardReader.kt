package com.ppy.nfclib.reader

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.tech.IsoDep
import android.nfc.tech.NfcA
import android.nfc.tech.NfcF
import com.ppy.nfclib.CardReaderInnerCallback

/**
 * API 小于 19 NFC读卡器模式.
 * Created by ZP on 2017/9/20.
 */
class JellyBeanCardReader(
    override val activity: Activity,
    override val mCardReaderInnerCallback: CardReaderInnerCallback?
) : BaseCardReader(activity, mCardReaderInnerCallback) {

    private val mPendingIntent: PendingIntent by lazy {
        PendingIntent.getActivity(
            activity,
            0,
            Intent(activity, activity.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0
        )
    }


    override fun enableCardReader() {
        val techListsArray = arrayOf(arrayOf(NfcF::class.java.name), arrayOf(IsoDep::class.java.name), arrayOf(NfcA::class.java.name))
        mDefaultNfcAdapter?.enableForegroundDispatch(activity, mPendingIntent, null, techListsArray)
    }

    override fun disableCardReader() {
        super.disableCardReader()
        mDefaultNfcAdapter?.disableForegroundDispatch(activity)
    }
}
