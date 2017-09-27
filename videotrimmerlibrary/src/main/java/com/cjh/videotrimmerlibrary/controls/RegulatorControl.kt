package com.cjh.videotrimmerlibrary.controls

import android.support.v7.widget.RecyclerView
import android.widget.VideoView
import com.cjh.videotrimmerlibrary.TrimmerSeekBar
import com.cjh.videotrimmerlibrary.callback.EndTouchActionListener
import com.cjh.videotrimmerlibrary.callback.EndScrollActionListener

/**
 * Created by cjh on 2017/8/31.
 */
class RegulatorControl private constructor() : EndScrollActionListener, EndTouchActionListener {

    override fun updateRegionIndex() {

    }

    override fun updateByScroll() {
        
    }

    companion object {
        private var mInstance: RegulatorControl? = null
        fun getInstance(videoView: VideoView, recyclerView: RecyclerView, trimmerSeekBar: TrimmerSeekBar): RegulatorControl {
            if (mInstance == null) {
                synchronized(VideoViewControl::class) {
                    if (mInstance == null) {
                        mInstance = RegulatorControl()
                        VideoViewControl.getInstance(videoView)
                        RecyclerViewControl.getInstance(recyclerView, mInstance!!)
                        TrimmerSeekBarControl.getInstance(trimmerSeekBar, mInstance!!)
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