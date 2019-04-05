package com.litesocket.sample

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    private val TAG = "MainActivity"
    private lateinit var mThread: Thread

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initListener() {
        super.initListener()
        btnSocket.setOnClickListener {
            startActivity(Intent(this, LiteSocketActivity::class.java))
        }

        btnLongSocket.setOnClickListener {
            startActivity(Intent(this, LiteLongSocketActivity::class.java))
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
        super.bindData(savedInstanceState)


    }




}
