package com.equipay.equipay;

public class PayerDetails {
    String payerName;
    float amount;

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
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
