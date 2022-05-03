package com.equipay.equipay;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class Transaction implements Parcelable {
    String occasionId;
    String transactionId;
    String event;
    String payee;
    float amount;
    ArrayList<String> onBehalfOf;
    String dateOfTransaction;

    public Transaction() {
    }


    public String getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(String dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public static Creator<Transaction> getCREATOR() {
        return CREATOR;
    }


    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }


    protected Transaction(Parcel in) {
        occasionId = in.readString();
        transactionId = in.readString();
        event = in.readString();
        payee = in.readString();
        amount = in.readFloat();
        onBehalfOf = in.createStringArrayList();
        dateOfTransaction = in.readString();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
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


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    public ArrayList<String> getOnBehalfOf() {
        return onBehalfOf;
    }

    public void setOnBehalfOf(ArrayList<String> onBehalfOf) {
        this.onBehalfOf = onBehalfOf;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "occasionId='" + occasionId + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", event='" + event + '\'' +
                ", payee='" + payee + '\'' +
                ", amount=" + amount +
                ", onBehalfOf=" + onBehalfOf +
                ", dateOfTransaction='" + dateOfTransaction + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(occasionId);
        parcel.writeString(transactionId);
        parcel.writeString(event);
        parcel.writeString(payee);
        parcel.writeFloat(amount);
        parcel.writeStringList(onBehalfOf);
        parcel.writeString(dateOfTransaction);
    }
}
