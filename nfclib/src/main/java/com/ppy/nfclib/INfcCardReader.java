package com.ppy.nfclib;

import android.content.Intent;

import java.io.IOException;

/**
 * NFC调用相关接口.
 * Created by ZP on 2017/9/20.
 */

public interface INfcCardReader {

    public void onCreate(Intent intent);

    public void onStart();

    public void onResume();

    public void onPause();

    public void onStop();

    public void onDestroy();

    public void onNewIntent(Intent intent);

    public String sendData(byte[] data) throws IOException;

    public String sendData(String hexData) throws IOException;

    public byte[] tranceive(byte[] data) throws IOException;

    public byte[] tranceive(String hexData) throws IOException;

    public boolean isCardConnected();

}
