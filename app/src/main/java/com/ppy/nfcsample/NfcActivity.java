package com.ppy.nfcsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ppy.nfclib.CardOperatorListener;
import com.ppy.nfclib.NfcCardReaderManager;
import com.ppy.nfclib.NfcStatusChangeBroadcastReceiver;

/**
 * Created by ZP on 2019/3/19.
 */
public abstract class NfcActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    protected NfcCardReaderManager mReaderManager;

    private CardOperatorListener mCardOperatorListener = new CardOperatorListener() {
        @Override
        public void onCardConnected(boolean isConnected) {
            Log.d(TAG, "onCardConnected: isConnected = " + isConnected);
            doOnCardConnected(isConnected);
        }

        @Override
        public void onException(int code, String message) {
            Log.d(TAG, "onException: code = " + code + ",message = " + message);
            doOnException(code, message);
        }
    };

    private NfcStatusChangeBroadcastReceiver mNfcStatusChangeBroadcastReceiver = new NfcStatusChangeBroadcastReceiver() {
        @Override
        protected void onNfcOn() {
            super.onNfcOn();
            doOnNfcOn();
        }

        @Override
        protected void onNfcOff() {
            super.onNfcOff();
            doOnNfcOff();
        }
    };



    private void initNfcCardReader() {
        mReaderManager = new NfcCardReaderManager.Builder(this)
                .enableSound(false)
//                .setReaderPresenceCheckDelay(30000)
                .build();
        mReaderManager.setOnCardOperatorListener(mCardOperatorListener);
    }

    public abstract void doOnCardConnected(boolean isConnected);

    public abstract void doOnException(int code, String message);

    public abstract void doOnNfcOn();

    public abstract void doOnNfcOff();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        registerReceiver(mNfcStatusChangeBroadcastReceiver, NfcStatusChangeBroadcastReceiver.getNfcBroadcastReceiverIntentFilter());
        initNfcCardReader();
        mReaderManager.onCreate(getIntent());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        mReaderManager.onStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        mReaderManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        mReaderManager.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        unregisterReceiver(mNfcStatusChangeBroadcastReceiver);
        mReaderManager.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: " + intent.getAction());
        mReaderManager.onNewIntent(intent);
    }

}
