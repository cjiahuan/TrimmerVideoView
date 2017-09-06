package com.cjh.videotrimmerlibrary.vo

import com.cjh.videotrimmerlibrary.Config

/**
 * Created by cjh on 2017/8/31.
 */
class ConfigVo {
    var videoPath: String? = null
    var width: Int = 0
    var height: Int = 0
    var durationL: Long = 0L
    var trimmerTimeL: Long = Config.DEFAULT_TRIMMER_TIME
    var showThumbCount = Config.DEFAULT_THUMB_SHOW_COUNT
    var adapterUpdateCount = Config.DEFAULT_ADAPTER_UPDATE_COUNT
    var thumbItemWidth = 0
    var thumbItemHeight = 0
}