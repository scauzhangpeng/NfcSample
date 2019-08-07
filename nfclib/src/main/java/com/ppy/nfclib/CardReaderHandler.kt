package com.ppy.nfclib

/**
 * 监听回调接口.
 * Created by ZP on 2017/9/29.
 */

interface CardReaderHandler {

    /**
     * 手机不支持NFC.
     */
    fun onNfcNotExit()

    /**
     * 手机支持NFC,但未开启.
     */
    fun onNfcNotEnable()

    /**
     * CPU卡是否被NFC检测到.
     *
     * @param isConnected true 已连接 false 未连接
     */
    fun onCardConnected(isConnected: Boolean)

}
