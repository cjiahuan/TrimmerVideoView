package com.cjh.videotrimmerlibrary.controls

import android.support.v7.widget.RecyclerView
import com.cjh.videotrimmerlibrary.ThumbAdapter
import com.cjh.videotrimmerlibrary.callback.GetFrameListener
import com.cjh.videotrimmerlibrary.vo.ThumbVo

/**
 * Created by cjh on 2017/8/30.
 */
class RecyclerViewControl private constructor(recyclerView: RecyclerView) : GetFrameListener {

    val mRecyclerView = recyclerView

    val mThumbAdapter: ThumbAdapter = mRecyclerView.adapter as ThumbAdapter

    companion object {
        private var mInstance: RecyclerViewControl? = null
        fun getInstance(recyclerView: RecyclerView): RecyclerViewControl {
            if (recyclerView == null) throw IllegalArgumentException("VideoViewControl getInstance ::: videoView is null ")
            if (mInstance == null) {
                synchronized(RecyclerViewControl::class) {
                    if (mInstance == null) {
                        recyclerView.adapter = ThumbAdapter()
                        mInstance = RecyclerViewControl(recyclerView)
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

}