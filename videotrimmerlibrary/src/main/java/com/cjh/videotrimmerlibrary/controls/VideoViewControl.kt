package com.cjh.videotrimmerlibrary.controls

import android.text.TextUtils
import android.util.Log
import android.widget.VideoView

/**
 * Created by cjh on 2017/8/30.
 */
class VideoViewControl private constructor(videoView: VideoView) {

    val mVideoView = videoView

    companion object {
        private var mInstance: VideoViewControl? = null
        fun getInstance(videoView: VideoView): VideoViewControl {
            if (mInstance == null) {
                synchronized(VideoViewControl::class) {
                    if (mInstance == null) {
                        mInstance = VideoViewControl(videoView)
                    }
                }
            }
            return mInstance!!
        }

        fun getInstance(): VideoViewControl {
            if (mInstance == null) {
                throw IllegalArgumentException("VideoViewControl getInstance ::: must call method getInstance(videoView: VideoView) first !!!")
            }
            return mInstance!!
        }
    }


    fun start() {
        if (TextUtils.isEmpty(MediaMetadataRetrieverAgent.getInstance().getConfigVo().videoPath)) throw IllegalArgumentException("VideoViewControl getInstance ::: videoPath cannot be null or empty ")
        mVideoView.setVideoPath(MediaMetadataRetrieverAgent.getInstance().getConfigVo().videoPath)
        mVideoView.requestFocus()
        mVideoView.start()
    }
}