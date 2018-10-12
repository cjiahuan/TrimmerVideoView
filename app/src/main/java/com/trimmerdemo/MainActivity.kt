package com.trimmerdemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.hiteshsondhi88.libffmpeg.videocompress.Compressor
import com.github.hiteshsondhi88.libffmpeg.videocompress.InitListener

class MainActivity : AppCompatActivity() {

    lateinit var mCompressor: Compressor

    var isLoadLibrary = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initCompressor()

    }

    private fun initCompressor() {
        mCompressor = Compressor(this)
        mCompressor.loadBinary(object : InitListener {
            override fun onLoadSuccess() {
                isLoadLibrary = true
                LogUtils.e("onLoadSuccess")
            }

            override fun onLoadFail(reason: String) {
                isLoadLibrary = false
                LogUtils.e("onLoadFail $reason")
            }
        })
    }

    fun trimmer(view: View) {
        startActivity(Intent(this, TrimmerVideoActivity::class.java))
    }

    fun trimmerByCustomConfig(view: View) {
        startActivity(Intent(this, TrimmerVideoByCustomConfigActivity::class.java))
    }

    fun trimmerByCustomDefaultConfig(view: View) {
        startActivity(Intent(this, TrimmerVideoByCustomDefaultConfigActivity::class.java))
    }

    fun compressorAndTrim(view : View){
        startActivity(Intent(this, TrimmerAndCompressorVideoActivity::class.java))
    }
}
