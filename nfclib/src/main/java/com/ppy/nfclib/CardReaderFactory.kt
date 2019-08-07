package com.ppy.nfclib

import android.app.Activity
import android.os.Build

/**
 * 读卡器模式工厂类，根据系统版本生产相应CardReader.
 * Created by ZP on 2017/9/20.
 */

object CardReaderFactory {

    fun productCardReader(activity: Activity): CardReader {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            KikKatCardReader(activity)
        } else {
            JellyBeanCardReader(activity)
        }
    }
}
