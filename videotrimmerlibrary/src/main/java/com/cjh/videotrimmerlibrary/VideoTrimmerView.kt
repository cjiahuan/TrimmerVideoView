package com.cjh.videotrimmerlibrary

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.cjh.videotrimmerlibrary.controls.RegulatorControl

/**
 * Created by cjh on 2017/8/28.
 */
class VideoTrimmerView : FrameLayout {

    var mLayoutId = R.layout.video_trimmer_view

    lateinit var recyclerView: RecyclerView

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val rootView = LayoutInflater.from(context).inflate(mLayoutId, this)
        recyclerView = rootView.findViewById(R.id.mRecyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        RegulatorControl.getInstance(rootView.findViewById(R.id.mVideoView), recyclerView!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChild(recyclerView, widthMeasureSpec, heightMeasureSpec)
        RegulatorControl.getInstance()
                .initialThumbItemWH(arrayOf(recyclerView.measuredWidth))
                .initialVideoViewWH(arrayOf(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec)))
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