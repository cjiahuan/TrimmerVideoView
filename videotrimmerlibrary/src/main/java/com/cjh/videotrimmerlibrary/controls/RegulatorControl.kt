package com.cjh.videotrimmerlibrary.controls

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.VideoView
import com.cjh.videotrimmerlibrary.DefaultConfig
import com.cjh.videotrimmerlibrary.MediaHandleManager
import com.cjh.videotrimmerlibrary.TrimmerSeekBar
import com.cjh.videotrimmerlibrary.callback.EndTouchActionListener
import com.cjh.videotrimmerlibrary.callback.EndScrollActionListener
import com.cjh.videotrimmerlibrary.callback.IConfig

/**
 * Created by cjh on 2017/8/31.
 */
class RegulatorControl private constructor() : EndScrollActionListener, EndTouchActionListener {

    override fun updateRegionIndex() {
        updateVideoViewThumb()
    }

    override fun updateByScroll() {
        updateVideoViewThumb()
    }

    private fun updateVideoViewThumb() {
        VideoViewControl.getInstance().updatePos(getThumbPos(TrimmerSeekBarControl.getInstance().leftIndex))
    }

    private fun getThumbPos(regionIndex: Int): Long {
        val realIndex = RecyclerViewControl.getInstance().firstItemPosition + regionIndex
        val thumbVo = RecyclerViewControl.getInstance().mThumbAdapter.mDatas[realIndex]
        return thumbVo.positionL / 1000
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

    fun setVideoPath(videoPath: String): RegulatorControl {
        MediaHandleManager.getInstance().setVideoPath(videoPath)
        return this
    }

    fun initialThumbItemWH(wh: Array<Int>): RegulatorControl {
        MediaHandleManager.getInstance().setThumbItemWH(wh)
        return this
    }

    fun handle() {
        VideoViewControl.getInstance().initial()
        MediaHandleManager.getInstance().getFrameThumb(RecyclerViewControl.getInstance())
    }


    fun release() {
        mInstance = null
        MediaHandleManager.getInstance().release()
    }

    fun setIConfig(icg: IConfig) {
        MediaHandleManager.getInstance().setIConfig(icg)
    }

    fun getTrimmerPos(): LongArray = longArrayOf(getThumbPos(TrimmerSeekBarControl.getInstance().leftIndex), getThumbPos(TrimmerSeekBarControl.getInstance().rightIndex))
}