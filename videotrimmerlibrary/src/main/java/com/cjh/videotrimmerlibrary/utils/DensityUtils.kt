package com.cjh.videotrimmerlibrary.utils

import android.content.Context

/**
 * Created by cjh on 2017/9/4.
 */
class DensityUtils {

    companion object {
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }

}