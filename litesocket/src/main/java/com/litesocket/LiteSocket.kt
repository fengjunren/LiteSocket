package com.litesocket

import android.os.Handler
import android.os.Looper
import com.litesocket.client.SocketClient
import com.litesocket.model.SocketConfig
import com.serialport.demo.SocketExecutors

class LiteSocket private constructor() {
    private var mHandler: Handler = Handler(Looper.getMainLooper())
    private var mConfig: SocketConfig? = null

//    private val TAG = "LiteSocket"

    private object Holder {
        val INSTANCE = LiteSocket()
    }

    companion object {
        val instance by lazy { Holder.INSTANCE }
    }

    fun start(socketConfig:SocketConfig?=null,sentData: ByteArray, callback: (() -> Unit)? = null): ReqParam {
        val reqParam = ReqParam()
        reqParam.startCallBack = callback
        reqParam.sentData = sentData
        reqParam.liteSocket = this
        reqParam.socketConfig = socketConfig
        return reqParam
    }

    fun start(sentData: ByteArray, callback: (() -> Unit)? = null): ReqParam {
        return  start(null,sentData,callback)
    }

    fun init(config: SocketConfig) {
        mConfig = config
    }

    private fun ok(param: ReqParam) {
        SocketExecutors.execIO(Runnable {
            val client = if(param.socketConfig!=null) SocketClient(param.socketConfig!!) else SocketClient(
                mConfig!!
            )

            try {
                client.open()
                param.startCallBack?.run {
                    mHandler.post{
                        this.invoke()
                    }
                }
                val result = client.sendVar(param.sentData!!)
                if(param.isRecvedOnMainThread){
                    mHandler.post{
                        param.recvCallBack?.invoke(result)
                    }
                }else{
                    param.recvCallBack?.invoke(result)
                }
            } catch (e: Exception) {
                param.errorCallBack?.run {
                    mHandler.post{
                        this.invoke(e)
                    }
                }
            } finally {
                client.close()
                if(param.isCompletedOnMainThread){
                    mHandler.post{
                        param.completeCallBack?.invoke()
                    }
                }else{
                    param.completeCallBack?.invoke()
                }
            }
        })
    }


    class ReqParam {
        var sentData: ByteArray? = null
        var startCallBack: (() -> Unit)? = null
        var recvCallBack: ((ByteArray) -> Unit)? = null
        var completeCallBack: (() -> Unit)? = null
        var errorCallBack: ((Exception) -> Unit)? = null
        var liteSocket: LiteSocket? = null
        var isRecvedOnMainThread: Boolean = true
        var isCompletedOnMainThread: Boolean = true
        var socketConfig:SocketConfig?=null

        fun receive(isOnMainThread: Boolean = true, callback: ((ByteArray) -> Unit)): ReqParam {
            isRecvedOnMainThread = isOnMainThread
            recvCallBack = callback
            return this
        }

        fun onError(callback: ((Exception) -> Unit)): ReqParam {
            errorCallBack = callback
            return this
        }

        fun onComplete(isOnMainThread: Boolean = true, callback: (() -> Unit)): ReqParam {
            isCompletedOnMainThread = isOnMainThread
            completeCallBack = callback
            return this
        }

        fun ok() {
            liteSocket?.ok(this)
        }
    }
}
