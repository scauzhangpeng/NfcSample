package com.ppy.nfclib

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.TagLostException
import android.nfc.tech.IsoDep
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import java.io.IOException
import java.util.*

/**
 * NFC读卡器模式，基类.
 * Created by ZP on 2017/9/20.
 */

open class CardReader(activity: Activity) {

    private val TAG = this.javaClass.simpleName
    protected var mDefaultAdapter: NfcAdapter? = null
    protected var mActivity: Activity? = activity
    protected var mIsoDep: IsoDep? = null
    internal var isCardConnected: Boolean = false
        get() = field && mIsoDep?.isConnected ?: false
    private var mHandler: Handler? = null
    private var mHandlerThread: HandlerThread? = null
    private var mCardReaderHandler: CardReaderHandler? = null

    init {
        mDefaultAdapter = NfcAdapter.getDefaultAdapter(activity)
    }

    open fun enableCardReader() {
        Logger.get().println(TAG, "enableCardReader: ")
        if (mActivity == null) {
            throw RuntimeException("please init first...")
        }
        mActivity?.let { it ->
            if (!Util.isNfcExits(it)) {
                mCardReaderHandler?.let {
                    it.onNfcNotExit()
                    return
                }
            }

            if (!Util.isNfcEnable(it)) {
                mCardReaderHandler?.onNfcNotEnable()
            }
        }
    }

    open fun disableCardReader() {
        Logger.get().println(TAG, "disableCardReader: ")
    }

    @Synchronized
    open fun dispatchTag(tag: Tag) {
        Logger.get().println(TAG, "dispatchTag: " + Arrays.toString(tag.techList))
        if (Arrays.toString(tag.techList).contains(IsoDep::class.java.name)) {
            connectCard(tag)
        }
    }

    @Synchronized
    private fun connectCard(tag: Tag) {
        mIsoDep?.let {
            Logger.get().println(TAG, "connectCard(): card connecting")
            return
        }

        mIsoDep = IsoDep.get(tag)

        mIsoDep?.let {
            try {
                it.connect()
                doOnCardConnected(true)
            } catch (e: IOException) {
                Logger.get().println(TAG, "connectCard exception = " + e.message, e)
                e.printStackTrace()
                doOnCardConnected(false)
            }
        } ?: doOnCardConnected(false)
    }

    private fun checkConnected() {
        mHandler?.postDelayed({
            mIsoDep?.let {
                if (it.isConnected) {
                    checkConnected()
                } else {
                    doOnCardConnected(false)
                }
            } ?: doOnCardConnected(false)
        }, 500)
    }

    private fun doOnCardConnected(isConnected: Boolean) {
        if (mCardReaderHandler == null) {
            return
        }
        isCardConnected = isConnected
        if (isConnected) {
            mCardReaderHandler?.onCardConnected(true)
            checkConnected()
        } else {
            mIsoDep = null
            mCardReaderHandler?.onCardConnected(false)
        }
    }

    open fun enablePlatformSound(enableSound: Boolean) {

    }

    open fun setReaderPresenceCheckDelay(delay: Int) {

    }

    @Throws(IOException::class)
    fun transceive(data: ByteArray): ByteArray {
        return mIsoDep?.transceive(data) ?: throw TagLostException("IsoDep is null")
    }

    fun setOnCardReaderHandler(handler: CardReaderHandler) {
        mCardReaderHandler = handler
        startCheckThread()
    }

    private fun startCheckThread() {
        mHandlerThread = HandlerThread("checkConnectThread")
        mHandlerThread?.let {
            it.start()
            mHandler = Handler(it.looper)
        }
    }

    fun stopCheckThread() {
        mHandler?.let {
            it.removeCallbacksAndMessages(null)
            mHandler = null
        }
        mHandlerThread?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                it.quitSafely()
            } else {
                it.quit()
            }
            mHandlerThread = null
        }
    }
}
