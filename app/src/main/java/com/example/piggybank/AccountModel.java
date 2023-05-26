package com.example.piggybank;

public class AccountModel {
    public AccountModel() {
    }

    public String getBankCardNum() {
        return bankCardNum;
    }

    String bankCardNum;
    String IBAN;
    String ID;

    public void setBalance(int balance) {
        this.balance = balance;
    }

    int balance;
    String clientID;

    public String getIBAN() {
        return IBAN;
    }

    public String getID() {
        return ID;
    }

    public int getBalance() {
        return balance;
    }

    public String getClientID() {
        return clientID;
    }


}
