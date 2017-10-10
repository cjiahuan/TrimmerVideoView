package com.cjh.videotrimmerlibrary.controls

import android.annotation.SuppressLint
import android.text.TextUtils
import android.widget.VideoView
import com.cjh.videotrimmerlibrary.MediaHandleManager

/**
 * Created by cjh on 2017/8/30.
 */
class VideoViewControl private constructor(videoView: VideoView) {

    private var mVideoView: VideoView = videoView

    companion object {
        @SuppressLint("StaticFieldLeak")
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


    fun initial() {
        if (TextUtils.isEmpty(MediaHandleManager.getInstance().getConfigVo().videoPath)) throw IllegalArgumentException("VideoViewControl getInstance ::: videoPath cannot be null or empty ")
        mVideoView.setVideoPath(MediaHandleManager.getInstance().getConfigVo().videoPath)
        mVideoView.requestFocus()
        mVideoView.start()
        mVideoView.seekTo(1)
        mVideoView.pause()
    }

    fun updatePos(pos: Long) {
        mVideoView.seekTo(pos.toInt())
    }

    fun release() {
        mInstance = null
        mVideoView.seekTo(0)
        mVideoView.pause()
    }
}