package com.cjh.videotrimmerlibrary

import android.media.MediaMetadataRetriever
import com.cjh.videotrimmerlibrary.callback.GetFrameListener
import com.cjh.videotrimmerlibrary.callback.IConfig
import com.cjh.videotrimmerlibrary.utils.CommonUtils
import com.cjh.videotrimmerlibrary.vo.ConfigVo
import com.cjh.videotrimmerlibrary.vo.ThumbVo
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

/**
* Created by cjh on 2017/8/31.
*/
internal class MediaHandleManager {

    private var mRetr: MediaMetadataRetriever
    private var mConfigVo: ConfigVo

    private var disposable: Disposable? = null

    private constructor() {
        mRetr = MediaMetadataRetriever()
        mConfigVo = ConfigVo()
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
        mConfigVo.updateIConfig(icg)
        return this
    }

    fun setVideoPath(videoPath: String): MediaHandleManager {
        mConfigVo.videoPath = videoPath
        initialConfigVo()
        return this
    }

    private fun initialConfigVo() {
        mRetr = MediaMetadataRetriever()
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
        mConfigVo.thumbItemWidth = wh[0] / mConfigVo.visiableThumbCount
        mConfigVo.thumbItemHeight = wh[1]
    }

    private fun getRightWH(): Array<Int> {
        var width = mRetr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toInt()
        var height = mRetr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toInt()
        val bitmap = mRetr.getFrameAtTime((5 * 1000 + 1L), MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        if(bitmap!= null && bitmap.width >0 && bitmap.height > 0){
            return arrayOf(bitmap.width, bitmap.height)
        }
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
        disposable = Flowable.fromIterable(groups)
                .subscribeOn(Schedulers.io())
                .flatMap { t -> Flowable.just(getFrameThumbVos(t, radixPosition)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t != null) listener.update(t)
                }, { t -> t.printStackTrace() }) { mRetr.release() }
    }

    //    CommonUtils.ratio(frame, mConfigVo.thumbItemWidth * 1.5f, mConfigVo.thumbItemHeight * 1.5f)
    private fun getFrameThumbVos(t: Int, radixPosition: Long): ArrayList<ThumbVo> {
        val thumbVos = ArrayList<ThumbVo>()
        val realIndex = t * mConfigVo.adapterUpdateCount
        var pos: Long
        for (i in 0 until mConfigVo.adapterUpdateCount) {
            pos = (realIndex + i) * radixPosition * 1000
            if (pos > mConfigVo.durationL * 1000) {
                break
            }
            try {
                val frame = mRetr.getFrameAtTime(pos, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                thumbVos.add(ThumbVo(CommonUtils.bitmap2byte(frame), pos))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return thumbVos
    }

    private fun getCountGroups(totalThumbCount: Long, shoudDoSpecialLogicAtLastGroup: Boolean): ArrayList<Int> {
        var group = (totalThumbCount / mConfigVo.adapterUpdateCount).toInt()
        if (shoudDoSpecialLogicAtLastGroup) group++
        val groupArray = ArrayList<Int>()
        groupArray += 0 until group
        return groupArray
    }

    private fun calculateRadixPosition(): Long {
        var radixPosition = mConfigVo.trimmerTimeL / (mConfigVo.visiableThumbCount-1)
        if (mConfigVo.trimmerTimeL > mConfigVo.durationL) {
            radixPosition = mConfigVo.durationL / (mConfigVo.visiableThumbCount)
        }
        return radixPosition
    }

    fun release() {
        mInstance = null
        mRetr.release()
        disposable?.dispose()
    }

}