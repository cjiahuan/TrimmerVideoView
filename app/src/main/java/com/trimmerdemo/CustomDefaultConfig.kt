package com.trimmerdemo

import com.cjh.videotrimmerlibrary.DefaultConfig

/**
 * Created by cjh on 2017/10/11.
 */
class CustomDefaultConfig : DefaultConfig() {

    override fun getMinTrimmerThumbCount(): Int = 3

}