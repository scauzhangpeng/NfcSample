package com.ppy.nfclib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;

/**
 * Created by ZP on 2017/9/20.
 * API >= 19 NFC读卡器模式
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class KikKatCardReader extends CardReader {

    private Activity mActivity;
    private NfcAdapter.ReaderCallback mReaderCallback = new NfcAdapter.ReaderCallback() {
        @Override
        public void onTagDiscovered(Tag tag) {
            dispatchTag(tag);
        }
    };

    private static final int PLATFORM_SOUND = NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS;

    private static int READER_FLAG = NfcAdapter.FLAG_READER_NFC_A
            | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;

    public KikKatCardReader(Activity activity) {
        mActivity = activity;
        mDefaultAdapter = NfcAdapter.getDefaultAdapter(activity);
    }

    @Override
    protected void enableCardReader() {
        super.enableCardReader();
        mDefaultAdapter.enableReaderMode(mActivity, mReaderCallback, READER_FLAG, null);
    }

    @Override
    protected void disableCardReader() {
        super.disableCardReader();
        mDefaultAdapter.disableReaderMode(mActivity);
    }

    @Override
    protected void dispatchTag(Tag tag) {
        super.dispatchTag(tag);
    }

    @Override
    protected void enablePlatformSound(boolean enableSound) {
        if (!enableSound) {
            READER_FLAG = READER_FLAG | PLATFORM_SOUND;
        }
    }
}
