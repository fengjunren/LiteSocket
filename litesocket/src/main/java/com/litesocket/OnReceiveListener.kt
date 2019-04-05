package com.litesocket

import com.litesocket.model.MsgEvent
import java.util.*


open abstract class OnReceiveListener {
    private var id:String=""
    private var isCancel: Boolean = false


    init {
        id= UUID.randomUUID().toString()
    }

    fun cancel() {
        isCancel = true
    }

    fun isCancel(): Boolean {
        return isCancel
    }

    fun getId(): String {
        return id
    }

    abstract fun eventChanged(msgEvent: MsgEvent)

     fun isOnMainThread():Boolean{
         return true
     }

}
