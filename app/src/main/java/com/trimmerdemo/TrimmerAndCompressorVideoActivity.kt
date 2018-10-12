package com.trimmerdemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.cjh.ffmpeg.FileUtils
import com.cjh.ffmpeg.utils.TrimVideoUtil
import com.cjh.ffmpeg.videocompress.CompressListener
import com.cjh.ffmpeg.videocompress.Compressor
import com.cjh.ffmpeg.videocompress.CompressorHandler.getCommand
import com.cjh.ffmpeg.videocompress.GetCommandListener
import kotlinx.android.synthetic.main.activity_trimmer_video.*
import java.io.File

class TrimmerAndCompressorVideoActivity : TrimmerVideoActivity() {

    var VIDEOCOMPRESSORDIR: String = ""

    lateinit var MOUTPUTVIDEOPATH: String

    private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val PERMISSIONS_REQUEST_CODE = 321

    private var compressor: Compressor? = null

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
        val start: String = TrimVideoUtil.convertSecondsToTime(trimmerPosArray[0] / 1000)
        val editTime = TrimVideoUtil.convertSecondsToTime((trimmerPosArray[1] - trimmerPosArray[0]) / 1000)
        compressor(start, editTime)
    }

    private fun compressor(start: String, editTime: String) {
        MOUTPUTVIDEOPATH = VIDEOCOMPRESSORDIR + "/" + System.currentTimeMillis() + ".mp4"
        val configVo = mVideoTrimmerView.getConfigVo()
        if (compressor == null) {
            compressor = Compressor(this)
        }

        val cmd = getCommand(configVo.videoPath, MOUTPUTVIDEOPATH, start, editTime, configVo.width, configVo.height, object : GetCommandListener{
            override fun checkSourceSuccess(sourceVideoPath: String?, cmd: String?): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun errorSourcePath(sourceVideoPath: String?, reason: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        compressor!!.execCommand(cmd, object : CompressListener {
            override fun onExecSuccess(message: String) {
                LogUtils.e(FileUtils.getFileSize(MOUTPUTVIDEOPATH))
                val intent = Intent()
                intent.action = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE//扫描单个文件
                intent.data = Uri.fromFile(File(MOUTPUTVIDEOPATH))//给图片的绝对路径
                sendBroadcast(intent)
            }

            override fun onExecFail(reason: String) {
                LogUtils.e("onExecFail")
            }

            override fun onExecProgress(message: String) {
                LogUtils.e(message)
//                onProgress(getProgress(message, trimmerl.toDouble(), TrimVideoUtil.coverStringToMillis(start), mLastProgress))
            }
        })
    }
}