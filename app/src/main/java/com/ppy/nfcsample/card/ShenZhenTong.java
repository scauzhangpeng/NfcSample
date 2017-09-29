package com.ppy.nfcsample.card;

import com.ppy.nfclib.Util;

/**
 * Created by ZP on 2017/9/29.
 */

public class ShenZhenTong extends DefaultCardInfo {

    /**
     * 解析卡片信息,未判断9000
     *
     */
    public void parseCardInfo(byte[] src) {
        int number = Util.hexToInt(src, 19, 4);
        cardNumber = String.valueOf(number);
    }

    /**
     * 解析卡片余额
     *
     * @param src 原始16进制数据
     */
    public void parseCardBalance(byte[] src) {
        balance = Util.hexToInt(src, 1, src.length - 2 - 1);
    }
}
