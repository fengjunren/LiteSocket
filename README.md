# LiteSocket
一个简单的socket客户端。支持长连接和短连接  

长连接可以持续接收响应信息，使用闹钟定时发送心跳包保持通道畅通；短链接可一问一答式收发

## 预览
### long socket：(测试报文：0000000102)
<img src="/img/socketlong.gif" width="70%" height="70%">

  
### short socket：(测试报文：0000000102)
<img src="/img/socket.gif" width="70%" height="70%">
  
## 使用

### LiteSocket

* 初始化
```
 LiteSocket.instance.init(SocketConfig(
            address="10.0.3.2",
            port=60000,
            lenHeader=4, // 总长度用几个字节存储
            connTimeOut=10*1000, // 连接超时时间
            readTimeOut=10*1000, // read超时时间
            dataHandler=MyDataHandler()
        ))
```

* 发送接收
```
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
```

### LiteLongSocket

* 初始化
```
 LiteLongSocket.instance.init(LongSocketConfig(
            address="10.0.3.2",
            port=60001,
            lenHeader=4, // 总长度用几个字节存储
            connTimeOut=10*1000, // 连接超时时间
            heartBeatTime=30*1000,
            heartBeatPack= byteArrayOf(0x55,0x01),
            dataHandler=MyDataHandler()
        ),this)
```
* 发送、接收、取消
```
LiteLongSocket.instance.send(byteArray)
```
```
listenerId= LiteLongSocket.instance.receive(object : OnReceiveListener(){
                override fun eventChanged(msgEvent: MsgEvent) {
                    val recv=Arrays.toString(msgEvent.origin)
                    Log.e(TAG,"【----------【收到数据：${recv}】------------】")
                    toast("收到数据：$recv")
                    setRecord(msgEvent.origin,false)
                }
            })
```
```
LiteLongSocket.instance.cancel(listenerId)
```


### 创建自己的 DataHandler，实现自己的读取协议
```
class MyDataHandler: DataHandler() {
    val Tag="MyDataHandler"
    override fun doReq(data: ByteArray, lenHeader: Int): ByteArray {
        val len = data.size
    ...
    
    override fun doResp(mInputStream: DataInputStream, lenHeader: Int): ByteArray? {
        val dataSize = ByteArray(lenHeader)
        mInputStream?.read(dataSize, 0, lenHeader)
    ...
```
  
    
      
 [socket 调试工具下载](https://raw.githubusercontent.com/fengjunren/LiteSocket/master/tools/SocketTool4.7z)
