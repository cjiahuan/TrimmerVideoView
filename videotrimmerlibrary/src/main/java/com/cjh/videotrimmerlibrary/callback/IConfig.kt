package com.cjh.videotrimmerlibrary.callback

/**
* Created by cjh on 2017/9/28.
*/
interface IConfig {

    fun getTrimmerTime(): Long

    fun getVisiableThumbCount(): Int

    fun getThumbListUpdateCount(): Int

    fun getTrimmerSeekBarShaowColor(): String

    fun getTrimmerSeekBarTrimmerStrokeColor(): String

    fun getTrimmerSeekBarTrimmerStrokeWidth(): Int

    fun getTrimmerOffsetValue(): Int

    fun getMinTrimmerThumbCount(): Int

    fun isShowTrimmerTextViews(): Boolean
}