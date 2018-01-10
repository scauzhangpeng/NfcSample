package com.ppy.nfclib;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcF;

/**
 * Created by ZP on 2017/9/20.
 * API 小于 19 NFC读卡器模式
 */

public class JellyBeanCardReader extends CardReader {


    private PendingIntent mPendingIntent;

    public JellyBeanCardReader(Activity activity) {
        super(activity);
        mPendingIntent = PendingIntent.getActivity(
                mActivity, 0, new Intent(mActivity, mActivity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void enableCardReader() {
        super.enableCardReader();
        String[][] techListsArray = new String[][] { new String[] { NfcF.class.getName() },
                new String[] {IsoDep.class.getName()} };
        if (mDefaultAdapter != null) {
            mDefaultAdapter.enableForegroundDispatch(mActivity, mPendingIntent, null, techListsArray);
        }

    }

    @Override
    protected void disableCardReader() {
        super.disableCardReader();
        if (mDefaultAdapter != null) {
            mDefaultAdapter.disableForegroundDispatch(mActivity);
        }
    }

    @Override
    protected void dispatchTag(Tag tag) {
        super.dispatchTag(tag);
    }
}
