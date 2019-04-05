package com.litesocket.sample

import android.util.Log
import com.litesocket.utils.ByteUtil
import com.litesocket.DataHandler
import com.litesocket.utils.Utils
import java.io.DataInputStream

class MyDataHandler: DataHandler() {
    val Tag="MyDataHandler"
    override fun doReq(data: ByteArray, lenHeader: Int): ByteArray {
        val len = data.size
        val dataSize: ByteArray = if (lenHeader == 4)
            Utils.toByte(len)
        else
            Utils.toByte(len.toLong())
        val sendData = ByteArray(lenHeader + len)
        System.arraycopy(dataSize, 0, sendData, 0, lenHeader)
        System.arraycopy(data, 0, sendData, lenHeader, len)
        return sendData
    }

    override fun doResp(mInputStream: DataInputStream, lenHeader: Int): ByteArray? {
        val dataSize = ByteArray(lenHeader)
        mInputStream?.read(dataSize, 0, lenHeader)
        val length = ByteUtil.byteToInt2(dataSize)
        if(length>=1*1024*1024||length<0){
          Log.i(Tag,"-------------------length is too long will OOM or length is Negative.length is :---------------$length")
            return null
        }else if (length==0)return null
        var left = length
        var total = 0
        var bs = ByteArray(length)
        var readed: Int
        while (left > 0&&mInputStream.available()>0) {
                readed = mInputStream?.read(bs, total, left)!!
                if (readed > 0) {
                    total += readed
                    left -= readed
                    continue
                }
            break
        }
        return  bs
    }

}