package com.ppy.nfcsample

import android.app.Application

import com.squareup.leakcanary.LeakCanary

/**
 * Created by ZP on 2019-07-10.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }
}
