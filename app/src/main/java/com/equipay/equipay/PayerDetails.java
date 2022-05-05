package com.equipay.equipay;

public class PayerDetails {
    String payerName;
    double amount;

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PayerDetails{" +
                "payerName='" + payerName + '\'' +
                ", amount=" + amount +
                '}';
    }
}
