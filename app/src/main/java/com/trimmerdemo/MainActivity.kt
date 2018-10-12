package com.trimmerdemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
