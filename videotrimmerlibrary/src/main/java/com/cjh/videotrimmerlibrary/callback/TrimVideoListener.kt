package com.cjh.videotrimmerlibrary.callback

import android.net.Uri

/**
 * Created by cjh on 2018/2/7.
 */
interface TrimVideoListener {

    fun onStartTrim(start: String, start1: Long, trimmers: String, trimmerl: Long)

    fun onProgress(progress: Int)

    fun onFinishTrim(uri: Uri)

    fun onCancel()

}