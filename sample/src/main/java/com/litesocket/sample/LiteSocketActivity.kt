package com.litesocket.sample

import android.os.Bundle
import android.util.Log
import com.litesocket.utils.ByteUtil
import com.litesocket.LiteSocket
import com.litesocket.getStackInfo
import kotlinx.android.synthetic.main.activity_litesocket.*

import java.io.IOException
import java.net.BindException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*



class LiteSocketActivity : BaseActivity() {
    private val TAG="MainActivity"

    override fun getLayoutId(): Int {
        return R.layout.activity_litesocket
    }

    override fun initListener() {
        super.initListener()
        btnSend.setOnClickListener {
            val txtCmd=etCmd.text.trim()
            if(txtCmd.isEmpty()){
                toast("请输入要发送的指令")
            }else{
//                val ba=byteArrayOf(0x03, 0x04, 0x0C, 0x00, 0x01)
                val ba= ByteUtil.HexToByteArr(txtCmd.toString())
                send(ba)
            }
        }


    }

    override fun bindData(savedInstanceState: Bundle?) {
        super.bindData(savedInstanceState)
        Log.e(TAG,"【----------------------】")

    }

    private fun send(ba: ByteArray){
        LiteSocket.instance.start(ba){
            Log.e(TAG,"【----------start------------】")
            setRecord(ba,true)
        }.receive{
            res->
            run {
            Log.e(TAG,"【----------received thread------------】"+Thread.currentThread().name)
            setRecord(res,false)
        }
        }.onError {
            e->
            run {
                Log.e(TAG,"【----------------------】"+getStackInfo(e))
                when (e) {
                    is SocketTimeoutException -> toast("响应超时 SocketTimeoutException")
                    is IOException -> toast("通信异常 IOException")
                    is UnknownHostException -> toast("连接异常 UnknownHostException")
                    is BindException -> toast("连接异常 BindException")
                    else -> toast("未知错误!")
                }
            }
        }.onComplete{
            Log.e(TAG,"【----------onComplete------------】")
            Log.e(TAG,"【----------onComplete thread------------】"+Thread.currentThread().name)
        }.ok()
    }

    private fun setStatus(txt:String){
        runOnUiThread {
            tvStatus.text = "状态: "+txt
        }
    }

    private fun setRecord(txt:ByteArray, isSend:Boolean){
         setRecord(ByteUtil.ByteArrToHex(txt),isSend)
    }

    private fun setRecord(txt:String, isSend:Boolean){
            var direction="接收："
            if(isSend){
                direction="发送："
            }
            val s=tvRecord.text.toString()+"\n"+direction+" "+txt+"            "+SimpleDateFormat("HH:mm:ss").format(Date())
            tvRecord.text=s
    }
}
