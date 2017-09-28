package com.cjh.videotrimmerlibrary.controls

import android.util.Log
import com.cjh.videotrimmerlibrary.MediaHandleManager
import com.cjh.videotrimmerlibrary.TrimmerSeekBar
import com.cjh.videotrimmerlibrary.callback.EndTouchActionListener

/**
 * Created by cjh on 2017/9/6.
 */
class TrimmerSeekBarControl private constructor(trimmerSeekBar: TrimmerSeekBar, listener: EndTouchActionListener) : EndTouchActionListener {

    var leftIndex = 0

    var rightIndex = MediaHandleManager.getInstance().getConfigVo().showThumbCount

    override fun updateRegionIndex() {
        leftIndex = posConvertIndex(mTrimmerSeekBar.leftPosX).toInt()
        rightIndex = posConvertIndex(mTrimmerSeekBar.rightPosX).toInt()
        endTouchActionListener.updateRegionIndex()
    }

    private fun posConvertIndex(pos: Float): Float {
        if (pos <= 0) {
            return 0f
        }
        if (pos >= mTrimmerSeekBar.imeasureWidth) {
            return MediaHandleManager.getInstance().getConfigVo().showThumbCount.toFloat()
        }
        val increase = mTrimmerSeekBar.imeasureWidth / MediaHandleManager.getInstance().getConfigVo().showThumbCount
        return (0 until MediaHandleManager.getInstance().getConfigVo().showThumbCount)
                .firstOrNull { pos <= (it + 1) * increase }
                ?.toFloat()
                ?: 0.toFloat()
    }

    val mTrimmerSeekBar = trimmerSeekBar

    val endTouchActionListener = listener

    companion object {
        private var mInstance: TrimmerSeekBarControl? = null
        fun getInstance(trimmerSeekBar: TrimmerSeekBar, listener: EndTouchActionListener): TrimmerSeekBarControl {
            if (mInstance == null) {
                synchronized(TrimmerSeekBarControl::class) {
                    if (mInstance == null) {
                        mInstance = TrimmerSeekBarControl(trimmerSeekBar, listener)
                        trimmerSeekBar.addEndActionListener(mInstance!!)
                    }
                }
            }
            return mInstance!!
        }

        fun getInstance(): TrimmerSeekBarControl {
            if (mInstance == null) {
                throw IllegalArgumentException("TrimmerSeekBarControl getInstance ::: must call method getInstance(trimmerSeekBar: TrimmerSeekBar) first !!!")
            }
            return mInstance!!
        }
    }
}