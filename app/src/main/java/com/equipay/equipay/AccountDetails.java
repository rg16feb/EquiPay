package com.equipay.equipay;

public class AccountDetails {
    String personWhoOwes;
    String personWhoWillGet;
    double amount;

    public String getPersonWhoOwes() {
        return personWhoOwes;
    }

    public void setPersonWhoOwes(String personWhoOwes) {
        this.personWhoOwes = personWhoOwes;
    }

    public String getPersonWhoWillGet() {
        return personWhoWillGet;
    }

    public void setPersonWhoWillGet(String personWhoWillGet) {
        this.personWhoWillGet = personWhoWillGet;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AccountDetails{" +
                "personWhoOwes='" + personWhoOwes + '\'' +
                ", personWhoWillGet='" + personWhoWillGet + '\'' +
                ", amount=" + amount +
                '}';
    }
}
