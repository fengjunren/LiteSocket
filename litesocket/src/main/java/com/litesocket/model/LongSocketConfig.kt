package com.litesocket.model

import com.litesocket.DataHandler


data class LongSocketConfig(
    var address:String = "10.0.3.2",
    var port:Int = 60001,
    var lenHeader:Int=4, // 总长度用几个字节存储
    var connTimeOut:Int=10*1000, // 连接超时时间
    var heartBeatTime:Long=30*1000, // 心跳间隔时间 单位ms
    var heartBeatPack:ByteArray?=null,
    var dataHandler: DataHandler?=null
)
