package com.cjh.videotrimmerlibrary.controls

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import com.cjh.videotrimmerlibrary.ThumbAdapter
import com.cjh.videotrimmerlibrary.callback.GetFrameListener
import com.cjh.videotrimmerlibrary.callback.EndScrollActionListener
import com.cjh.videotrimmerlibrary.vo.ThumbVo

/**
 * Created by cjh on 2017/8/30.
 */
class RecyclerViewControl private constructor(recyclerView: RecyclerView, updateTrimmerViewsListener: EndScrollActionListener) : GetFrameListener, RecyclerView.OnScrollListener() {

    val mUpdateTrimmerViewsListener: EndScrollActionListener = updateTrimmerViewsListener

    val mRecyclerView = recyclerView

    val mThumbAdapter: ThumbAdapter = mRecyclerView.adapter as ThumbAdapter

    var firstItemPosition: Int = 0

    companion object {
        private var mInstance: RecyclerViewControl? = null
        fun getInstance(recyclerView: RecyclerView, updateTrimmerViewsListener: EndScrollActionListener): RecyclerViewControl {
            if (mInstance == null) {
                synchronized(RecyclerViewControl::class) {
                    if (mInstance == null) {
                        recyclerView.adapter = ThumbAdapter()
                        mInstance = RecyclerViewControl(recyclerView, updateTrimmerViewsListener)
                        recyclerView.addOnScrollListener(mInstance)
                    }
                }
            }
            return mInstance!!
        }

        fun getInstance(): RecyclerViewControl {
            if (mInstance == null) {
                throw IllegalArgumentException("VideoViewControl getInstance ::: must call method getInstance(videoView: VideoView, videoPath: String, maxWidth: Int, maxHeight: Int) first !!!")
            }
            return mInstance!!
        }
    }

    override fun update(thumbs: ArrayList<ThumbVo>) {
        mThumbAdapter.addDatas(thumbs)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == SCROLL_STATE_IDLE) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            firstItemPosition = layoutManager.findFirstVisibleItemPosition()
            mUpdateTrimmerViewsListener.updateByScroll()
        }

    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        val layoutManager = recyclerView?.layoutManager as LinearLayoutManager
        firstItemPosition = layoutManager.findFirstVisibleItemPosition()
        mUpdateTrimmerViewsListener.updateByScroll()
    }

}