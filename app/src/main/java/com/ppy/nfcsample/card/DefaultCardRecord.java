package com.ppy.nfcsample.card;

/**
 * Created by ZP on 2017/9/21.
 * <p>
 *     城市一卡通卡片交易记录实体
 * </p>
 */

public class DefaultCardRecord {
    /**
     * 交易类型编码
     */
    private String typeCode;
    /**
     * 交易类型名称
     */
    private String typeName;
    /**
     * 交易金额，单位：分
     */
    private long price;
    /**
     * 交易日期：yyyyMMddHHmmss
     */
    private String date;
    /**
     * 交易序列号
     */
    private String serialNumber;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void readRecord(byte[] resp) {
        String record = Commands.ByteArrayToHexString(resp);
        serialNumber = record.substring(0,4);
        price = Integer.parseInt(record.substring(10, 18), 16);
        typeCode = record.substring(18, 20);
        if ("09".equals(typeCode)) {
            typeName = "消费";
        } else {
            typeName = "充值";
        }
        date = record.substring(36, 46);
    }
}
