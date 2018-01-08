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
    public boolean parseCardInfo(byte[] src) {
        try {
            int number = Util.hexToIntLittleEndian(src, 19, 4);
            cardNumber = String.valueOf(number);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 解析卡片余额
     *
     * @param src 原始16进制数据
     */
    public boolean parseCardBalance(byte[] src) {
        try {
            balance = Util.hexToInt(src, 1, src.length - 2 - 1);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
