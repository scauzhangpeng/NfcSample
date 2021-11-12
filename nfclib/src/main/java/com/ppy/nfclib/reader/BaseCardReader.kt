package com.ppy.nfclib.reader

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.TagLostException
import android.nfc.tech.IsoDep
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import com.ppy.nfclib.CardReaderInnerCallback
import com.ppy.nfclib.exception.ConnectTagException
import com.ppy.nfclib.util.Logger
import java.io.IOException
import java.util.*

abstract class BaseCardReader(override val activity: Activity, override val mCardReaderInnerCallback: CardReaderInnerCallback?) :
    NfcCardReader {

    internal var isCardConnected: Boolean = false
        get() = field && mIsoDep?.isConnected ?: false

    val mDefaultNfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(activity)
    }

    private val mHandlerThread: HandlerThread by lazy {
        HandlerThread("checkConnectThread")
    }

    private val mHandler: Handler by lazy {
        Handler(mHandlerThread.looper)
    }

    init {
        startCheckThread()
    }

    private var mIsoDep: IsoDep? = null

    protected var mEnableInitDelay: Long = 150

    override fun setEnableCardReaderDelay(delay: Long) {
        mEnableInitDelay = delay
    }

    @Throws(IOException::class)
    override fun transceive(data: ByteArray): ByteArray {
        return mIsoDep?.transceive(data) ?: throw TagLostException("IsoDep is null")
    }

    /**
     * 对Nfc发现的[android.nfc.Tag]进行分发.
     */
    @Synchronized
    open fun dispatchTag(tag: Tag) {
        Logger.get().println("dispatchTag: " + Arrays.toString(tag.techList))
        if (Arrays.toString(tag.techList).contains(IsoDep::class.java.name)) {
            connectCard(tag)
        }
    }

    /**
     * 根据[android.nfc.Tag]打开并且连接[IsoDep].
     */
    @Synchronized
    private fun connectCard(tag: Tag) {
        mIsoDep?.let {
            Logger.get().println("connectCard(): card connecting")
            return
        }

        mIsoDep = IsoDep.get(tag)

        mIsoDep?.let {
            try {
                it.connect()
                doOnCardConnected(true)
            } catch (e: IOException) {
                Logger.get().println("connectCard exception = " + e.message, e)
                mIsoDep = null
                mCardReaderInnerCallback?.onException(ConnectTagException("BasicTagTechnology connect fail"))
            }
        } ?: mCardReaderInnerCallback?.onException(ConnectTagException("IsoDep is null"))

    }

    /**
     * 回调卡片连接状态.
     * @param isConnected true 卡片连接 <br> false 卡片丢失
     */
    private fun doOnCardConnected(isConnected: Boolean) {
        isCardConnected = isConnected
        if (isConnected) {
            mCardReaderInnerCallback?.onCardConnected(true)
            checkConnected()
        } else {
            mIsoDep = null
            mCardReaderInnerCallback?.onCardConnected(false)
        }
    }

    /**
     * 延迟500毫秒寻循环检测卡片是否连接.
     */
    private fun checkConnected() {
        mHandler.postDelayed({
            mIsoDep?.let {
                if (it.isConnected) {
                    checkConnected()
                } else {
                    doOnCardConnected(false)
                }
            } ?: doOnCardConnected(false)
        }, 500)
    }

    private fun startCheckThread() {
        mHandlerThread.start()
    }

    fun stopCheckThread() {
        mHandler.removeCallbacksAndMessages(null)
        mHandlerThread.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                it.quitSafely()
            } else {
                it.quit()
            }
        }
    }
}