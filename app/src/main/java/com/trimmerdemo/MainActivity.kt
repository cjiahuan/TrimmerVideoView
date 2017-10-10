package com.trimmerdemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    private val CHOOSE_VIDEO_CODE = 11

    private val REQUEST_PERSION_CODE = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun trimmer(view: View) {
        startActivity(Intent(this, TrimmerVideoActivity::class.java))
    }
}
