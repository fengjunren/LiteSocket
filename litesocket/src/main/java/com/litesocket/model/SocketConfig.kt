package com.litesocket.model

import com.litesocket.DataHandler

class SocketConfig(
    var address:String = "10.0.3.2",
    var port:Int = 60000,
    var lenHeader:Int=4 ,// 总长度用几个字节存储
    var connTimeOut:Int=10*1000, // 连接超时时间
    var readTimeOut:Int=10*1000, // read超时时间
    var dataHandler:DataHandler?=null

)
