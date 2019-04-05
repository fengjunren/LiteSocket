package com.litesocket.sample

import android.os.Bundle
import android.util.Log
import com.litesocket.*
import com.litesocket.model.MsgEvent
import com.litesocket.utils.ByteUtil
import kotlinx.android.synthetic.main.activity_litelongsocket.*
import java.text.SimpleDateFormat
import java.util.*


class LiteLongSocketActivity : BaseActivity() {
    private val TAG="MainActivity"
    private lateinit var listenerId: String

    override fun getLayoutId(): Int {
        return R.layout.activity_litelongsocket
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

        btnListener.setOnClickListener {
            LiteLongSocket.instance.cancel(listenerId)
            listenerId= LiteLongSocket.instance.receive(object : OnReceiveListener(){
                override fun eventChanged(msgEvent: MsgEvent) {
                    val recv=Arrays.toString(msgEvent.origin)
                    Log.e(TAG,"【----------【收到数据：${recv}】------------】")
                    toast("收到数据：$recv")
                    setRecord(msgEvent.origin,false)
                }
            })
        }
        btnCancel.setOnClickListener {
            LiteLongSocket.instance.cancel(listenerId)
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
        super.bindData(savedInstanceState)
        Log.e(TAG,"【----------------------】")
        listenerId= LiteLongSocket.instance.receive(object : OnReceiveListener(){
            override fun eventChanged(msgEvent: MsgEvent) {
                val recv=Arrays.toString(msgEvent.origin)
                Log.e(TAG,"【----------【收到数据：${recv}】------------】")
                toast("收到数据：$recv")
                setRecord(msgEvent.origin,false)
            }
        })

    }

    private fun send(ba: ByteArray){
        setRecord(ba,true)
        LiteLongSocket.instance.send(ba)
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
