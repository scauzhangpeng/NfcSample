package com.ppy.nfclib.reader

import android.app.Activity
import android.os.Build
import com.ppy.nfclib.CardReaderInnerCallback
import com.ppy.nfclib.util.Logger
import com.ppy.nfclib.util.Util
import java.io.IOException

interface NfcCardReader {

    val activity:Activity
    val mCardReaderInnerCallback: CardReaderInnerCallback?

    /**
     * 检测设备Nfc状态.
     * @return true 设备具备Nfc并且已打开读卡器模式 <br> false 设备不具备Nfc或者Nfc未打开
     */
    fun checkNfc(): Boolean {
        if (!Util.isNfcExits(activity)) {
            mCardReaderInnerCallback?.onNfcNotExit()
            return false
        }
        if (!Util.isNfcEnable(activity)) {
            mCardReaderInnerCallback?.onNfcNotEnable()
            return false
        }
        return true
    }

    /**
     * 启动Nfc读卡器模式.
     */
    fun enableCardReader() {
        Logger.get().println("enableCardReader")
    }

    /**
     * 关闭Nfc读卡器模式.
     */
    fun disableCardReader() {
        Logger.get().println("disableCardReader")
    }

    /**
     * 是否允许Nfc发现[android.nfc.Tag]时发生系统声音.
     * @param enableSound true 允许 <br> false 不允许
     */
    fun enablePlatformSound(enableSound: Boolean) {
    }

    /**
     * 暂停Nfc一段时间, 使其不检测新[android.nfc.Tag].
     * @param delay 延迟时间, 单位毫秒
     */
    fun setReaderPresenceCheckDelay(delay: Int) {
    }

    @Throws(IOException::class)
    fun transceive(data: ByteArray): ByteArray

    /**
     * Nfc读卡器模式工厂类.
     * API 19以后使用[KikKatCardReader] API 19以前使用[JellyBeanCardReader]
     */
    companion object Factory {
        operator fun invoke(activity: Activity, mCardReaderInnerCallback: CardReaderInnerCallback): BaseCardReader {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> KikKatCardReader(activity, mCardReaderInnerCallback)
                else -> JellyBeanCardReader(activity, mCardReaderInnerCallback)
            }
        }
    }
}