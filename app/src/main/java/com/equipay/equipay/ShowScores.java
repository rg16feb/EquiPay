package com.equipay.equipay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ShowScores extends AppCompatActivity {
    Occasion occasion;
    EquiPayDatabase myEquiPayDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scores);
        Intent i = getIntent();
        myEquiPayDB = new EquiPayDatabase(this);
        occasion = (Occasion) i.getParcelableExtra("My Occasion");
        HashMap<String, HashMap<String, Float>> accountBook = clearMyAccount(occasion);
        simplifyResults(accountBook);

    }

    private void simplifyResults(HashMap<String, HashMap<String, Float>> accountBook) {
        ArrayList<String> payees = getMyList(accountBook.keySet());
        System.out.println("List of Payees: "+payees);
        for(String payee:payees){
            ArrayList<String> payingFor = getMyList(accountBook.get(payee).keySet());
            HashMap<String, Float> tempMap = new HashMap<>();
            for(String payingForMember:payingFor){
                System.out.println("Getting Details:"+accountBook.get(payee).get(payingForMember) +":"+payee+"----"+payingForMember+":"+accountBook.get(payingForMember).get(payee));
                if(!payee.equalsIgnoreCase(payingForMember)) {
                    if (accountBook.get(payee).get(payingForMember) > accountBook.get(payingForMember).get(payee)) {
                        tempMap = accountBook.get(payee);
                        System.out.println("tempMap:" + tempMap);
                        tempMap.put(payingForMember, (accountBook.get(payee).get(payingForMember) - accountBook.get(payingForMember).get(payee)));
                        accountBook.get(payingForMember).remove(payee);
                    } else if (accountBook.get(payee).get(payingForMember) < accountBook.get(payingForMember).get(payee)) {
                        tempMap = accountBook.get(payingForMember);
                        System.out.println("tempMap:" + tempMap);

                        tempMap.put(payee, (accountBook.get(payingForMember).get(payee) - accountBook.get(payee).get(payingForMember)));
                        accountBook.get(payee).remove(payingForMember);
                    }
                }
                else{
                    continue;
                }
            }
        }
        System.out.println("Account Book(simplifyResults):"+accountBook);
    }

    public ArrayList<String> getMyList(Set<String> set){
        ArrayList<String> keyList = new ArrayList<>();
        for ( String key : set) {
            keyList.add(key);
        }
        return keyList;
    }

    public HashMap<String,HashMap<String,Float>> clearMyAccount(Occasion occasion) {
        HashMap<String, HashMap<String, Float>> accountBook = new HashMap<>();
        ArrayList<String> members = occasion.getMembers();
        ArrayList<Transaction> transactionList = getTransactionDetails(occasion.getOccasionId());
        for (String member : members) {
            HashMap<String, Float> amountOwed = new HashMap<>();
            for (Transaction transaction : transactionList) {
                String payee = transaction.getPayee();
                if (payee.equalsIgnoreCase(member)) {
                    ArrayList<String> onBehalfOf = transaction.getOnBehalfOf();
                    float amt = transaction.getAmount();
                    System.out.println("Amount:" + amt + "transaction Event:" + transaction.getEvent());
                    for (String owesTo : onBehalfOf) {
                        if (!amountOwed.containsKey(owesTo)) {
                            float payableAmtToOwer = amt / onBehalfOf.size();
                            amountOwed.put(owesTo, payableAmtToOwer);
                        } else {
                            float newPayableAmt = amountOwed.get(owesTo) + (amt / onBehalfOf.size());
                            amountOwed.put(owesTo, newPayableAmt);
                        }
                    }
                }
            }
            if(amountOwed.size()!=0) {
                accountBook.put(member, amountOwed);
                for (String membername : members)
                    System.out.println(membername + ":" + amountOwed.get(membername));
            }
        }

        System.out.println("Account Book:" + accountBook);
        return accountBook;
    }



    public ArrayList<Transaction> getTransactionDetails(String occId) {
        ArrayList<Transaction> transactionsList = new ArrayList<>();
        Cursor data = myEquiPayDB.getTransactionsByOccasionId(occId);

        while (data.moveToNext()) {
            Transaction t1 = new Transaction();
            t1.setTransactionId(data.getString(0));
            t1.setEvent(data.getString(1));
            t1.setOccasionId(data.getString(2));
            t1.setPayee(data.getString(3));
            t1.setAmount(data.getInt(4));

            String retrievedMembers = data.getString(5);
            ArrayList<String> results = splitBy(retrievedMembers, ",");
            t1.setOnBehalfOf(results);
            t1.setDateOfTransaction(data.getString(6));
            transactionsList.add(t1);

        }


        return transactionsList;
    }

    private ArrayList<String> splitBy(String retrievedMembers, String s) {
        ArrayList<String> list = new ArrayList<>();
        String[] data = retrievedMembers.split(s);
        for (String i : data) {
            list.add(i);
        }
        return list;
    }
}