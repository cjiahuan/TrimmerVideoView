package com.cjh.videotrimmerlibrary.utils

import io.reactivex.Observer

/**
 * Created by cjh on 2017/8/31.
 */
abstract class VObserver<T> : Observer<T> {

    override fun onError(t: Throwable?) {
        t?.printStackTrace()
    }

    override fun onComplete() {

    }

}