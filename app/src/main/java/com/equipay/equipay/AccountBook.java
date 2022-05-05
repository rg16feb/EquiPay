package com.equipay.equipay;

import java.util.ArrayList;

public class AccountBook {
    String personWhoOwes;
    ArrayList<AccountDetails> accountDetailsForPersonWhoOwes;

    public String getPersonWhoOwes() {
        return personWhoOwes;
    }

    public void setPersonWhoOwes(String personWhoOwes) {
        this.personWhoOwes = personWhoOwes;
    }

    public ArrayList<AccountDetails> getAccountDetailsForPersonWhoOwes() {
        return accountDetailsForPersonWhoOwes;
    }

    public void setAccountDetailsForPersonWhoOwes(ArrayList<AccountDetails> accountDetailsForPersonWhoOwes) {
        this.accountDetailsForPersonWhoOwes = accountDetailsForPersonWhoOwes;
    }

    @Override
    public String toString() {
        return "AccountBook{" +
                "personWhoOwes='" + personWhoOwes + '\'' +
                ", accountDetailsForPersonWhoOwes=" + accountDetailsForPersonWhoOwes +
                '}';
    }
}
