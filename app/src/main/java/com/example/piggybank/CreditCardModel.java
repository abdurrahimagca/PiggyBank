package com.example.piggybank;

public class CreditCardModel {
    String ID;
    Integer cardLimit;
    String cardNum;

    public Integer getMin() {
        return min;
    }

    Integer min;

    public Integer getCardID() {
        return cardID;
    }

    Integer cardID;

    public String getID() {
        return ID;
    }

    public Integer getCardLimit() {
        return cardLimit;
    }

    public String getCardNum() {
        return cardNum;
    }

    public String getExpDate() {
        return expDate;
    }

    public Integer getCVV() {
        return CVV;
    }

    public Integer getDebt() {
        return debt;
    }

    public Integer getPin() {
        return pin;
    }

    String expDate;
    Integer CVV;
    Integer debt;
    Integer pin;
    public CreditCardModel(){}



}

