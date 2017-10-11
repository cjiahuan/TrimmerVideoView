package com.cjh.videotrimmerlibrary.vo

import com.cjh.videotrimmerlibrary.DefaultConfig
import com.cjh.videotrimmerlibrary.callback.IConfig

/**
* Created by cjh on 2017/8/31.
*/
class ConfigVo {

    private var iConfig: IConfig = DefaultConfig()

    var videoPath: String? = null
    var width: Int = 0
    var height: Int = 0
    var durationL: Long = 0L

    var thumbItemWidth = 0
    var thumbItemHeight = 0

    var trimmerTimeL = iConfig.getTrimmerTime()
    var offsetValue = iConfig.getTrimmerOffsetValue()
    var visiableThumbCount = iConfig.getVisiableThumbCount()
    var adapterUpdateCount = iConfig.getThumbListUpdateCount()
    var seekBarShaowColor = iConfig.getTrimmerSeekBarShaowColor()
    var seekBarStrokeColor = iConfig.getTrimmerSeekBarTrimmerStrokeColor()
    var seekBarStrokeWidth = iConfig.getTrimmerSeekBarTrimmerStrokeWidth()

    fun updateIConfig(icg: IConfig) {
        trimmerTimeL = icg.getTrimmerTime()
        visiableThumbCount = icg.getVisiableThumbCount()
        adapterUpdateCount = icg.getThumbListUpdateCount()
        seekBarShaowColor = icg.getTrimmerSeekBarShaowColor()
        seekBarStrokeColor = icg.getTrimmerSeekBarTrimmerStrokeColor()
        seekBarStrokeWidth = icg.getTrimmerSeekBarTrimmerStrokeWidth()
    }

    fun getVisiableEndPos(): Long = Math.min(trimmerTimeL, durationL)
}