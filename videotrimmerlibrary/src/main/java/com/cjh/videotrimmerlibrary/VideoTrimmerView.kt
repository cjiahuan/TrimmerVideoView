package com.cjh.videotrimmerlibrary

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.cjh.videotrimmerlibrary.controls.RegulatorControl
import com.cjh.videotrimmerlibrary.utils.DensityUtils
import kotlinx.android.synthetic.main.video_trimmer_view.view.*

/**
 * Created by cjh on 2017/8/28.
 */
class VideoTrimmerView : FrameLayout {

    var mLayoutId = R.layout.video_trimmer_view

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(mLayoutId, this)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        RegulatorControl.getInstance(videoView, recyclerView, trimmerSeekBar)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        RegulatorControl.getInstance()
                .initialThumbItemWH(arrayOf(MeasureSpec.getSize(widthMeasureSpec) - DensityUtils.dip2px(context, 25f * 2), recyclerView.measuredHeight))
    }

    fun setTrimmerTime(trimmerTime: Long): VideoTrimmerView {
        RegulatorControl.getInstance().setTrimmerTime(trimmerTime)
        return this
    }

    fun setThumbShowCount(thumbShowCount: Int): VideoTrimmerView {
        RegulatorControl.getInstance().setThumbShowCount(thumbShowCount)
        return this
    }

    fun setAdapterUpdateCount(adapterUpdateCount: Int): VideoTrimmerView {
        RegulatorControl.getInstance().setAdapterUpdateCount(adapterUpdateCount)
        return this
    }

    fun setVideoPath(videoPath: String): VideoTrimmerView {
        RegulatorControl.getInstance().setVideoPath(videoPath)
        return this
    }

    fun handle() {
        RegulatorControl.getInstance().handle()
    }

    fun release() {
        RegulatorControl.getInstance().release()
    }
}