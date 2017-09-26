package com.cjh.videotrimmerlibrary.controls

import android.media.MediaMetadataRetriever
import android.text.TextUtils
import com.cjh.videotrimmerlibrary.Config
import com.cjh.videotrimmerlibrary.callback.GetFrameListener
import com.cjh.videotrimmerlibrary.utils.CommonUtils
import com.cjh.videotrimmerlibrary.vo.ConfigVo
import com.cjh.videotrimmerlibrary.vo.ThumbVo
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

/**
 * Created by cjh on 2017/8/31.
 */
internal class MediaMetadataRetrieverAgent {

    private var mRetr: MediaMetadataRetriever
    private var mConfigVo: ConfigVo

    private constructor() {
        mRetr = MediaMetadataRetriever()
        mConfigVo = ConfigVo()
    }

    companion object {
        private var mInstance: MediaMetadataRetrieverAgent? = null
        fun getInstance(): MediaMetadataRetrieverAgent {
            if (mInstance == null) {
                synchronized(MediaMetadataRetrieverAgent::class) {
                    if (mInstance == null) {
                        mInstance = MediaMetadataRetrieverAgent()
                    }
                }
            }
            return mInstance!!
        }
    }

    fun setVideoPath(videoPath: String) {
        mConfigVo.videoPath = videoPath
    }

    fun setThumbCount(showThumbCount: Int) {
        if (showThumbCount == mConfigVo.showThumbCount) return
        mConfigVo.thumbItemWidth = mConfigVo.showThumbCount * mConfigVo.thumbItemWidth / showThumbCount
        mConfigVo.showThumbCount = showThumbCount
    }

    fun setAdapterUpdateCount(adapterUpdateCount: Int) {
        mConfigVo.adapterUpdateCount = adapterUpdateCount
    }

    fun setThumbItemWH(wh: Array<Int>) {
        mConfigVo.thumbItemWidth = wh[0] / mConfigVo.showThumbCount
        mConfigVo.thumbItemHeight = mConfigVo.thumbItemWidth / 3 * 5
    }

    fun build() {
        if (TextUtils.isEmpty(mConfigVo.videoPath)) throw IllegalArgumentException("MediaMetadataRetrieverUtil build ::: videoPath can not be null or empty")
        initialVideoVo()

    }

    private fun initialVideoVo() {
        mRetr.setDataSource(mConfigVo.videoPath)
        mConfigVo.durationL = java.lang.Long.parseLong(mRetr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))
        val wh = getRightWH()
        mConfigVo.width = wh[0]
        mConfigVo.height = wh[1]

    }

    private fun getRightWH(): Array<Int> {
        var width = mRetr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toInt()
        var height = mRetr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toInt()
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
        return arrayOf(width, height)
    }

    fun getConfigVo(): ConfigVo {
        return mConfigVo
    }

    fun getFrameThumb(listener: GetFrameListener) {
        val radixPosition = calculateRadixPosition()
        val totalThumbCount = mConfigVo!!.durationL / radixPosition
        val shoudDoSpecialLogicAtLastGroup = ((totalThumbCount % mConfigVo!!.adapterUpdateCount) == 0L)
        val groups: ArrayList<Int> = getCountGroups(totalThumbCount, shoudDoSpecialLogicAtLastGroup)
        Flowable.fromIterable(groups)
                .subscribeOn(Schedulers.io())
                .flatMap(object : Function<Int, Publisher<ArrayList<ThumbVo>>> {
                    override fun apply(t: Int): Publisher<ArrayList<ThumbVo>> {
                        return Flowable.just(getFrameThumbVos(t, radixPosition))
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<ArrayList<ThumbVo>> {
                    override fun accept(t: ArrayList<ThumbVo>?) {
                        if (t != null)
                            listener.update(t!!)
                    }
                }, object : Consumer<Throwable> {
                    override fun accept(t: Throwable?) {
                        t?.printStackTrace()
                    }
                })
    }

    private fun getFrameThumbVos(t: Int, radixPosition: Long): ArrayList<ThumbVo> {
        val thumbVos = ArrayList<ThumbVo>()
        val realIndex = t * mConfigVo.adapterUpdateCount
        var pos = 0L
        for (i in 0..mConfigVo.adapterUpdateCount - 1) {
            pos = (realIndex + i) * radixPosition * 1000
            val frame = mRetr.getFrameAtTime(pos, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            thumbVos.add(ThumbVo(CommonUtils.bitmap2byte(CommonUtils.ratio(frame, mConfigVo.thumbItemWidth * 1.5f, mConfigVo.thumbItemHeight * 1.5f)), pos))
        }
        return thumbVos
    }

    private fun getCountGroups(totalThumbCount: Long, shoudDoSpecialLogicAtLastGroup: Boolean): ArrayList<Int> {
        var group = (totalThumbCount / mConfigVo!!.adapterUpdateCount).toInt()
        if (shoudDoSpecialLogicAtLastGroup) group++
        val groupArray = ArrayList<Int>()
        for (i in 0..group - 1) {
            groupArray.add(i)
        }
        return groupArray
    }

    private fun calculateRadixPosition(): Long {
        val theoryThumbCount = mConfigVo!!.durationL / 1000
        var radixPosition = 1000L
        if (theoryThumbCount < mConfigVo.showThumbCount) {
            radixPosition = mConfigVo!!.durationL / mConfigVo.showThumbCount
        }
        return radixPosition
    }

    fun release() {
        mInstance = null
    }

}