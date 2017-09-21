package com.ppy.nfclib;

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
    protected IsoDep mIsoDep;
    protected boolean isCardConnected;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private CardOperatorListener mCardOperatorListener;

    public CardReader() {

    }

    protected void enableCardReader() {
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
        if (mCardOperatorListener == null) {
            return;
        }
        isCardConnected = isConnected;
        if (isConnected) {
            mCardOperatorListener.onCardConnected(true);
            checkConnected();
        } else {
            mIsoDep = null;
            mCardOperatorListener.onCardConnected(false);
        }
    }

    public boolean isCardConnected() {
        return isCardConnected && mIsoDep != null && mIsoDep.isConnected();
    }

    public void setCardConnected(boolean cardConnected) {
        isCardConnected = cardConnected;
    }

    protected void enablePlatformSound(boolean enableSound) {

    }

    protected byte[] transceive(byte[] data) throws IOException {
        if (mIsoDep == null) {
            throw new TagLostException("Iso is null");
        }
        return mIsoDep.transceive(data);
    }

    public void setOnCardOperatorListener(CardOperatorListener listener) {
        mCardOperatorListener = listener;
        startCheckThread();
    }

    private void startCheckThread() {
        mHandlerThread = new HandlerThread("checkConnectThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }
}
