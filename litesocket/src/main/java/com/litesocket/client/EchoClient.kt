package com.litesocket.client

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.litesocket.OnReceiveListener
import com.litesocket.model.LongSocketConfig
import com.litesocket.model.MsgEvent

class EchoClient(mConfig: LongSocketConfig, val ctx:Application) : LongSocketClient(mConfig){
    private var mListeners: HashMap<String, OnReceiveListener> = hashMapOf()
    private var mHandler: Handler = Handler(Looper.getMainLooper())
    private var lastReceiveTime:Long = System.currentTimeMillis()

    private val TAG = "EchoClient"

    private fun fireChanged(data: ByteArray) {
        for (item in mListeners){
            if(!item.value.isCancel()){
                if(item.value.isOnMainThread()){
                    mHandler.post{
                        item.value.eventChanged(MsgEvent(data))
                    }
                }else{
                    item.value.eventChanged(MsgEvent(data))
                }
            }
        }
    }

    override fun notifyData(data: ByteArray) {
        super.notifyData(data)
        lastReceiveTime=System.currentTimeMillis()
        fireChanged(data)
    }

    fun addReceiveListener(listener: OnReceiveListener){
        mListeners[listener.getId()] = listener
    }
    fun cancelListener(id:String):Boolean{
        var isSucc = false
        mListeners[id]?.run {
            cancel()
            isSucc = true
        }
        return isSucc
    }

    fun getLastReceiveTime():Long{
        return lastReceiveTime
    }
}