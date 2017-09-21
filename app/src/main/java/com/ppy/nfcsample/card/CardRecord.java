package com.ppy.nfcsample.card;

/**
 * Created by ZP on 2017/9/21.
 */

public class CardRecord {

    private String typeCode;
    private String typeName;
    private long price;
    private String date;
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
