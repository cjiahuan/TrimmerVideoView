package com.cjh.videotrimmerlibrary.callback

import com.cjh.videotrimmerlibrary.vo.ThumbVo

/**
 * Created by cjh on 2017/8/31.
 */
interface GetFrameListener {

    fun update(thumbs: ArrayList<ThumbVo>)

}