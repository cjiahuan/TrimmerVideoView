package com.cjh.videotrimmerlibrary.utils

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import com.cjh.videotrimmerlibrary.Config
import com.cjh.videotrimmerlibrary.callback.GetFrameListener
import com.cjh.videotrimmerlibrary.vo.ThumbVo
import com.cjh.videotrimmerlibrary.vo.VideoVo
import io.reactivex.Flowable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * Created by cjh on 2017/8/31.
 */
internal class MediaMetadataRetrieverUtil {

    private var mRetr: MediaMetadataRetriever
    private var mVideoPath: String
    private var mThumbCount = Config.DEFAULT_THUMB_COUNT
    private var mVideoVo: VideoVo? = null
    private var mUpdateCount = Config.ADAPTER_UPDATE_COUNT

    private constructor(videoPath: String, thumbCount: Int, updateCount: Int) {
        mVideoPath = videoPath
        mThumbCount = thumbCount
        mUpdateCount = updateCount
        mRetr = MediaMetadataRetriever()
        mRetr.setDataSource(videoPath)
        initialVideoVo()
    }

    companion object {
        private var mInstance: MediaMetadataRetrieverUtil? = null
        fun getInstance(videoPath: String, thumbCount: Int, updateCount: Int): MediaMetadataRetrieverUtil {
            if (TextUtils.isEmpty(videoPath)) throw IllegalArgumentException("MediaMetadataRetrieverUtil getInstance ::: videoPath can not be null or empty")
            if (mInstance == null) {
                synchronized(MediaMetadataRetrieverUtil::class) {
                    if (mInstance == null) {
                        mInstance = MediaMetadataRetrieverUtil(videoPath, thumbCount, updateCount)
                    }
                }
            }
            return mInstance!!
        }

        fun getInstance(): MediaMetadataRetrieverUtil {
            if (mInstance == null) throw IllegalArgumentException("MediaMetadataRetrieverUtil getInstance ::: must call method getInstance(videoPath: String, thumbCount: Int) first !!!")
            return mInstance!!
        }
    }

    private fun initialVideoVo() {
        var width = mRetr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toInt()
        var height = mRetr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toInt()
        val videoLengthL = java.lang.Long.parseLong(mRetr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))
        val min = Math.min(width, height)
        val max = Math.max(width, height)
        val orientation = mRetr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        if (Config.VERTICAL.equals(orientation)) {
            width = min
            height = max
        } else {
            width = max
            height = min
        }
        mVideoVo = VideoVo(width, height, videoLengthL)
    }

    fun getVideoVo(): VideoVo {
        return mVideoVo!!
    }

    fun getFrameThumb(listener: GetFrameListener) {
        val radixPosition = calculateRadixPosition()
        val totalThumbCount = mVideoVo!!.durationL / radixPosition
        val groups = getCountGroups(totalThumbCount)
        Flowable.just(listener)
                .subscribeOn(Schedulers.io())
                .map(object : Function)
        Flowable.just(listener)
                .flatMap(object : Function<GetFrameListener, Flowable<ArrayList<ThumbVo>>> {
                    override fun apply(t: GetFrameListener?): Flowable<ArrayList<ThumbVo>> {
                        val radixPosition = calculateRadixPosition()
                        val totalThumbCount = mVideoVo!!.durationL / radixPosition
                        val thumbs = ArrayList<ThumbVo>()
                        for (i in 0..totalThumbCount - 1) {
                            val position = i * radixPosition
                            val bitmap = mRetr.getFrameAtTime(position, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                            if (bitmap != null) {
                                thumbs.add(ThumbVo(bitmap, position))
                                if (thumbs.size == Config.THUMB_UPDATE_COUNT) {
                                    Flowable.just(thumbs)
                                            .subscribeOn(AndroidSchedulers.mainThread())
                                            .subscribe(object : Consumer<ArrayList<ThumbVo>> {
                                                override fun accept(t: ArrayList<ThumbVo>?) {
                                                    t.toString()
                                                }
                                            }, object : Consumer<Throwable> {
                                                override fun accept(t: Throwable?) {
                                                    t?.printStackTrace()
                                                }
                                            })
                                    thumbs.clear()
                                }
                            }
                        }
                        mRetr.release()
                        return Flowable.just(thumbs)
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<ArrayList<ThumbVo>> {
                    override fun accept(t: ArrayList<ThumbVo>?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }


                })
//                .subscribe(object : Consumer<ArrayList<ThumbVo>> {
//                    override fun accept(t: ArrayList<ThumbVo>?) {
//                        t.toString()
//                    }
//                }, object : Consumer<Throwable> {
//                    override fun accept(t: Throwable?) {
//                        t?.printStackTrace()
//                    }
//                })

    }

    private fun getCountGroups(totalThumbCount: Long) {

    }

    private fun calculateRadixPosition(): Long {
        val theoryThumbCount = mVideoVo!!.durationL / 1000
        var radixPosition = 1000L
        if (theoryThumbCount < mThumbCount) {
            radixPosition = mVideoVo!!.durationL / mThumbCount
        }
        return radixPosition
    }

}