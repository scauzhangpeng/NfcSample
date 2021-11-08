package com.ppy.nfclib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.os.Build
import com.ppy.nfclib.util.Logger

/**
 * NFC开启、关闭系统广播.
 * Created by ZP on 2019/3/18.
 */
open class NfcStatusChangeBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_ADAPTER_STATE_CHANGED == action) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                val state =
                    intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE, NfcAdapter.STATE_OFF)

                Logger.get().println("nfc state broadcast receiver, state is $state")
                when (state) {
                    NfcAdapter.STATE_OFF -> onNfcOff()
                    NfcAdapter.STATE_ON -> onNfcOn()
                    NfcAdapter.STATE_TURNING_OFF -> onNfcTurningOff()
                    NfcAdapter.STATE_TURNING_ON -> onNfcTurningOn()
                    5 -> onCardPayMode()//samsumg return 5 that minds card pay mode
                    else -> onNfcOff()
                }
            }
        }
    }

    protected open fun onNfcTurningOn() {

    }

    protected open fun onNfcTurningOff() {

    }

    protected open fun onNfcOn() {

    }

    protected open fun onNfcOff() {

    }

    protected open fun onCardPayMode() {

    }

    companion object {

        val nfcBroadcastReceiverIntentFilter: IntentFilter
            get() = IntentFilter("android.nfc.action.ADAPTER_STATE_CHANGED")
    }
}
