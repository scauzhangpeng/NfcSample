package com.ppy.nfcsample.card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZP on 2017/9/21.
 * <p>
 *     城市一卡通卡片实体
 * </p>
 */

public class DefaultCardInfo {

    /**
     * 卡片类型，例如：羊城通，深圳通
     */
    protected int type;

    /**
     *  卡号
     */
    protected String cardNumber;
    /**
     *  卡片版本
     */
    protected int version;
    /**
     * 卡片余额
     */
    protected long balance;
    /**
     * 卡片生效日期
     */
    protected String effectiveDate;
    /**
     * 卡片失效日期
     */
    protected String expiredDate;
    /**
     * 卡片交易记录
     */
    protected List<DefaultCardRecord> records = new ArrayList<>(16);

    public String getCardNumber() {
        return cardNumber;
    }

    public int getVersion() {
        return version;
    }

    public long getBalance() {
        return balance;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public List<DefaultCardRecord> getRecords() {
        return records;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
