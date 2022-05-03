package com.equipay.equipay;

public class TransactionTable {
    final String TableName = "Transactions";
    final String transactionId = "TransactionID";
    final String occasionIdFK = "OccasionID";
    final String transactionEvent = "transactionEvent";
    final String payee = "Payee";
    final String amount = "Amount";
    final String behalfOf = "BehalfOf";
    final String dateOfTransaction = "DateOfTransaction";

    public String getTransactionEvent() {
        return transactionEvent;
    }
    public String getOccasionIdFK() {
        return occasionIdFK;
    }
    public String getTableName() {
        return TableName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getPayee() {
        return payee;
    }

    public String getAmount() {
        return amount;
    }

    public String getBehalfOf() {
        return behalfOf;
    }

    public String getDateOfTransaction() {
        return dateOfTransaction;
    }
}
