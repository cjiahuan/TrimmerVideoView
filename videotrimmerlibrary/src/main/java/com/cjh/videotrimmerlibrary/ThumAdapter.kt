package com.cjh.videotrimmerlibrary

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream


/**
 * Created by cjh on 2017/8/30.
 */
class ThumAdapter : RecyclerView.Adapter<ThumAdapter.ThumViewHolder> {

    var mDatas: ArrayList<Bitmap> = arrayListOf<Bitmap>()

    var mThumbWidth = 0

    var mThumbHeight = 0

    constructor(recyclerViewWidth: Int, recyclerViewHeight: Int, thumbCount: Int) : super() {
        mThumbWidth = recyclerViewWidth / (if (thumbCount > 0) thumbCount else Config.DEFAULT_THUMB_COUNT)
        mThumbHeight = recyclerViewHeight
    }

    fun addDatas(datas: ArrayList<Bitmap>) {
        val positionStart = mDatas.size
        mDatas.addAll(datas)
        notifyItemRangeInserted(positionStart, datas.size)
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ThumViewHolder {
        val thumbImageView = ImageView(parent?.context)
        thumbImageView.layoutParams.width = mThumbWidth
        thumbImageView.layoutParams.height = mThumbHeight
        return ThumViewHolder(thumbImageView)
    }

    override fun onBindViewHolder(holder: ThumViewHolder?, position: Int) {
        if (mDatas[position] != null) {
            holder?.render(mDatas[position])
        }
    }

    inner class ThumViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        fun render(bitmap: Bitmap) {
            Flowable.just(bitmap)
                    .map(object : Function<Bitmap, ByteArray> {
                        override fun apply(t: Bitmap?): ByteArray {
                            return bitmap2byte(t!!)
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Consumer<ByteArray> {
                        override fun accept(t: ByteArray?) {
                            Glide.with(itemView.context).load(t).centerCrop().crossFade().override(mThumbWidth, mThumbHeight).into(itemView as ImageView)
                        }
                    }, object : Consumer<Throwable> {
                        override fun accept(t: Throwable?) {
                        }
                    })
        }

        fun bitmap2byte(bitmap: Bitmap): ByteArray {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            return baos.toByteArray()
        }
    }
}
