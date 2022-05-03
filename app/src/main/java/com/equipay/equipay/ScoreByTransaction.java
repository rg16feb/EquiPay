package com.equipay.equipay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.equipay.equipay.databinding.ActivityScoreByTransactionBinding;

import java.util.ArrayList;

public class ScoreByTransaction extends AppCompatActivity {
    TextView transactionName,occasionName,payee,amount;
    ListView payerDetails;
    Button gotToOccasionsBtn,goToTransactionsBtn;
    ListAdapterScoreByTransaction listAdapter;
    ActivityScoreByTransactionBinding binding;
    Transaction transaction;
    EquiPayDatabase myEquiPayDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_by_transaction);
        binding = ActivityScoreByTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myEquiPayDB = new EquiPayDatabase(this);
        Intent i = getIntent();
        transaction = i.getParcelableExtra("Transaction");
        System.out.println("My Transaction:"+transaction.toString());
        transactionName = findViewById(R.id.transaction_name_text_view);
        occasionName = findViewById(R.id.occasion_name_text_view);
        payee = findViewById(R.id.payee);
        amount = findViewById(R.id.amountPaidValue);
        payerDetails = findViewById(R.id.payerDetails);
        gotToOccasionsBtn = findViewById(R.id.go_to_my_occasion);
        goToTransactionsBtn = findViewById(R.id.go_to_transactions);
        transactionName.setText(transaction.getEvent());
        payee.setText(transaction.getPayee());
        amount.setText("Rs."+transaction.getAmount());
        ArrayList<String> payingOnBehalfOf = transaction.getOnBehalfOf();
        int membersCount = payingOnBehalfOf.size();
        float splitAmount = (float)(transaction.getAmount())/membersCount;

        // Setting payer Details in the list

        ArrayList<PayerDetails> payerDetails = new ArrayList<>();
        for(String name:payingOnBehalfOf){
            PayerDetails details = new PayerDetails();
            details.setPayerName(name);
            details.setAmount(splitAmount);
            payerDetails.add(details);
        }
        listAdapter = new ListAdapterScoreByTransaction(ScoreByTransaction.this, payerDetails);
        binding.payerDetails.setAdapter(listAdapter);

        gotToOccasionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreByTransaction.this, OccasionsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        goToTransactionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreByTransaction.this, TransactionsActivity.class);
                Occasion occ = getOccasionDetails(transaction.getOccasionId());
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