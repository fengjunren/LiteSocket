package com.litesocket.client

import android.print.PrintAttributes
import android.util.Log
import com.litesocket.getStackInfo
import com.litesocket.model.LongSocketConfig
import java.io.DataInputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*

open class LongSocketClient(private val mConfig: LongSocketConfig) {

    private lateinit var mSocket: Socket
    private var mOutputStram: OutputStream? = null
    private var mInputStream: DataInputStream? = null
    private val TAG = "LongSocketClient"
    private val mQueue: Queue<ByteArray> = LinkedList()
    private var mIsStop = false
    private var isConnected = false
    private var mReadThread: Thread? = null
    private var mSendThread: Thread? = null

    private fun open(count: Int) {
        var retryCount:Int = count
        var isInterrupted = false
        try {
            mSocket = Socket()
            mSocket.connect(InetSocketAddress(mConfig?.address, mConfig?.port), mConfig.connTimeOut)
            mInputStream = DataInputStream(mSocket.getInputStream())
            mOutputStram = mSocket.getOutputStream()
            mIsStop=false
            startSend()
            isConnected = true
            retryCount = 0
            startReceive()
        } catch (e: Exception) {
            Log.e(TAG, getStackInfo(e))
            when (e) {
                is InterruptedException -> isInterrupted = true
            }
            Log.e(TAG, "-----------------【isInterrupted：$isInterrupted】----------------")
            isConnected = false
        } finally {
            Log.e(TAG, "-----------------【retryCount：${retryCount+1}】----------------")
            if (!isInterrupted && !isConnected && ++retryCount < 3) {
                Thread.sleep(3 * 1000)
                close()
                open(retryCount)
            }
        }
    }

    fun connect(isRetry:Boolean) {
        mIsStop=true
        if (mReadThread != null) {
            mReadThread?.interrupt()
            mReadThread = null
        }
        mReadThread = Thread {
            var count=if(isRetry)0 else 3
            open(count)
        }
        mReadThread?.start()
    }

    private fun startSend() {
        if (mSendThread != null) {
            mSendThread?.interrupt()
            mSendThread = null
        }
        mSendThread = Thread {
            try {
                while (!mIsStop&&isConnected()) {
                    mQueue.poll()?.run {
                        mConfig.dataHandler?.doReq(this, mConfig.lenHeader)?.run {
                            mOutputStram?.write(this)
                        }
                    }
                    Thread.sleep(30)
                }
            }catch (e:Exception){
                Log.i(TAG,""+getStackInfo(e))
            }finally {
            }
        }
        mSendThread?.start()
    }

    private fun startReceive() {
        while (!mIsStop) {
            mConfig.dataHandler?.doResp(mInputStream!!, mConfig.lenHeader)?.run {
                notifyData(this)
            }
            Thread.sleep(30)
        }
    }

    private fun close() {
        mSocket?.run {
            mInputStream?.run {
                close()
            }
            mOutputStram?.run {
                flush()
                close()
            }
            close()
        }
    }

    fun send(data: ByteArray) {
        mQueue.add(data)
    }

    fun isConnected(): Boolean {
        return isConnected
    }

    open fun notifyData(data: ByteArray) {}
}
