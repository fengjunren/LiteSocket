package com.litesocket

import java.io.DataInputStream

open abstract class DataHandler{
    abstract fun doReq(data: ByteArray, lenHeader: Int): ByteArray
    abstract fun doResp(mInputStream: DataInputStream, lenHeader: Int): ByteArray?
}