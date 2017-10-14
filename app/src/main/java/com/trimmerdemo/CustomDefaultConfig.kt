package com.trimmerdemo

import com.cjh.videotrimmerlibrary.DefaultConfig

/**
 * Created by cjh on 2017/10/11.
 */
class CustomDefaultConfig : DefaultConfig() {

    override fun getVisiableThumbCount(): Int = 30

    override fun getTrimmerTime(): Long = 20*1000

    override fun getThumbListUpdateCount(): Int = 5

}