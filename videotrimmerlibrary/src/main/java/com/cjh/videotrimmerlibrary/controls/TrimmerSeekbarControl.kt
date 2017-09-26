package com.cjh.videotrimmerlibrary.controls

import com.cjh.videotrimmerlibrary.TrimmerSeekBar

/**
 * Created by cjh on 2017/9/6.
 */
class TrimmerSeekBarControl private constructor(trimmerSeekBar: TrimmerSeekBar) {

    val mTrimmerSeekBar = trimmerSeekBar

    companion object {
        private var mInstance: TrimmerSeekBarControl? = null
        fun getInstance(trimmerSeekBar: TrimmerSeekBar): TrimmerSeekBarControl {
            if (trimmerSeekBar == null) throw IllegalArgumentException("TrimmerSeekBarControl getInstance ::: trimmerSeekBar is null ")
            if (mInstance == null) {
                synchronized(TrimmerSeekBarControl::class) {
                    if (mInstance == null) {
                        mInstance = TrimmerSeekBarControl(trimmerSeekBar)
                    }
                }
            }
            return mInstance!!
        }
    }



}