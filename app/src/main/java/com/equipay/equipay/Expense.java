package com.equipay.equipay;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class Expense implements Parcelable {
    String occasionId;
    String expenseId;
    String event;
    String payee;
    double amount;
    ArrayList<String> onBehalfOf;
    String dateOfExpense;

    public Expense() {
    }


    public String getDateOfExpense() {
        return dateOfExpense;
    }

    public void setDateOfExpense(String dateOfExpense) {
        this.dateOfExpense = dateOfExpense;
    }

    public static Creator<Expense> getCREATOR() {
        return CREATOR;
    }


    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    protected Expense(Parcel in) {
        occasionId = in.readString();
        expenseId = in.readString();
        event = in.readString();
        payee = in.readString();
        amount = in.readDouble ();
        onBehalfOf = in.createStringArrayList();
        dateOfExpense = in.readString();
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };
    public String getOccasionId() {
        return occasionId;
    }

    public void setOccasionId(String occasionId) {
        this.occasionId = occasionId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }


    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }


    public ArrayList<String> getOnBehalfOf() {
        return onBehalfOf;
    }

    public void setOnBehalfOf(ArrayList<String> onBehalfOf) {
        this.onBehalfOf = onBehalfOf;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "occasionId='" + occasionId + '\'' +
                ", expenseId='" + expenseId + '\'' +
                ", event='" + event + '\'' +
                ", payee='" + payee + '\'' +
                ", amount=" + amount +
                ", onBehalfOf=" + onBehalfOf +
                ", dateOfExpense='" + dateOfExpense + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(occasionId);
        parcel.writeString(expenseId);
        parcel.writeString(event);
        parcel.writeString(payee);
        parcel.writeDouble (amount);
        parcel.writeStringList(onBehalfOf);
        parcel.writeString(dateOfExpense);
    }
}
