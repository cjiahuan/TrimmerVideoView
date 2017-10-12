package com.trimmerdemo

import com.cjh.videotrimmerlibrary.callback.IConfig

/**
 * Created by cjh on 2017/9/28.
 */
class CustomConfig : IConfig {
    override fun isShowTrimmerTextViews(): Boolean = false

    override fun getMinTrimmerThumbCount(): Int = 3

    override fun getTrimmerOffsetValue(): Int = 5

    override fun getTrimmerTime(): Long = 20 * 1000L

    override fun getVisiableThumbCount(): Int = 10

    override fun getThumbListUpdateCount(): Int = 5

    override fun getTrimmerSeekBarShaowColor(): String = "#99ff0000"

    override fun getTrimmerSeekBarTrimmerStrokeColor(): String = "#ff0000"

    override fun getTrimmerSeekBarTrimmerStrokeWidth(): Int = 5
}