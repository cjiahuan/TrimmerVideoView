package com.cjh.videotrimmerlibrary.controls

import com.cjh.videotrimmerlibrary.MediaHandleManager
import com.cjh.videotrimmerlibrary.TrimmerSeekBar
import com.cjh.videotrimmerlibrary.callback.EndTouchActionListener
import com.cjh.videotrimmerlibrary.callback.UpdatePosListener

/**
* Created by cjh on 2017/9/6.
*/
class TrimmerSeekBarControl private constructor(trimmerSeekBar: TrimmerSeekBar, listener: EndTouchActionListener, listener2: UpdatePosListener) : EndTouchActionListener, UpdatePosListener {

    val mTrimmerSeekBar = trimmerSeekBar

    private val endTouchActionListener = listener

    private val updatePosListener = listener2

    override fun updatePos() {
        updateIndex()
        updatePosListener.updatePos()
    }

    var leftIndex = 0

    var rightIndex = MediaHandleManager.getInstance().getConfigVo().visiableThumbCount - 1

    override fun updateRegionIndex() {
        updateIndex()
        endTouchActionListener.updateRegionIndex()
    }

    private fun updateIndex() {
        leftIndex = posConvertIndex(mTrimmerSeekBar.leftPosX).toInt()
        rightIndex = posConvertIndex(mTrimmerSeekBar.rightPosX).toInt()
    }

    fun getLeftPosX(): Float = mTrimmerSeekBar.leftPosX

    fun getRightPosX(): Float = mTrimmerSeekBar.rightPosX

    private fun posConvertIndex(pos: Float): Float {
        if (pos <= 0) return 0f
        val increase = mTrimmerSeekBar.imeasureWidth / MediaHandleManager.getInstance().getConfigVo().visiableThumbCount
        return (0 until MediaHandleManager.getInstance().getConfigVo().visiableThumbCount)
                .firstOrNull { pos < (it + 1) * increase }
                ?.toFloat()
                ?: (MediaHandleManager.getInstance().getConfigVo().visiableThumbCount - 1).toFloat()
    }


    companion object {
        private var mInstance: TrimmerSeekBarControl? = null
        fun getInstance(trimmerSeekBar: TrimmerSeekBar, listener: EndTouchActionListener, updatePosListener: UpdatePosListener): TrimmerSeekBarControl {
            if (mInstance == null) {
                synchronized(TrimmerSeekBarControl::class) {
                    if (mInstance == null) {
                        mInstance = TrimmerSeekBarControl(trimmerSeekBar, listener, updatePosListener)
                        trimmerSeekBar.addEndActionListener(mInstance!!)
                        trimmerSeekBar.addUpdatePosListener(mInstance!!)
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

    fun postInvalidateByConfig(){
        leftIndex = 0
        rightIndex = MediaHandleManager.getInstance().getConfigVo().visiableThumbCount - 1
        mTrimmerSeekBar.postInvalidateByConfig()
    }

    fun release() {
        mInstance = null
    }
}