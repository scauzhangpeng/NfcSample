package com.ppy.nfcsample.card;

import com.ppy.nfclib.Util;

/**
 * Created by ZP on 2017/9/21.
 */

public class YangChengTong extends DefaultCardInfo {

    /**
     * 解析卡片信息,未判断9000
     *
     * @throws Exception {@link IndexOutOfBoundsException}
     */
    public void parseCardInfo(String src) throws Exception{
        cardNumber = src.substring(22, 32);
        effectiveDate = src.substring(46, 54);
        expiredDate = src.substring(54, 62);
    }

    /**
     * 解析卡片余额
     *
     * @param src 原始16进制数据
     */
    public void parseCardBalance(byte[] src) {
        balance = Util.hexToInt(src, 0, src.length - 2);
    }
}
