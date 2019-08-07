package com.ppy.nfcsample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.ppy.nfclib.CardOperatorListener
import com.ppy.nfclib.NfcCardReaderManager
import com.ppy.nfclib.NfcStatusChangeBroadcastReceiver

/**
 * Created by ZP on 2019/3/19.
 */
abstract class NfcActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName

    protected lateinit var mReaderManager: NfcCardReaderManager

    private val mCardOperatorListener = object : CardOperatorListener {
        override fun onCardConnected(isConnected: Boolean) {
            Log.d(TAG, "onCardConnected: isConnected = $isConnected")
            doOnCardConnected(isConnected)
        }

        override fun onException(code: Int, message: String) {
            Log.d(TAG, "onException: code = $code,message = $message")
            doOnException(code, message)
        }
    }

    private val mNfcStatusChangeBroadcastReceiver = object : NfcStatusChangeBroadcastReceiver() {
        override fun onNfcOn() {
            super.onNfcOn()
            doOnNfcOn()
        }

        override fun onNfcOff() {
            super.onNfcOff()
            doOnNfcOff()
        }
    }


    private fun initNfcCardReader() {
        mReaderManager = NfcCardReaderManager.Builder(this)
                .enableSound(false)
                //.setReaderPresenceCheckDelay(30000)
                .build()
        mReaderManager.setOnCardOperatorListener(mCardOperatorListener)
    }

    abstract fun doOnCardConnected(isConnected: Boolean)

    abstract fun doOnException(code: Int, message: String)

    abstract fun doOnNfcOn()

    abstract fun doOnNfcOff()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        registerReceiver(mNfcStatusChangeBroadcastReceiver, NfcStatusChangeBroadcastReceiver.nfcBroadcastReceiverIntentFilter)
        initNfcCardReader()
        mReaderManager.onCreate(intent)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
        mReaderManager.onStart()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        mReaderManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
        mReaderManager.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        unregisterReceiver(mNfcStatusChangeBroadcastReceiver)
        mReaderManager.onDestroy()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent: " + intent.action!!)
        mReaderManager.onNewIntent(intent)
    }

}
