package com.ppy.nfclib.reader

import android.annotation.TargetApi
import android.app.Activity
import android.nfc.Tag
import android.os.Build
import com.ppy.nfclib.CardReaderInnerCallback
import com.ppy.nfclib.exception.ExceptionConstant
import com.ppy.nfclib.util.Logger
import com.ppy.nfclib.util.MiUtil

@TargetApi(Build.VERSION_CODES.KITKAT)
class MIUICardReader(
    override val activity: Activity,
    override val mCardReaderInnerCallback: CardReaderInnerCallback?
) : JellyBeanCardReader(activity, mCardReaderInnerCallback) {


    override fun enableCardReader() {
        Logger.get().println("miui enableCardReader")
        checkPermission {
            enableCardReaderCore()
        }
    }

    fun enableCardReaderCore() {
        //限制为读卡器模式
//            super.enableCardReader()
        Logger.get().println("miui enableCardReaderCore")
        //设置前台调度，触发系统NFC使用弹窗
        super.enableCardReader()
    }

    override fun disableCardReader() {
        Logger.get().println("miui disableCardReader")
        checkPermission(false) {
            super.disableCardReader()
        }
    }

    override fun dispatchTag(tag: Tag) {
        Logger.get().println("miui dispatchTag = ${tag.toString()}")
        checkPermission {
            super.dispatchTag(tag)
        }
    }

    fun dispatchTagCore(tag: Tag) {
        Logger.get().println("miui dispatchTagCore = ${tag.toString()}")
        super.dispatchTag(tag)
    }


    private inline fun checkPermission(needCallback: Boolean = true, doNext: () -> Unit) {
        when (MiUtil.checkNfcPermission(activity)) {
            MiUtil.PermissionResult.PERMISSION_GRANTED -> {
                doNext()
            }
            MiUtil.PermissionResult.PERMISSION_DENIED -> {
                if (needCallback) {
                    mCardReaderInnerCallback?.onNfcPermission(ExceptionConstant.NFC_PERMISSION_NOT_GRANTED)
                }
            }
            MiUtil.PermissionResult.PERMISSION_ASK -> {
                if (needCallback) {
                    mCardReaderInnerCallback?.onNfcPermission(ExceptionConstant.NFC_PERMISSION_ASK)
                }
            }
            else -> {
                if (needCallback) {
                    mCardReaderInnerCallback?.onNfcPermission(ExceptionConstant.NFC_PERMISSION_UNKNOWN)
                }
                doNext()
            }
        }
    }
}