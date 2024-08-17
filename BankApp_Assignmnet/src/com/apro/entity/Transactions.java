package com.apro.entity;

import java.sql.Timestamp;

public class Transactions {
    private int transactionId;
    private long fromAccount;
    private long toAccount;
    private String transactionType;
    private double amount;
    private Timestamp transactionDate;

    // Constructor
    public Transactions(int transactionId, long fromAccount, long toAccount, String transactionType, double amount, Timestamp transactionDate) {
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }
    
    public Transactions() {
    	
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public long getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(long fromAccount) {
        this.fromAccount = fromAccount;
    }

    public long getToAccount() {
        return toAccount;
    }

    public void setToAccount(long toAccount) {
        this.toAccount = toAccount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    
}
