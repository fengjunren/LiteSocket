package com.litesocket

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class ReceiveHearBeat : BroadcastReceiver() {
    private val TAG="ReceiveHearBeat"

    override fun onReceive(context: Context?, intent: Intent?) {
        val lastReceiveTime=LiteLongSocket.instance.getLastReceiveTime()
        val sdf=SimpleDateFormat("hh:mm:ss")
        Log.i(TAG,"-----------【ReceiveHearBeat】----------"+sdf.format(Date())+",lastReceiveTime:${sdf.format(lastReceiveTime)}"+",intent.action:"+intent?.action)
       intent?.action?.run {
           when(this){
               "com.litesocket.ReceiveHearBeat.sendHeartBeat" -> {
                   val d=System.currentTimeMillis()-lastReceiveTime
                   LiteLongSocket.instance.sendHearBeat()
                   if(d>LiteLongSocket.instance.getConfig().heartBeatTime+10*1000){
                       Log.e(TAG, "-----------------【reConnect】----------------")
                       LiteLongSocket.instance.reConnect(false)
                   }
               }
           }
       }
        LiteLongSocket.instance.startHeartBeatTimer()
    }
}