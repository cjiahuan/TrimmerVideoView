package com.trimmerdemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.cjh.ffmpeg.FileUtils
import com.cjh.ffmpeg.videocompress.CompressListener
import com.cjh.ffmpeg.videocompress.CompressorHandler
import kotlinx.android.synthetic.main.activity_trimmer_video.*

class TrimmerAndCompressorVideoActivity : TrimmerVideoActivity() {

    var VIDEOCOMPRESSORDIR: String = ""

    lateinit var MOUTPUTVIDEOPATH: String

    private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val PERMISSIONS_REQUEST_CODE = 321

    override fun compressor(view: View) {
        VIDEOCOMPRESSORDIR = Environment.getExternalStorageDirectory().absolutePath + "/compressor"

        if (!isPermissionsGrant()) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE)
        } else {
            precompressor()
        }
    }

    fun isPermissionsGrant(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        precompressor()
    }

    @SuppressLint("SetTextI18n")
    private fun precompressor() {
        val trimmerPosArray = mVideoTrimmerView.getTrimmerPos()
        trimmerPos.text = trimmerPosArray[0].toString() + " ::::::: " + trimmerPosArray[1].toString()
        compressor(trimmerPosArray[0], trimmerPosArray[1])
    }

    private fun compressor(start: Long, endPos: Long) {
        MOUTPUTVIDEOPATH = VIDEOCOMPRESSORDIR + "/" + System.currentTimeMillis() + ".mp4"
        val configVo = mVideoTrimmerView.getConfigVo()

        val lastTime = System.currentTimeMillis()

        CompressorHandler.excute(true,
                this,
                configVo.videoPath,
                MOUTPUTVIDEOPATH,
                start,
                endPos,
                configVo.width,
                configVo.height,
                object : CompressListener() {
                    @SuppressLint("SetTextI18n")
                    override fun onExecSuccess(message: String?) {
                        LogUtils.e("MOUTPUTVIDEOPATH -> $MOUTPUTVIDEOPATH ||| size -> ${FileUtils.getFileSize(MOUTPUTVIDEOPATH)}")
                        msg.text = "${msg.text} + \n\n" + "MOUTPUTVIDEOPATH -> $MOUTPUTVIDEOPATH ||| size -> ${FileUtils.getFileSize(MOUTPUTVIDEOPATH)}"
                    }

                    override fun onExecFail(reason: String?) {
                    }

                    override fun onExecProgress(cmd: String, orginalMessage: String, progress: Int) {
                        msg.visibility = View.VISIBLE
                        LogUtils.e("onExecProgress -> $progress")
                        val sb = StringBuilder("cmd -> $cmd")
                                .append("\n\n")
                                .append("progress -> $progress")
                                .append("\n\n")
                                .append(orginalMessage)
                                .append("\n\n")
                                .append("time -> ${(System.currentTimeMillis() - lastTime) / 1000f}")
                                .toString()
                        msg.text = sb
                    }

                })

    }
}