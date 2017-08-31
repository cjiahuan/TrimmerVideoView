package com.cjh.videotrimmerlibrary

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.VideoView
import com.cjh.videotrimmerlibrary.callback.GetFrameListener
import com.cjh.videotrimmerlibrary.utils.MediaMetadataRetrieverUtil
import com.cjh.videotrimmerlibrary.vo.ThumbVo

/**
 * Created by cjh on 2017/8/28.
 */
class VideoTrimmerView : FrameLayout {

    val LOG_TAG = VideoTrimmerView::class.java.simpleName

    var mWidth: Int = 0

    var mHeight: Int = 0

    var mLayoutId = R.layout.video_trimmer_view

    var mVideoPath: String? = null

    var mTrimmerTime: Long = Config.DEFAULT_TRIMMER_TIME

    var mRootView: View

    var mVideoView: VideoView? = null

    var mRecyclerView: RecyclerView? = null

    var mUpdateCount = Config.ADAPTER_UPDATE_COUNT

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mRootView = LayoutInflater.from(context).inflate(mLayoutId, this)
        mVideoView = mRootView.findViewById(R.id.mVideoView)
        mRecyclerView = mRootView.findViewById(R.id.mRecyclerView)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        initialVideoViewWH()
    }

    private fun initialVideoViewWH() {
        mVideoView!!.layoutParams.width = mWidth
        mVideoView!!.layoutParams.height = mHeight / 2
    }

    fun setTrimmerTime(trimmerTime: Long): VideoTrimmerView {
        mTrimmerTime = trimmerTime
        return this
    }

    fun setAdapterUpdateCount(updateCount: Int): VideoTrimmerView {
        mUpdateCount = updateCount
        return this
    }

    fun setVideoPath(videoPath: String) {
        mVideoPath = videoPath
        if (mVideoView != null) {
            mVideoView!!.setVideoPath(mVideoPath)
            mVideoView!!.requestFocus()
            mVideoView!!.start()
            MediaMetadataRetrieverUtil.getInstance(mVideoPath!!, Config.DEFAULT_THUMB_COUNT, mUpdateCount).getFrameThumb(object : GetFrameListener {
                override fun update(thumbs: ArrayList<ThumbVo>) {

                }
            })
        }
    }

    fun handle(){
        MediaMetadataRetrieverUtil.getInstance(mVideoPath!!, Config.DEFAULT_THUMB_COUNT, mUpdateCount)
    }

    fun release() {
        if (mVideoView != null) {
            mVideoView!!.pause()
            mVideoView!!.stopPlayback()
        }
    }
}