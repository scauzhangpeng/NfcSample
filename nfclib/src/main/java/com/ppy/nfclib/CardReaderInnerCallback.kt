package com.ppy.nfclib

import com.ppy.nfclib.util.MiUtil

/**
 * 监听回调接口.
 * Created by ZP on 2017/9/29.
 */

interface CardReaderInnerCallback {

    /**
     * 手机不支持NFC.
     */
    fun onNfcNotExit()

    /**
     * 手机支持NFC,但未开启.
     */
    fun onNfcNotEnable()

    /**
     * NFC权限回调.
     */
    fun onNfcPermission(@MiUtil.PermissionResult permission: Int)

    /**
     * CPU卡是否被NFC检测到.
     *
     * @param isConnected true 已连接 false 未连接
     */
    fun onCardConnected(isConnected: Boolean)

    fun onException(exception: Exception)

}
