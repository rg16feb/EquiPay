package com.equipay.equipay;

public class ExpenseTable {
    final String TableName = "Expenses";
    final String ExpenseId = "ExpenseID";
    final String occasionIdFK = "OccasionID";
    final String ExpenseEvent = "ExpenseEvent";
    final String payee = "Payee";
    final String amount = "Amount";
    final String behalfOf = "BehalfOf";
    final String dateOfExpense = "DateOfExpense";

    public String getExpenseEvent() {
        return ExpenseEvent;
    }
    public String getOccasionIdFK() {
        return occasionIdFK;
    }
    public String getTableName() {
        return TableName;
    }

    public String getExpenseId() {
        return ExpenseId;
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

    public String getDateOfExpense() {
        return dateOfExpense;
    }
}
