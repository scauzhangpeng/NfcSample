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

    /**
     * NFC异常，例如手机不支持NFC，手机NFC未开启
     *
     * @param code 异常状态码 {@link ExceptionConstant}
     * @param message 异常信息 {@link ExceptionConstant}
     */
    void onException(int code, String message);
}
