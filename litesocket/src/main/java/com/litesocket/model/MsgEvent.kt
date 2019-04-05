package com.litesocket.model


data class MsgEvent(val origin:ByteArray) {

    fun getMsg():String{
        return if (origin!=null)String(origin)else ""
    }

}