package com.equipay.equipay;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.equipay.equipay.databinding.ActivityCreateNewTransactionBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateNewTransaction extends AppCompatActivity {
    EquiPayDatabase myEquiPayDB;
    EditText transactionName,amountPaid;
    Spinner payee;
    Occasion occasion;
    TextView membersSelectedDisplay;
    ListView membersListToBeSelected;
    ArrayList<String> members;
    Button createNewTransactionBtn, cancelTransactionBtn;
    ActivityCreateNewTransactionBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_transaction);
        binding = ActivityCreateNewTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        transactionName = findViewById(R.id.transaction_name);
        cancelTransactionBtn = findViewById(R.id.cancelTransaction);
        payee = findViewById(R.id.payee_selection);
        amountPaid = findViewById(R.id.amountPaidValueEditText);
        membersSelectedDisplay = findViewById(R.id.members_selected_display);
        createNewTransactionBtn = findViewById(R.id.createNewTransactionBtn);

        myEquiPayDB = new EquiPayDatabase(this);

        Intent i = getIntent();
        occasion= (Occasion) i.getParcelableExtra("My Occasion");
        members = new ArrayList<>();
        members.add("Choose Payee");
        members = occasion.getMembers();

         // Parsing Members data to Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, members);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payee = findViewById(R.id.payee_selection);
        payee.setAdapter(adapter);
        // Parsing members to be selected to ListView
        membersListToBeSelected = findViewById(R.id.membersListToBeSelected);
        adapter = new ArrayAdapter<String>(this, 
                android.R.layout.simple_list_item_multiple_choice,members);
        membersListToBeSelected.setAdapter(adapter);

        // Selecting Payee from Spinner
        payee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),""+payee.getSelectedItem()+" is Paying.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cancelTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateNewTransaction.this, TransactionsActivity.class);
                intent.putExtra("My Occasion", occasion);
                startActivity(intent);
                finish();
            }
        });

        createNewTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "";

                String event = transactionName.getText().toString();
                makeToast("Transaction:"+event);
                if(event==null || event==""){
                    msg = "Please Enter Transaction Name";
                    makeToast(msg);
                }

                try{
                    if(amountPaid.getText().toString()!="" || amountPaid.getText().toString()!=null){
                        int amt = Integer.parseInt(amountPaid.getText().toString());
                        makeToast(Integer.toString(amt));
                    }
                }
                catch(NumberFormatException e){
                    makeToast("Can't leave amount as empty, Mate!");
                }
                String selectedPayee = payee.getSelectedItem().toString();

                ArrayList<String> onBehalfOfList = new ArrayList<>();
                String memberSelected = "Selected items:\n";
                for(int i=0;i<membersListToBeSelected.getCount();i++){
                    if(membersListToBeSelected.isItemChecked(i)){
                        memberSelected+=membersListToBeSelected.getItemAtPosition(i)+"\n";
                        onBehalfOfList.add(membersListToBeSelected.getItemAtPosition(i).toString());
                    }
                }
                makeToast(memberSelected);
                Transaction newTransaction = new Transaction();
                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());

                newTransaction.setPayee(selectedPayee);
                newTransaction.setOccasionId(occasion.getOccasionId());
                newTransaction.setAmount(Integer.parseInt(amountPaid.getText().toString()));
                newTransaction.setOnBehalfOf(onBehalfOfList);
                newTransaction.setEvent(event);
                newTransaction.setDateOfTransaction(date);

                if(myEquiPayDB.createNewTransaction(newTransaction)) {
                    Intent intent = new Intent(CreateNewTransaction.this, TransactionsActivity.class);
                    intent.putExtra("My Occasion", occasion);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    public void makeToast(String msg){
        Toast.makeText(CreateNewTransaction.this,msg,Toast.LENGTH_SHORT).show();
    }

}