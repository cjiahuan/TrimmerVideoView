package com.trimmerdemo

import com.cjh.videotrimmerlibrary.callback.IConfig

/**
 * Created by cjh on 2017/9/28.
 */
class MyConfig : IConfig {
    override fun getMinTrimmerTime(): Long = 1000L

    override fun getTrimmerOffsetValue(): Int = 5

    override fun getTrimmerTime(): Long = 20 * 1000L

    override fun getVisiableThumbCount(): Int = 10

    override fun getThumbListUpdateCount(): Int = 5

    override fun getTrimmerSeekBarShaowColor(): String = "#99000000"

    override fun getTrimmerSeekBarTrimmerStrokeColor(): String = "#ff0000"

    override fun getTrimmerSeekBarTrimmerStrokeWidth(): Int = 5
}