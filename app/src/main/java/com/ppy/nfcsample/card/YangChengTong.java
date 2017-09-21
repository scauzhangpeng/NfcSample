package com.ppy.nfcsample.card;

import java.util.List;

/**
 * Created by ZP on 2017/9/21.
 */

public class YangChengTong extends CardInfo {


    private String cardNumber;
    private long balance;
    private List<CardRecord> records;

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

    public List<CardRecord> getRecords() {
        return records;
    }

    public void setRecords(List<CardRecord> records) {
        this.records = records;
    }
}
