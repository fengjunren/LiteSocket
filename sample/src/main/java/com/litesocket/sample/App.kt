package com.litesocket.sample

import android.app.Application
import com.litesocket.LiteLongSocket
import com.litesocket.LiteSocket
import com.litesocket.model.LongSocketConfig
import com.litesocket.model.SocketConfig


class App : Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: App
    }

  override fun onCreate(){
      super.onCreate()
       initSocket()
  }

    private fun initSocket(){
        LiteSocket.instance.init(SocketConfig(
            address="10.0.3.2",
            port=60000,
            lenHeader=4, // 总长度用几个字节存储
            connTimeOut=10*1000, // 连接超时时间
            readTimeOut=10*1000, // read超时时间
            dataHandler=MyDataHandler()
        ))

        LiteLongSocket.instance.init(LongSocketConfig(
            address="10.0.3.2",
            port=60001,
            lenHeader=4, // 总长度用几个字节存储
            connTimeOut=10*1000, // 连接超时时间
            heartBeatTime=30*1000,
            heartBeatPack= byteArrayOf(0x55,0x01),
            dataHandler=MyDataHandler()
        ),this)
    }

}
