package com.cjh.videotrimmerlibrary.vo

import com.cjh.videotrimmerlibrary.DefaultConfig
import com.cjh.videotrimmerlibrary.callback.IConfig

/**
 * Created by cjh on 2017/8/31.
 */
class ConfigVo {

    var iConfig: IConfig = DefaultConfig()

    var videoPath: String? = null
    var width: Int = 0
    var height: Int = 0
    var durationL: Long = 0L
    var trimmerTimeL: Long = iConfig.getTrimmerTime()
    var showThumbCount = iConfig.getVisiableThumbCount()
    var adapterUpdateCount = iConfig.getThumbListUpdateCount()
    var thumbItemWidth = 0
    var thumbItemHeight = 0
}