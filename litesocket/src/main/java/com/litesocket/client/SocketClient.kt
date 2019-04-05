package com.litesocket.client

import com.litesocket.model.SocketConfig
import java.io.DataInputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

class SocketClient(private val mConfig: SocketConfig) {

    private val mSocket: Socket = Socket()
    private var mOutputStram: OutputStream? = null
    private var mInputStream: DataInputStream? = null
    private val TAG = "SocketClient"

    fun open() {
        mSocket.soTimeout=mConfig.readTimeOut
        mSocket.connect(InetSocketAddress(mConfig?.address, mConfig?.port), mConfig.connTimeOut)
        mInputStream = DataInputStream(mSocket.getInputStream())
        mOutputStram = mSocket.getOutputStream()
    }

    fun close() {
        mSocket?.let {
            mInputStream?.run {
                close()
            }
            mOutputStram?.run {
                flush()
                close()
            }
            it.close()
        }
    }

    fun sendVar(data: ByteArray): ByteArray {
        sendVar(data, mConfig.lenHeader)
        val bs = receiveVar(mConfig.lenHeader)
        return bs!!
    }

    private fun sendVar(data: ByteArray, lenHeader: Int): Int {
        val sendData=mConfig.dataHandler?.doReq(data,lenHeader)
        mOutputStram?.write(sendData)
        return sendData?.size!!
    }

   private fun receiveVar(lenHeader: Int): ByteArray? {
       return mConfig.dataHandler?.doResp(mInputStream!!,lenHeader)
    }
}
