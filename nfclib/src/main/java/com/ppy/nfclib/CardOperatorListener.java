package com.ppy.nfclib;

/**
 * Created by ZP on 2017/9/20.
 * NFC与CPU卡片交互监听
 */

public interface CardOperatorListener {

    /**
     * CPU卡是否被NFC检测到
     * @param isConnected true 已连接 false 未连接
     */
    void onCardConnected(boolean isConnected);
}
