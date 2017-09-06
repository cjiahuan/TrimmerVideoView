package com.trimmerdemo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.cjh.videotrimmerlibrary.VideoTrimmerView
import com.cjh.videotrimmerlibrary.utils.UriUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val CHOOSE_VIDEO_CODE = 11

    val REQUEST_PERSION_CODE = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            chooseVideo()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            chooseVideo()
        }
    }

    fun chooseVideo() {
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
            mVideoTrimmerView?.setVideoPath(videoPath!!)?.handle() ?: (findViewById(R.id.mVideoTrimmerView) as VideoTrimmerView)?.setVideoPath(videoPath!!).handle()
        }
    }
}
