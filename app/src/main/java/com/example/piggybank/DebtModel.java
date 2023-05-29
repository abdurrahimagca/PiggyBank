package com.example.piggybank;

public class DebtModel {
    public String getID() {
        return ID;
    }

    public Integer getAmount() {
        return amount;
    }

    public int getInstallment() {
        return installment;
    }

    public Boolean getInstallmentStatus() {
        return installmentStatus;
    }

    String ID;
    Integer amount;
    int installment;
    Boolean installmentStatus;
    public DebtModel(){

    }

}
