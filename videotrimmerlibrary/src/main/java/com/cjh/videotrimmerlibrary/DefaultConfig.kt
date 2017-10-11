package com.cjh.videotrimmerlibrary

import com.cjh.videotrimmerlibrary.callback.IConfig

/**
* Created by cjh on 2017/9/28.
*/
open class DefaultConfig : IConfig {
    override fun isShowTrimmerTextViews(): Boolean = true

    override fun getTrimmerTime(): Long = Constant.DEFAULT_TRIMMER_TIME

    override fun getVisiableThumbCount(): Int = Constant.DEFAULT_THUMB_SHOW_COUNT

    override fun getThumbListUpdateCount(): Int = Constant.DEFAULT_ADAPTER_UPDATE_COUNT

    override fun getTrimmerSeekBarShaowColor(): String = Constant.DEFAULT_SHAOW_COLOR

    override fun getTrimmerSeekBarTrimmerStrokeColor(): String = Constant.DEFAULT_TRIMMER_COLRO

    override fun getTrimmerSeekBarTrimmerStrokeWidth(): Int = Constant.DEFAULT_TRIMMER_STROKE_WIDTH

    override fun getTrimmerOffsetValue(): Int = Constant.DEFAULT_SEEKBAR_OFFSET_VALUE

    override fun getMinTrimmerThumbCount(): Int = Constant.DEFAULT_TRIMMER_MIN_THUMB_COUNT
}