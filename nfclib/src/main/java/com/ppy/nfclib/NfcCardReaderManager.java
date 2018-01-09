package com.ppy.nfclib;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;

import java.io.IOException;

/**
 * Created by ZP on 2017/9/20.
 * NFC管理类，外层调用只需要与本类交互
 */

public class NfcCardReaderManager implements INfcCardReader{

    private Activity mActivity;
    private CardReader mCardReader;
    private boolean enableSound = true;
    private int mDelay;
    private CardOperatorListener mCardOperatorListener;

    private CardReaderHandler mHandler = new CardReaderHandler() {
        @Override
        public void onNfcNotExit() {
            if (mCardOperatorListener != null) {
                mCardOperatorListener.onException(ExceptionConstant.NFC_NOT_EXIT,
                        ExceptionConstant.mNFCException.get(ExceptionConstant.NFC_NOT_EXIT));
            }
        }

        @Override
        public void onNfcNotEnable() {
            if (mCardOperatorListener != null) {
                mCardOperatorListener.onException(ExceptionConstant.NFC_NOT_ENABLE,
                        ExceptionConstant.mNFCException.get(ExceptionConstant.NFC_NOT_ENABLE));
            }
        }

        @Override
        public void onCardConnected(boolean isConnected) {
            if (mCardOperatorListener != null) {
                mCardOperatorListener.onCardConnected(isConnected);
            }
        }
    };


    private NfcCardReaderManager(Builder builder) {
        mActivity = builder.mActivity;
        mCardReader = builder.mCardReader;
        if (builder.mCardReader == null) {
            mCardReader = CardReaderFactory.productCardReader(mActivity);
        }
        enableSound = builder.enableSound;
        mCardReader.enablePlatformSound(enableSound);
        mCardReader.setReaderPresenceCheckDelay(builder.mDelay);
        mCardReader.setOnCardReaderHandler(mHandler);
    }


    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onResume() {
        mCardReader.enableCardReader();
    }

    @Override
    public void onPause() {
        mCardReader.disableCardReader();
    }

    @Override
    public void onDestroy() {
        if (mCardReader != null) {
            mCardReader = null;
            mActivity = null;
        }
    }
    @Override
    public void onNewIntent(Intent intent) {
        if (intent != null) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            mCardReader.dispatchTag(tag);
        }
    }

    @Override
    public String sendData(byte[] data) throws IOException {
        return Util.ByteArrayToHexString(mCardReader.transceive(data));
    }

    @Override
    public String sendData(String hexData) throws IOException {
        return sendData(Util.HexStringToByteArray(hexData));
    }

    @Override
    public byte[] tranceive(byte[] data) throws IOException {
        return mCardReader.transceive(data);
    }

    @Override
    public byte[] tranceive(String hexData) throws IOException {
        return tranceive(Util.HexStringToByteArray(hexData));
    }

    public void setOnCardOperatorListener(CardOperatorListener listener) {
        mCardOperatorListener = listener;
    }

    @Override
    public boolean isCardConnected() {
        return mCardReader.isCardConnected();
    }

    public static final class Builder {
        private Activity mActivity;
        private CardReader mCardReader;
        private boolean enableSound;
        private int mDelay;

        public Builder(Activity activity) {
            mActivity = activity;
        }

        public Builder(NfcCardReaderManager copy) {
            this.mActivity = copy.mActivity;
            this.mCardReader = copy.mCardReader;
            this.enableSound = copy.enableSound;
            this.mDelay = copy.mDelay;
        }

        public Builder mCardReader(CardReader val) {
            mCardReader = val;
            return this;
        }

        public Builder enableSound(boolean val) {
            enableSound = val;
            return this;
        }

        public Builder setReaderPresenceCheckDelay(int delay) {
            if (delay < 0) {
                delay = 0;
            }
            mDelay = delay;
            return this;
        }

        public NfcCardReaderManager build() {
            return new NfcCardReaderManager(this);
        }
    }
}
