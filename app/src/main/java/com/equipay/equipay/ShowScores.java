package com.equipay.equipay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import java.lang.Double ;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.equipay.equipay.databinding.ActivityShowScoresBinding;

import java.util.ArrayList;
import java.util.Set;

public class ShowScores extends AppCompatActivity {
    Occasion occasion;
    EquiPayDatabase myEquiPayDB;
    ListView payerDetails;
    TextView occasionName;
    double zero;
    Button gotToOccasionsBtn,goToExpensesBtn;
    ActivityShowScoresBinding binding;
    ListAdapterAccountBook listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scores);
        binding = ActivityShowScoresBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        payerDetails = findViewById(R.id.payerDetails);
        occasionName = findViewById(R.id.occasion_name_text_view);
        gotToOccasionsBtn = findViewById(R.id.go_to_my_occasion_show_scores);
        goToExpensesBtn = findViewById(R.id.go_to_Expenses_show_scores);

        Intent i = getIntent();
        myEquiPayDB = new EquiPayDatabase(this);
        occasion = (Occasion) i.getParcelableExtra("My Occasion");
        ArrayList<AccountDetails> accountBook = createAccountBook(occasion);

        accountBook = settleAccountBook(accountBook,occasion);

        ArrayList<AccountDetails> accountsToBeRemoved = new ArrayList<>();

        // Getting list of accountDetails to be removed with zero amount Details
        for(AccountDetails accountDetails:accountBook){
            if (Double.compare(accountDetails.getAmount(), zero) == 0) {
                accountsToBeRemoved.add(accountDetails);
            }
        }

        // Removing the records
        for(AccountDetails ad:accountsToBeRemoved) {
            accountBook.remove(ad);
        }
        // Creating a list of people who owe
        ArrayList<String> peopleWhoOwe = getListOfPersonWhoOwes(accountBook);

        // Mapping Account Details W.r.t. Person Who Owes (Payee, Payee Owes x amt to Payer) (String,Array<AccountDetails>)
        ArrayList<AccountBook> completeAccountBook = getAccountDetailsWRTPeopleWhoOwe(peopleWhoOwe,accountBook);
        System.out.println("Complete Account Book:"+completeAccountBook);
        // Setting occasion Name
        occasionName.setText(occasion.getOccasionName());
        listAdapter = new ListAdapterAccountBook(ShowScores.this, completeAccountBook);
        binding.payerDetails.setAdapter(listAdapter);

        gotToOccasionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowScores.this, OccasionsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        goToExpensesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowScores.this, ExpensesActivity.class);
                intent.putExtra("My Occasion", occasion);
                startActivity(intent);
                finish();
            }
        });
    }

    private ArrayList<AccountBook> getAccountDetailsWRTPeopleWhoOwe(ArrayList<String> peopleWhoOwe, ArrayList<AccountDetails> accountBook) {
        ArrayList<AccountBook> completeAccountBook = new ArrayList<>();
        for(String person:peopleWhoOwe){
            AccountBook accountBookOfSinglePerson = new AccountBook();
            accountBookOfSinglePerson.setPersonWhoOwes(person);

            ArrayList<AccountDetails> accountDetailsForPersonWhoOwes = new ArrayList<>();

            for(AccountDetails account:accountBook){
                if(account.getPersonWhoOwes().equalsIgnoreCase(person)){
//                    System.out.println("Account inside if:"+account);
                    accountDetailsForPersonWhoOwes.add(account);
                }
            }
            accountBookOfSinglePerson.setAccountDetailsForPersonWhoOwes(accountDetailsForPersonWhoOwes);
            completeAccountBook.add(accountBookOfSinglePerson);
        }
        return completeAccountBook;
    }

    private ArrayList<String> getListOfPersonWhoOwes(ArrayList<AccountDetails> accountBook) {
        ArrayList<String> peopleWhoOwe = new ArrayList<>();
        for(AccountDetails accountDetails:accountBook){
            if(peopleWhoOwe.indexOf(accountDetails.getPersonWhoOwes())==-1){
                peopleWhoOwe.add(accountDetails.getPersonWhoOwes());
            }
        }
        return peopleWhoOwe;
    }

    private ArrayList<AccountDetails> createAccountBook(Occasion occasion) {
        ArrayList<String> members = occasion.getMembers();
//        System.out.println("Member: "+members);

        // Initializing List of Account Details with all Unique Pairs
        ArrayList<AccountDetails> accountBook = new ArrayList<>();
        int membersCount = members.size();
        for(int i=0;i<membersCount;i++){
            for(int j=i+1;j<membersCount;j++){
                AccountDetails accountDetails = new AccountDetails();
                accountDetails.setPersonWhoWillGet(members.get(i));
                accountDetails.setPersonWhoOwes(members.get(j));
//                accountDetails.setAmount(0.0d);
                accountBook.add(accountDetails);
            }
        }
        return accountBook;
    }

    private ArrayList<AccountDetails> settleAccountBook(ArrayList<AccountDetails> accountBook, Occasion occasion){
        ArrayList<Expense> ExpenseList = getExpenseDetails(occasion.getOccasionId());

        // Getting List Of Payees
        ArrayList<String> payees = getListOfPayees(ExpenseList);


        for(Expense t:ExpenseList){
            String payee = t.getPayee();
            double amt = t.getAmount();
            double amountToBePaid;
            ArrayList<String> payingOnBehalfOf = t.getOnBehalfOf();
            for(String memberPayingOnBehalfOf:payingOnBehalfOf){
                amountToBePaid = amt/payingOnBehalfOf.size();
                accountBook = updateAccountDetails(accountBook,payee,memberPayingOnBehalfOf,amountToBePaid);
            }
        }
        return accountBook;
    }

    private ArrayList<AccountDetails> updateAccountDetails(ArrayList<AccountDetails> accountBook, String payee, String memberPayingOnBehalfOf, Double amountToBePaid) {
        for(AccountDetails accDetail:accountBook) {
            double amountOwed = accDetail.getAmount();
            if (accDetail.getPersonWhoWillGet().equalsIgnoreCase(payee) && accDetail.getPersonWhoOwes().equalsIgnoreCase(memberPayingOnBehalfOf)) {
                accDetail.setAmount(amountOwed + amountToBePaid);
            } else if (accDetail.getPersonWhoWillGet().equalsIgnoreCase(memberPayingOnBehalfOf) && accDetail.getPersonWhoOwes().equalsIgnoreCase(payee)) {
                accDetail.setAmount(amountOwed - amountToBePaid);
                if (accDetail.getAmount() < 0) {
                    accDetail.setAmount(accDetail.getAmount()*-1);
                    accDetail.setPersonWhoWillGet(payee);
                    accDetail.setPersonWhoOwes(memberPayingOnBehalfOf);
                }
            }
        }
        return accountBook;
    }

    private ArrayList<String> getListOfPayees(ArrayList<Expense> ExpenseList) {
        ArrayList<String> payees = new ArrayList<>();
        for(Expense t:ExpenseList){
            if(payees.indexOf(t.getPayee())==-1){
                payees.add(t.getPayee());
            }
        }
        return payees;
    }



    public ArrayList<String> getKeysFromMap(Set<String> set){
        ArrayList<String> keyList = new ArrayList<>();
        for ( String key : set) {
            keyList.add(key);
        }
        return keyList;
    }



    public ArrayList<Expense> getExpenseDetails(String occId) {
        ArrayList<Expense> ExpensesList = new ArrayList<>();
        Cursor data = myEquiPayDB.getExpensesByOccasionId(occId);

        while (data.moveToNext()) {
            Expense t1 = new Expense();
            t1.setExpenseId(data.getString(0));
            t1.setEvent(data.getString(1));
            t1.setOccasionId(data.getString(2));
            t1.setPayee(data.getString(3));
            t1.setAmount(data.getInt(4));

            String retrievedMembers = data.getString(5);
            ArrayList<String> results = splitBy(retrievedMembers, ",");
            t1.setOnBehalfOf(results);
            t1.setDateOfExpense(data.getString(6));
            ExpensesList.add(t1);

        }


        return ExpensesList;
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