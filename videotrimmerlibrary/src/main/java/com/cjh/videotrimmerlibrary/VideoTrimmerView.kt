package com.cjh.videotrimmerlibrary

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.VideoView
import com.cjh.videotrimmerlibrary.callback.IConfig
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
        getRecyclerView().layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        RegulatorControl.getInstance(this)
    }

    @SuppressLint("WrongViewCast")
    fun getVideoView(): VideoView {
        return findViewById<VideoView>(R.id.videoView)
    }

    @SuppressLint("WrongViewCast")
    fun getRecyclerView(): RecyclerView {
        return findViewById<RecyclerView>(R.id.recyclerView)
    }

    @SuppressLint("WrongViewCast")
    fun getTrimmerSeekBar(): TrimmerSeekBar {
        return findViewById<TrimmerSeekBar>(R.id.trimmerSeekBar)
    }

    @SuppressLint("WrongViewCast")
    fun getLeftPosTextView(): TextView {
        return findViewById<TextView>(R.id.leftPos)
    }

    @SuppressLint("WrongViewCast")
    fun getRightPosTextView(): TextView {
        return findViewById<TextView>(R.id.rightPos)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        RegulatorControl.getInstance()
                .initialThumbItemWH(arrayOf(MeasureSpec.getSize(widthMeasureSpec) - DensityUtils.dip2px(context, 25f * 2), recyclerView.measuredHeight))
    }

    fun setVideoPath(videoPath: String): VideoTrimmerView {
        RegulatorControl.getInstance().setVideoPath(videoPath)
        return this
    }

    fun setIConfig(icg: IConfig) {
        RegulatorControl.getInstance().setIConfig(icg)
    }

    fun handle() {
        RegulatorControl.getInstance().handle()
    }

    fun release() {
        RegulatorControl.getInstance().release()
    }

    fun getTrimmerPos(): LongArray = RegulatorControl.getInstance().getTrimmerPos()
}