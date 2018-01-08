# NfcSample
NFC库，兼容4.3之前API以及4.4之后的API，读卡器模式，Sample读取羊城通卡号、余额、交易记录

>无意中从事C++开发的龙腿和我聊起适配，萌发了我想写各种各样的Android适配方面的东西，这个库算是一个开端，适配不同版本(4.4之前和之后)的NFC（读卡器模式）

本库将NFC的CardReader模式在API 4.3以及API 4.4之后不同的注册方式进行统一封装处理，使用本库不必考虑版本API差异，仅按照生命周期调用注册即可。

本库的Sample例子：利用手机NFC读出城市一卡通（羊城通、深圳通）的卡号、余额、交易记录

## 截图
索尼XM50t　　　　　　　　　　　　　　坚果Pro   
![索尼xm50t](http://o7ukzo7vj.bkt.clouddn.com/sony.jpg)
![坚果Pro](http://o7ukzo7vj.bkt.clouddn.com/Smartisan.png)  
华为Mate 8微信读羊城通  
![华为Mate 8](http://o7ukzo7vj.bkt.clouddn.com/Huawei_mate8.png)
![华为Mate 8](http://o7ukzo7vj.bkt.clouddn.com/Huawei_mate8_.png)

## 如何使用本库
1.使用Gradle依赖（先原谅我还没写完这个库，也不会上传）  

```
compile '...'
```

2.直接下载本库然后依赖源码，或者你自己打一个aar包

3.初始化  

本库所有API统一从NfcCardReaderManager调用，NfcCardReaderManager采用建造者，初始化如下：
```java
private NfcCardReaderManager mReaderManager;

private void initNfcCardReader() {
  mReaderManager = new NfcCardReaderManager.Builder()
    .mActivity(this)
    .enableSound(false)//读卡时是否有声音
    .build();
}
```

4.生命周期调用进行注册NFC  
```java  

    @Override
    protected void onResume() {
        super.onResume();
        mReaderManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReaderManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReaderManager.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mReaderManager.onNewIntent(intent);
    }
```

5.传入指令(Apdu),NFC与CPU卡进行交互
```java
  public String sendData(byte[] data) throws IOException;

  public String sendData(String hexData) throws IOException;

  public byte[] tranceive(byte[] data) throws IOException;

  public byte[] tranceive(String hexData) throws IOException;
```

6.判断用户是否已经将卡贴在NFC感应处，可以调用isCardConnected()方法

7.可以在初始化的时候设置监听，监听卡片是否贴在感应处
```java

  private CardOperatorListener mCardOperatorListener = new CardOperatorListener() {
    @Override
    public void onCardConnected(boolean isConnected) {
      if(isConnected) {
        //TODO 卡片已经贴在NFC感应处
      }
    }
  };
	
  @Override
  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNfcCardReader();
        mReaderManager.setOnCardOperatorListener(mCardOperatorListener);
  }
```

## 本库存在的缺陷
1.Sample临时写的，有点乱，没能整理好

2.延迟检测API还未暴露出来，有一些手机NFC会因为感应灵敏度，经常重读(卡片未离开却重新断开又再次连接)，之后会用延迟检测去适配更多的机型或者你有更好的解决重读的方式，联系我，教我~！

3.本库写了两天，只是在索尼XM50t(4.3),锤子坚果Pro(7.1.1),谷歌N6(7.1.1)测试过

## TODO
1.整理好Sanple

2.完善本库，增加延迟检测API

3.考虑解决重读问题

4.完善本库，增加卡模拟相关API

## 感谢
1.潘工  
2.龙腿

## NFC参考链接
[谷歌官网NFC基础知识](https://developer.android.google.cn/guide/topics/connectivity/nfc/nfc.html)  
[谷歌官网NFC进阶知识](https://developer.android.google.cn/guide/topics/connectivity/nfc/advanced-nfc.html)  
[谷歌官网NFC卡模拟](https://developer.android.google.cn/guide/topics/connectivity/nfc/hce.html)
