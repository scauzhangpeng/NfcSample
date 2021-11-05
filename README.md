# NfcSample
NFC库，兼容4.3之前API以及4.4之后的API，读卡器模式，Sample读取羊城通卡号、余额、交易记录

[ ![NFClib](https://img.shields.io/badge/nfccompat-2.0.0-blue) ](https://repo1.maven.org/maven2/io/github/scauzhangpeng/nfccompat/)

本库将NFC的CardReader模式在API 4.3以及API 4.4之后不同的注册方式进行统一封装处理，使用本库不必考虑版本API差异，内部lifecycle管理nfc生命周期。

本库的Sample例子：利用手机NFC读出城市一卡通（羊城通、深圳通）的卡号、余额、交易记录

## 截图
|sony xm50t(4.3)|oppo A37m(5.1)|锤子 坚果pro(7.1.1)|
|---------------|--------------|------------------|
|<img src="https://s1.ax1x.com/2020/07/22/UHMZ6K.jpg" width="300"/>|<img src="https://s1.ax1x.com/2020/07/22/UHMFYR.png" width="300"/>|<img src="https://s1.ax1x.com/2020/07/22/UHMVl6.png" width="300"/>|
|nexus 6(5.0)|mate8 QQ-卡号|mate8 QQ-记录|
|<img src="https://s1.ax1x.com/2020/07/22/UHM9w4.png" width="300"/>|<img src="https://s1.ax1x.com/2020/07/22/UHMeOO.png" width="300"/>|<img src="https://s1.ax1x.com/2020/07/22/UHMik9.png" width="300"/>|
## 如何使用本库
1.使用Gradle依赖

```
implementation 'io.github.scauzhangpeng:nfccompat:2.0.0'
```

2.初始化

本库所有API统一从NfcManagerCompat调用，NfcCardReaderManager采用具名参数+默认值，初始化如下：
```kotlin
protected lateinit var mReaderManager: NfcManagerCompat

private fun initNfcCardReader() {
        mReaderManager = NfcManagerCompat(activity = this,//上下文
            cardOperatorListener = mCardOperatorListener,//回调监听
            printer = LoggerImpl(),//日志打印
            enableSound = true)//检测到卡片时是否有系统声音
    }
```

3.生命周期调用进行注册NFC  
```kotlin

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            initNfcCardReader()//调用初始化
        }

        override fun onNewIntent(intent: Intent) {
            super.onNewIntent(intent)
            mReaderManager.onNewIntent(intent)//在API 4.3前台调度读卡需要调用
        }
```
4.初始化的时候设置监听，一旦卡片贴紧NFC感应处，将回调onCardConnected
```kotlin

  private val mCardOperatorListener = object : CardOperatorListener {//检测到卡片
          override fun onCardConnected(isConnected: Boolean) {
              Log.d(TAG, "onCardConnected: isConnected = $isConnected")
              //TODO: doOnCardConnected(isConnected)
          }

          override fun onException(code: Int, message: String) {//Nfc或读卡异常
              Log.d(TAG, "onException: code = $code,message = $message")
              //TODO: doOnException(code, message)
          }

          override fun onCardPay() {//nfc 支付模式
              super.onCardPay()
              Log.d(TAG, "onCardPayMode: ")
              //TODO: doOnNfcOff()
          }

          override fun onNfcEnable(stateOn: Boolean) {// nfc是否开启
              super.onNfcEnable(stateOn)
              Log.d(TAG, "onNfcEnable: $stateOn")
              if (!stateOn) {
                  //TODO: doOnNfcOff()
              } else {
                  //TODO: doOnNfcOn()
              }
          }

          override fun onNfcTurning(turningOn: Boolean) {// nfc是否正在打开或正在关闭
              super.onNfcTurning(turningOn)
              Log.d(TAG, "onNfcTurning: $turningOn")
          }
      }
}
```

5.当回调NFC已读取卡片标签，则可以调用NfcCardReaderManager如下方法进行指令(APDU)操作。
```kotlin
    @Throws(IOException::class)
    fun sendData(data: ByteArray): String

    @Throws(IOException::class)
    fun sendData(hexData: String): String {
        return sendData(Util.hexStringToByteArray(hexData))
    }

    @Throws(IOException::class)
    fun tranceive(data: ByteArray): ByteArray

    @Throws(IOException::class)
    fun tranceive(hexData: String): ByteArray {
        return tranceive(Util.hexStringToByteArray(hexData))
    }
```

6.判断用户是否已经将卡贴在NFC感应处，可以调用NfcManagerCompat的isCardConnected()方法

7.相关辅助工具类

|作用|方法|
|----|----|
|设备是否提供NFC|Util.isNfcExits|
|设备是否开启NFC|Util.isNfcEnable|
|跳转NFC设置界面|Util.intentToNfcSetting|
|设备是否提供AndroidBeam|Util.isAndroidBeamExits|
|设备是否开启AndroidBeam|Util.isAndroidBeamEnable|
|跳转AndroidBeam设置界面|Util.intentToNfcShare|
|金额分转元|Util.toAmountString|
|16进制转10进制|Util.hexToInt|
|16进制转10进制(小端模式)|Util.hexToIntLittleEndian|


## 特别说明几点
 - ~~有一些机型会贴卡后一直回调Tag，导致一直重读。曾经出现过的机型三星S3，Android 4.3~~

 - NFC跳转至设置界面，锤子坚果pro，需要跳转到Android Beam，不清楚是否锤子系统都这样。测试版本smartisan v3.5.2

 - ~~OPPO A37m机型需要同时开启NFC以及Android Beam，论坛也有人反馈，但是目前本人测试后并不需要，但是QQ读卡需要同时开启，目前不清楚。
 (版本号：A37m_11_A.23_171025,内核版本：3.10.72-G201710251842,基带版本：MOLY.LR11.W1539.MD.TC16.JAD.SP.V1.P31.T47,2017/10/13 16:19)~~
 <img src = "https://s1.ax1x.com/2020/07/22/UHMkf1.png" width = "900"/>

 - 已测试机型如下表：

|机型|版本|ROM版本|
|----|---|-------|
|索尼XM50t|4.3|~|
|oppo A35|5.1|colorOs v3.0|
|锤子坚果pro|7.1.1|smartisan v3.5.2|
|谷歌Nexus 6|5.0/5.1.1/6.0.1/7.1.1|~|
|华为Mate 8|7.1.1|EMUI 5.0|
|华为p20 pro|8.1.0|EMUI 8.1.0|
|小米note2|8.0|MIUI 10.0|
|魅族16s|9.0|Flyme 7.3.0.0A|
|华为Mate 40 Pro|Harmony2.0.0|Harmony2.0.0|
|....希望大家一起测试完善|....|....|

## 感谢
1.潘工  
2.龙腿

## NFC参考链接
[谷歌官网NFC基础知识](https://developer.android.google.cn/guide/topics/connectivity/nfc/nfc.html)  
[谷歌官网NFC进阶知识](https://developer.android.google.cn/guide/topics/connectivity/nfc/advanced-nfc.html)  
[谷歌官网NFC卡模拟](https://developer.android.google.cn/guide/topics/connectivity/nfc/hce.html)

## 联系作者
本库关注的人并不多，目前也处于稳定使用，不定期佛性更新状态，如有bug或者需求不满足需要改的可以联系我  
写这个库由于之前工作中经常用NFC读写ETC卡(粤通卡、赣通卡、蒙通卡等)，也短暂搞过读银行卡卡号，读城市交通卡(羊城通、深圳通)都是我看一些资料后写的。  
博客：https://blog.csdn.net/scau_zhangpeng/article/details/70162775  
Email：scau_zhangpeng@163.com  

|微信|QQ|
|----|----|
|<img src = "https://s1.ax1x.com/2020/07/22/UHMCTJ.jpg" width = "200"/>|<img src = "https://s1.ax1x.com/2020/07/22/UHMESx.jpg" width = "200"/>|
