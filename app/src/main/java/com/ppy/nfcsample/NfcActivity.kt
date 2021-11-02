package com.ppy.nfcsample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ppy.nfclib.CardOperatorListener
import com.ppy.nfclib.NfcManagerCompat

/**
 * Created by ZP on 2019/3/19.
 */
abstract class NfcActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName

    protected lateinit var mReaderManager: NfcManagerCompat

    private val mCardOperatorListener = object : CardOperatorListener {
        override fun onCardConnected(isConnected: Boolean) {
            Log.d(TAG, "onCardConnected: isConnected = $isConnected")
            doOnCardConnected(isConnected)
        }

        override fun onException(code: Int, message: String) {
            Log.d(TAG, "onException: code = $code,message = $message")
            doOnException(code, message)
        }

        override fun onCardPay() {
            super.onCardPay()
            Log.d(TAG, "onCardPayMode: ")
            doOnNfcOff()
        }

        override fun onNfcEnable(stateOn: Boolean) {
            super.onNfcEnable(stateOn)
            Log.d(TAG, "onNfcEnable: $stateOn")
            if (!stateOn) {
                doOnNfcOff()
            } else {
                doOnNfcOn()
            }
        }

        override fun onNfcTurning(turningOn: Boolean) {
            super.onNfcTurning(turningOn)
            Log.d(TAG, "onNfcTurning: $turningOn")
        }
    }

    private fun initNfcCardReader() {
        mReaderManager = NfcManagerCompat(activity = this,
            cardOperatorListener = mCardOperatorListener,
            printer = LoggerImpl(),
            enableSound = true)
    }

    abstract fun doOnCardConnected(isConnected: Boolean)

    abstract fun doOnException(code: Int, message: String)

    abstract fun doOnNfcOn()

    abstract fun doOnNfcOff()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ${this.javaClass.simpleName}")
        initNfcCardReader()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent: " + intent.action!!)
        mReaderManager.onNewIntent(intent)
    }

}
