package com.ppy.nfcsample.card;

import java.util.List;

/**
 * Created by ZP on 2017/9/21.
 * <p>
 *     城市一卡通卡片实体
 * </p>
 */

public class DefaultCardInfo {

    /**
     *  卡号
     */
    private String cardNumber;
    /**
     *  卡片版本
     */
    private int version;
    /**
     * 卡片余额
     */
    private long balance;
    /**
     * 卡片生效日期
     */
    private String effectiveDate;
    /**
     * 卡片失效日期
     */
    private String expiredDate;
    /**
     * 卡片交易记录
     */
    private List<DefaultCardRecord> records;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public List<DefaultCardRecord> getRecords() {
        return records;
    }

    public void setRecords(List<DefaultCardRecord> records) {
        this.records = records;
    }
}
