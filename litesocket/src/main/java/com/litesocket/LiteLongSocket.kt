package com.litesocket

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.litesocket.client.EchoClient
import com.litesocket.model.LongSocketConfig

class LiteLongSocket private constructor(){

    private var mConfig: LongSocketConfig? = null
    private lateinit var echoClient: EchoClient
    private val TAG="LiteLongSocket"
    private var alarm: AlarmManager? = null
    private var sender: PendingIntent?=null
    private lateinit var mCtx:Application

    private object Holder {
        val INSTANCE = LiteLongSocket()
    }

    companion object {
        val instance by lazy { Holder.INSTANCE }
    }

    fun init(config: LongSocketConfig,ctx:Application) {
        mCtx=ctx
        mConfig = config
        echoClient= EchoClient(config, ctx)
        echoClient.connect(true)
        startHeartBeatTimer()
    }

    fun reConnect(isRetry:Boolean){
        echoClient?.connect(isRetry)
    }

    fun receive(listener:OnReceiveListener):String{
        listener?.run {
            echoClient.addReceiveListener(listener)
        }
        return listener.getId()
    }

    fun send(data:ByteArray){
        echoClient.send(data)
    }

    fun sendHearBeat(){
        mConfig?.heartBeatPack?.let {
            send(it)
        }?: Log.i(TAG,"-----【heartBeatPack is null , Please init heartBeatPack first!!】--------")
    }


     fun startHeartBeatTimer() {
        if (null == alarm)
            alarm = mCtx.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent= Intent("com.litesocket.ReceiveHearBeat.sendHeartBeat")
        if (null == sender)sender= PendingIntent.getBroadcast(mCtx, 1, intent, 0)

        alarm?.cancel(sender)

        val intervalMillis:Long=mConfig?.heartBeatTime!!
        val triggerAtMillis=System.currentTimeMillis()+intervalMillis
        alarm?.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender)

    }

    fun cancel(id:String):Boolean{
        return echoClient.cancelListener(id)
    }

    fun getConfig():LongSocketConfig{
        return mConfig!!
    }

    fun getLastReceiveTime():Long{
        return echoClient.getLastReceiveTime()
    }
}