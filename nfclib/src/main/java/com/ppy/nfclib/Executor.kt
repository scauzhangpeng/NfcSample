package com.ppy.nfclib

import android.app.Activity
import com.ppy.nfclib.util.MiUtil
import com.ppy.nfclib.util.Util

interface Executor {
    fun actionNext()

    class DefaultNfcSetting(val activity: Activity): Executor {
        override fun actionNext() {
            Util.intentToNfcSetting(activity)
        }
    }

    class DefaultAppSetting(val activity: Activity): Executor {
        override fun actionNext() {
            MiUtil.intentToAppSetting(activity)
        }
    }
}