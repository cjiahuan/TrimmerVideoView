package com.cjh.videotrimmerlibrary

import android.media.MediaMetadataRetriever
import android.text.TextUtils
import com.cjh.videotrimmerlibrary.callback.GetFrameListener
import com.cjh.videotrimmerlibrary.callback.IConfig
import com.cjh.videotrimmerlibrary.utils.CommonUtils
import com.cjh.videotrimmerlibrary.vo.ConfigVo
import com.cjh.videotrimmerlibrary.vo.ThumbVo
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by cjh on 2017/8/31.
 */
internal class MediaHandleManager {

    private var mRetr: MediaMetadataRetriever
    private var mConfigVo: ConfigVo

    private constructor() {
        mRetr = MediaMetadataRetriever()
        mConfigVo = ConfigVo()
        mConfigVo.iConfig = DefaultConfig()
    }

    companion object {
        private var mInstance: MediaHandleManager? = null
        fun getInstance(): MediaHandleManager {
            if (mInstance == null) {
                synchronized(MediaHandleManager::class) {
                    if (mInstance == null) {
                        mInstance = MediaHandleManager()
                    }
                }
            }
            return mInstance!!
        }
    }

    fun setIConfig(icg: IConfig): MediaHandleManager {
        mConfigVo.iConfig = icg
        return this
    }

    fun setVideoPath(videoPath: String): MediaHandleManager {
        mConfigVo.videoPath = videoPath
        initialConfigVo()
        return this
    }

    private fun initialConfigVo() {
        mRetr.setDataSource(mConfigVo.videoPath)
        mConfigVo.durationL = java.lang.Long.parseLong(mRetr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))
        val wh = getRightWH()
        mConfigVo.width = wh[0]
        mConfigVo.height = wh[1]
    }

    fun setThumbItemWH(wh: Array<Int>) {
        if (mConfigVo.thumbItemWidth > 0 && mConfigVo.thumbItemHeight > 0) {
            return
        }
        mConfigVo.thumbItemWidth = wh[0] / mConfigVo.showThumbCount
        mConfigVo.thumbItemHeight = wh[1]
    }

    private fun getRightWH(): Array<Int> {
        var width = mRetr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toInt()
        var height = mRetr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toInt()
        val min = Math.min(width, height)
        val max = Math.max(width, height)
        val orientation = mRetr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
        if (Constant.VERTICAL == orientation) {
            width = min
            height = max
        } else {
            width = max
            height = min
        }
        return arrayOf(width, height)
    }

    fun getConfigVo(): ConfigVo = mConfigVo

    fun getFrameThumb(listener: GetFrameListener) {
        val radixPosition = calculateRadixPosition()
        val totalThumbCount = mConfigVo.durationL / radixPosition
        val shoudDoSpecialLogicAtLastGroup = ((totalThumbCount % mConfigVo.adapterUpdateCount) != 0L)
        val groups: ArrayList<Int> = getCountGroups(totalThumbCount, shoudDoSpecialLogicAtLastGroup)
        Flowable.fromIterable(groups)
                .subscribeOn(Schedulers.io())
                .flatMap { t -> Flowable.just(getFrameThumbVos(t, radixPosition)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t != null) listener.update(t)
                }, { t -> t?.printStackTrace() })
    }

    private fun getFrameThumbVos(t: Int, radixPosition: Long): ArrayList<ThumbVo> {
        val thumbVos = ArrayList<ThumbVo>()
        val realIndex = t * mConfigVo.adapterUpdateCount
        var pos: Long
        for (i in 0 until mConfigVo.adapterUpdateCount) {
            pos = (realIndex + i) * radixPosition * 1000
            val frame = mRetr.getFrameAtTime(pos, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            thumbVos.add(ThumbVo(CommonUtils.bitmap2byte(CommonUtils.ratio(frame, mConfigVo.thumbItemWidth * 1.5f, mConfigVo.thumbItemHeight * 1.5f)), pos))
        }
        return thumbVos
    }

    private fun getCountGroups(totalThumbCount: Long, shoudDoSpecialLogicAtLastGroup: Boolean): ArrayList<Int> {
        var group = (totalThumbCount / mConfigVo.adapterUpdateCount).toInt()
        if (shoudDoSpecialLogicAtLastGroup) group++
        val groupArray = ArrayList<Int>()
        for (i in 0 until group) {
            groupArray.add(i)
        }
        return groupArray
    }

    private fun calculateRadixPosition(): Long {
        val theoryThumbCount = mConfigVo.durationL / 1000
        var radixPosition = 1000L
        if (theoryThumbCount < mConfigVo.showThumbCount) {
            radixPosition = mConfigVo.durationL / mConfigVo.showThumbCount
        }
        return radixPosition
    }

    fun release() {
        mInstance = null
    }

}