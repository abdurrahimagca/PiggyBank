package com.example.piggybank;

public class BankCardModel {
    public String getIBAN() {
        return IBAN;
    }

    public Integer getCardID() {
        return cardID;
    }

    Integer cardID;
    public int getCVV() {
        return CVV;
    }

    public String getID() {
        return ID;
    }

    public String getCardNum() {
        return cardNum;
    }

    public String getExpDate() {
        return expDate;
    }

    String IBAN;
    int CVV;
    String ID;
    String cardNum;
    String expDate;
    public BankCardModel(){

    }


}
