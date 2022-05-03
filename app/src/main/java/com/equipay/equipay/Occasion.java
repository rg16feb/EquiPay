package com.equipay.equipay;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Occasion implements Parcelable {
    public void setOccasionId(String occasionId) {
        this.occasionId = occasionId;
    }

    public String getOccasionId() {
        return occasionId;
    }

    String occasionId;
    String occasionName;
    ArrayList<String> members;
    ArrayList<Transaction> transactionsList;
    String createdOnDate;


    public Occasion(){}

    protected Occasion(Parcel in) {
        occasionId = in.readString();
        occasionName = in.readString();
        createdOnDate = in.readString();
        members = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(occasionId);
        dest.writeString(occasionName);
        dest.writeString(createdOnDate);
        dest.writeStringList(members);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Occasion> CREATOR = new Creator<Occasion>() {
        @Override
        public Occasion createFromParcel(Parcel in) {
            return new Occasion(in);
        }

        @Override
        public Occasion[] newArray(int size) {
            return new Occasion[size];
        }
    };

    public void setOccasionName(String occasion) {
        this.occasionName = occasion;
    }
    public String getOccasionName() {
        return occasionName;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setTransactionsList(ArrayList<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
    }

    public void setCreatedOnDate(String createdOnDate) {
        this.createdOnDate = createdOnDate;
    }

    public String getCreatedOnDate() {
        return createdOnDate;
    }

    public ArrayList<Transaction> getTransactionsList() {
        return transactionsList;
    }


    @Override
    public String toString() {
        return "Occasion{" +
                "occasionId='" + occasionId + '\'' +
                ", occasionName='" + occasionName + '\'' +
                ", members=" + members +
                ", transactionsList=" + transactionsList +
                ", createdOnDate='" + createdOnDate + '\'' +
                '}';
    }
}
