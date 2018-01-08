package com.ppy.nfcsample.card;

import com.ppy.nfclib.Util;

/**
 * Created by ZP on 2017/9/21.
 */

public class YangChengTong extends DefaultCardInfo {

    /**
     * 解析卡片信息
     *
     */
    public boolean parseCardInfo(byte[] resp) {
        try {
            String src = Util.ByteArrayToHexString(resp);
            cardNumber = src.substring(22, 32);
            effectiveDate = src.substring(46, 54);
            expiredDate = src.substring(54, 62);
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
            balance = Util.hexToInt(src, 0, src.length - 2);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
