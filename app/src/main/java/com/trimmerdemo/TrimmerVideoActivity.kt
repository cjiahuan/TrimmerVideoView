package com.trimmerdemo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.cjh.videotrimmerlibrary.DefaultConfig
import com.cjh.videotrimmerlibrary.VideoTrimmerView
import com.cjh.videotrimmerlibrary.callback.IConfig
import com.cjh.videotrimmerlibrary.utils.UriUtils
import kotlinx.android.synthetic.main.activity_trimmer_video.*

/**
 * Created by cjh on 2017/10/9.
 */
open class TrimmerVideoActivity : AppCompatActivity() {

    private val CHOOSE_VIDEO_CODE = 11

    private val REQUEST_PERSION_CODE = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trimmer_video)

        mVideoTrimmerView.setIConfig(getConfig())

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            chooseVideo()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERSION_CODE)
        }
    }

    open fun getConfig(): IConfig? = null

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            chooseVideo()
        }
    }

    private fun chooseVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, CHOOSE_VIDEO_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_VIDEO_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val videoUri = data.data
            val videoPath = UriUtils.getPath(this, videoUri)
            mVideoTrimmerView?.setVideoPath(videoPath!!)?.handle() ?: (findViewById<VideoTrimmerView>(R.id.mVideoTrimmerView))?.setVideoPath(videoPath!!)?.handle()
        }
    }

    @SuppressLint("SetTextI18n")
    open fun compressor(view: View) {
        trimmerPos.text = mVideoTrimmerView.getTrimmerPos()[0].toString() + " ::::::: " + mVideoTrimmerView.getTrimmerPos()[1].toString()
    }

    @SuppressLint("SetTextI18n")
    open fun compressorWidthProgress(view: View) {
        trimmerPos.text = mVideoTrimmerView.getTrimmerPos()[0].toString() + " ::::::: " + mVideoTrimmerView.getTrimmerPos()[1].toString()
    }

    override fun finish() {
        super.finish()
        mVideoTrimmerView.release()
    }


}