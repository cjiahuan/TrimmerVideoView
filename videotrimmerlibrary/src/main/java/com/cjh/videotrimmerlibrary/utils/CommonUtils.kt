package com.cjh.videotrimmerlibrary.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * Created by cjh on 2017/9/6.
 */
class CommonUtils {

    companion object {
        fun bitmap2byte(bitmap: Bitmap): ByteArray {
            val ba = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ba)
            return ba.toByteArray()
        }

        fun ratio(image: Bitmap, pixelW: Float, pixelH: Float): Bitmap {
            val newSrc = ByteArrayInputStream(bitmap2byte(image))
            val newOpts = BitmapFactory.Options()
            newOpts.inJustDecodeBounds = true
            newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888
            BitmapFactory.decodeStream(newSrc, null, newOpts)
            val w = newOpts.outWidth
            val h = newOpts.outHeight
            var be = 1
            if (w > h && w > pixelW) {
                be = (newOpts.outWidth / pixelW).toInt()
            } else if (w < h && h > pixelH) {
                be = (newOpts.outHeight / pixelH).toInt()
            }
            if (be <= 0) be = 1
            newOpts.inJustDecodeBounds = false
            newSrc.reset()
            newOpts.inSampleSize = be
            var bitmap = BitmapFactory.decodeStream(newSrc, null, newOpts)
            if (bitmap == null) bitmap = image
            return bitmap
        }


    }

}