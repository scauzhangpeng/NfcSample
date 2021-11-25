package com.ppy.nfclib

import com.ppy.nfclib.exception.ExceptionConstant
/**
 * NFC与CPU卡片交互监听.
 * Created by ZP on 2017/9/20.
 */

interface CardOperatorListener {

    /**
     * CPU卡是否被NFC检测到.
     *
     * @param isConnected true 已连接 false 未连接
     */
    fun onCardConnected(isConnected: Boolean)

    /**
     * NFC异常，例如手机不支持NFC，手机NFC未开启.
     *
     * @param code    异常状态码
     * @param message 异常信息
     */
    fun onException(code: Int, message: String = ExceptionConstant.mNFCException.get(code), executor: Executor? = null)

    /**
     * NFC是否开启.
     * @param stateOn `true` 已开启 <br> `false` 未开启
     */
    fun onNfcEnable(stateOn: Boolean) {

    }

    /**
     * NFC模式为支付模式.
     * 部分手机: 三星手机
     */
    fun onCardPay() {

    }

    /**
     * NFC是否正在打开或者关闭.
     * @param turningOn `true` 正在打开 <br> `false` 正在关闭
     */
    fun onNfcTurning(turningOn: Boolean) {

    }
}
