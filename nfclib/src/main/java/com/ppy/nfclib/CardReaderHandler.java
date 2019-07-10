package com.ppy.nfclib;

/**
 * Created by ZP on 2017/9/29.
 */

public interface CardReaderHandler {

    /**
     * 手机不支持NFC.
     */
    void onNfcNotExit();

    /**
     * 手机支持NFC,但未开启.
     */
    void onNfcNotEnable();

    /**
     * CPU卡是否被NFC检测到.
     *
     * @param isConnected true 已连接 false 未连接
     */
    void onCardConnected(boolean isConnected);

}
