package com.ppy.nfclib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;

/**
 * Created by ZP on 2019/3/18.
 */
public class NfcStatusChangeBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_ADAPTER_STATE_CHANGED.equals(action)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                int status = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE, NfcAdapter.STATE_OFF);
                switch (status) {
                    case NfcAdapter.STATE_OFF:
                        doOnNfcOff();
                        break;
                    case NfcAdapter.STATE_ON:
                        doOnNfcOn();
                        break;
                    case NfcAdapter.STATE_TURNING_OFF:
                        doOnNfcTurningOff();
                        break;
                    case NfcAdapter.STATE_TURNING_ON:
                        doOnNfcTurningOn();
                        break;
                }
            }
        }
    }

    protected void doOnNfcTurningOn() {

    }

    protected void doOnNfcTurningOff() {

    }

    protected void doOnNfcOn() {

    }

    protected void doOnNfcOff() {

    }

    public static IntentFilter getNfcBroadcastReceiverIntentFilter() {
        return new IntentFilter("android.nfc.action.ADAPTER_STATE_CHANGED");
    }
}
