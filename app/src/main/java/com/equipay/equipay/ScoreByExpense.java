package com.equipay.equipay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.equipay.equipay.databinding.ActivityScoreByExpenseBinding;

import java.util.ArrayList;

public class ScoreByExpense extends AppCompatActivity {
    TextView ExpenseName,occasionName,payee,amount;
    ListView payerDetails;
    Button gotToOccasionsBtn,goToExpensesBtn;
    ListAdapterScoreByExpense listAdapter;
    ActivityScoreByExpenseBinding binding;
    Expense Expense;
    EquiPayDatabase myEquiPayDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_by_expense);
        binding = ActivityScoreByExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myEquiPayDB = new EquiPayDatabase(this);
        Intent i = getIntent();
        Expense = i.getParcelableExtra("Expense");
        System.out.println("My Expense:"+Expense.toString());
        ExpenseName = findViewById(R.id.Expense_name_text_view);
        occasionName = findViewById(R.id.occasion_name_text_view);
        payee = findViewById(R.id.payee);
        amount = findViewById(R.id.amountPaidValue);
        payerDetails = findViewById(R.id.payerDetails);
        gotToOccasionsBtn = findViewById(R.id.go_to_my_occasion);
        goToExpensesBtn = findViewById(R.id.go_to_my_expenses);
        ExpenseName.setText(Expense.getEvent());
        payee.setText(Expense.getPayee());
        amount.setText("Rs."+Expense.getAmount());
        ArrayList<String> payingOnBehalfOf = Expense.getOnBehalfOf();
        int membersCount = payingOnBehalfOf.size();
        double splitAmount = (float)(Expense.getAmount())/membersCount;

        // Setting payer Details in the list

        ArrayList<PayerDetails> payerDetails = new ArrayList<>();
        for(String name:payingOnBehalfOf){
            PayerDetails details = new PayerDetails();
            details.setPayerName(name);
            details.setAmount(splitAmount);
            payerDetails.add(details);
        }
        listAdapter = new ListAdapterScoreByExpense(ScoreByExpense.this, payerDetails);
        binding.payerDetails.setAdapter(listAdapter);

        gotToOccasionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreByExpense.this, OccasionsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        goToExpensesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreByExpense.this, ExpensesActivity.class);
                Occasion occ = getOccasionDetails(Expense.getOccasionId());
                intent.putExtra("My Occasion", occ);
                startActivity(intent);
                finish();
            }
        });
    }


    public Occasion getOccasionDetails(String occId) {
        Cursor data = myEquiPayDB.getOccasionDetails(occId);
        Occasion occasion = new Occasion();
        while(data.moveToNext()){

            occasion.setOccasionId(data.getString(0));
            occasion.setOccasionName(data.getString(1));
            String retrievedMembers = data.getString(2);
            ArrayList<String> results= splitBy(retrievedMembers,",");
            occasion.setCreatedOnDate(data.getString(3));

            occasion.setMembers(results);

        }
        return occasion;
    }
    private ArrayList<String> splitBy(String retrievedMembers, String s) {
        ArrayList<String> list = new ArrayList<>();
        String[] data = retrievedMembers.split(s);
        for(String i:data){
            list.add(i);
        }
        return list;
    }
}