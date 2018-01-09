package com.ppy.nfclib;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.IsoDep;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by ZP on 2017/9/20.
 * NFC读卡器模式，基类
 */

public class CardReader {

    private String TAG = this.getClass().getSimpleName();
    protected NfcAdapter mDefaultAdapter;
    protected Activity mActivity;
    protected IsoDep mIsoDep;
    protected boolean isCardConnected;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private CardReaderHandler mCardReaderHandler;

    public CardReader() {

    }
    public CardReader(Activity activity) {
        mActivity = activity;
        mDefaultAdapter = NfcAdapter.getDefaultAdapter(activity);
    }

    protected void enableCardReader() {
        if (mActivity == null) {
            throw new RuntimeException("please init first...");
        }
        if (!Util.isNfcExits(mActivity)) {
            if (mCardReaderHandler != null) {
                mCardReaderHandler.onNfcNotExit();
                return;
            }
        }

        if (!Util.isNfcEnable(mActivity)) {
            if (mCardReaderHandler != null) {
                mCardReaderHandler.onNfcNotEnable();
            }
        }
        Log.d(TAG, "enableCardReader: ");
    }

    protected void disableCardReader() {
        Log.d(TAG, "disableCardReader: ");
    }

    protected synchronized void dispatchTag(Tag tag) {
        Log.d(TAG, "dispatchTag: ");
        System.out.println(Arrays.toString(tag.getTechList()));
        if (Arrays.toString(tag.getTechList()).contains(IsoDep.class.getName())) {
            connectCard(tag);
        }
    }

    private synchronized void connectCard(Tag tag) {
        if (mIsoDep != null) {
            Log.d(TAG, "connectCard: card connecting");
            return;
        }
        mIsoDep = IsoDep.get(tag);
        if (mIsoDep != null) {
            try {
                mIsoDep.connect();
                doOnCardConnected(true);
            } catch (IOException e) {
                e.printStackTrace();
                doOnCardConnected(false);
            }
        } else {
            doOnCardConnected(false);
        }
    }

    private void checkConnected() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIsoDep.isConnected()) {
                    checkConnected();
                } else {
                    doOnCardConnected(false);
                }
            }
        }, 500);
    }

    private void doOnCardConnected(boolean isConnected) {
        if (mCardReaderHandler == null) {
            return;
        }
        isCardConnected = isConnected;
        if (isConnected) {
            mCardReaderHandler.onCardConnected(true);
            checkConnected();
        } else {
            mIsoDep = null;
            mCardReaderHandler.onCardConnected(false);
        }
    }

    public boolean isCardConnected() {
        return isCardConnected && mIsoDep != null && mIsoDep.isConnected();
    }

    protected void enablePlatformSound(boolean enableSound) {

    }

    protected void setReaderPresenceCheckDelay(int delay) {

    }

    protected byte[] transceive(byte[] data) throws IOException {
        if (mIsoDep == null) {
            throw new TagLostException("Iso is null");
        }
        return mIsoDep.transceive(data);
    }

    public void setOnCardReaderHandler(CardReaderHandler handler) {
        mCardReaderHandler = handler;
        startCheckThread();
    }

    private void startCheckThread() {
        mHandlerThread = new HandlerThread("checkConnectThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }
}
