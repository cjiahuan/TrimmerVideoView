package com.cjh.videotrimmerlibrary.controls

import android.support.v7.widget.RecyclerView
import android.widget.VideoView

/**
 * Created by cjh on 2017/8/31.
 */
class RegulatorControl private constructor() {

    companion object {
        private var mInstance: RegulatorControl? = null
        fun getInstance(videoView: VideoView, recyclerView: RecyclerView): RegulatorControl {
            if (mInstance == null) {
                synchronized(VideoViewControl::class) {
                    if (mInstance == null) {
                        VideoViewControl.getInstance(videoView)
                        RecyclerViewControl.getInstance(recyclerView)
                        mInstance = RegulatorControl()
                    }
                }
            }
            return mInstance!!
        }

        fun getInstance(): RegulatorControl {
            if (mInstance == null) {
                throw IllegalArgumentException("RegulatorControl getInstance ::: must call method getInstance(videoView: VideoView, recyclerView: RecyclerView) first !!!")
            }
            return mInstance!!
        }
    }

    fun setTrimmerTime(trimmerTime: Long) {

    }

    fun setVideoPath(videoPath: String) {
        MediaMetadataRetrieverAgent.getInstance().setVideoPath(videoPath)
    }

    fun setThumbShowCount(thumbShowCount: Int) {
        MediaMetadataRetrieverAgent.getInstance().setThumbCount(thumbShowCount)
    }

    fun setAdapterUpdateCount(adapterUpdateCount: Int) {
        MediaMetadataRetrieverAgent.getInstance().setAdapterUpdateCount(adapterUpdateCount)
    }

    fun initialVideoViewWH(wh: Array<Int>): RegulatorControl {
        VideoViewControl.getInstance().resetVideoViewWH(wh)
        return this
    }

    fun initialThumbItemWH(wh: Array<Int>): RegulatorControl {
        MediaMetadataRetrieverAgent.getInstance().setThumbItemWH(wh)
        return this
    }

    fun handle() {
        VideoViewControl.getInstance().start()
        MediaMetadataRetrieverAgent.getInstance().build()
        MediaMetadataRetrieverAgent.getInstance().getFrameThumb(RecyclerViewControl.getInstance())
    }

    fun release() {
        mInstance = null
        MediaMetadataRetrieverAgent.getInstance().release()
    }

}