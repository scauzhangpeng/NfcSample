package com.ppy.nfclib;

import android.app.Activity;
import android.content.Intent;

import java.io.IOException;

/**
 * Created by ZP on 2017/9/20.
 * NFC调用相关接口
 */

public interface INfcCardReader {

    public void onStart(Activity activity);

    public void onResume();

    public void onPause();

    public void onDestroy();

    public void onNewIntent(Intent intent);

    public String sendData(byte[] data) throws IOException;

    public String sendData(String hexData) throws IOException;

    public byte[] tranceive(byte[] data) throws IOException;

    public byte[] tranceive(String hexData) throws IOException;

    public boolean isCardConnected();

}
