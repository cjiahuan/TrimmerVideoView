package com.trimmerdemo

import android.app.Application
import com.squareup.leakcanary.LeakCanary

/**
 * Created by cjh on 2017/10/11.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
    }

}