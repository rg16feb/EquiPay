package com.equipay.equipay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.equipay.equipay.databinding.ActivityCreateNewExpenseBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateNewExpense extends AppCompatActivity {
    EquiPayDatabase myEquiPayDB;
    EditText ExpenseName,amountPaid;
    Spinner payee;
    Occasion occasion;
    ListView membersListToBeSelected;
    ArrayList<String> members;
    Button createNewExpenseBtn, cancelExpenseBtn;
    ActivityCreateNewExpenseBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_expense);
        binding = ActivityCreateNewExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ExpenseName = findViewById(R.id.Expense_name);
        cancelExpenseBtn = findViewById(R.id.cancelExpense);
        payee = findViewById(R.id.payee_selection);
        amountPaid = findViewById(R.id.amountPaidValueEditText);
        createNewExpenseBtn = findViewById(R.id.createNewExpenseBtn);

        myEquiPayDB = new EquiPayDatabase(this);

        Intent i = getIntent();
        occasion= (Occasion) i.getParcelableExtra("My Occasion");
        members = new ArrayList<>();
        members.add("Choose Payee");
        members = occasion.getMembers();

         // Parsing Members data to Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, members);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payee = findViewById(R.id.payee_selection);
        payee.setAdapter(adapter);
        // Parsing members to be selected to ListView
        membersListToBeSelected = findViewById(R.id.membersListToBeSelected);
        adapter = new ArrayAdapter<String>(this, R.layout.yellow_simple_list_item_multiple_choice,members);
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

        cancelExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateNewExpense.this, ExpensesActivity.class);
                intent.putExtra("My Occasion", occasion);
                startActivity(intent);
                finish();
            }
        });

        createNewExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "";

                String event = ExpenseName.getText().toString();
                makeToast("Expense:"+event);
                if(event==null || event==""){
                    msg = "Please Enter Expense Name";
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
                Expense newExpense = new Expense();
                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());

                newExpense.setPayee(selectedPayee);
                newExpense.setOccasionId(occasion.getOccasionId());
                newExpense.setAmount(Integer.parseInt(amountPaid.getText().toString()));
                newExpense.setOnBehalfOf(onBehalfOfList);
                newExpense.setEvent(event);
                newExpense.setDateOfExpense(date);

                if(myEquiPayDB.createNewExpense(newExpense)) {
                    Intent intent = new Intent(CreateNewExpense.this, ExpensesActivity.class);
                    intent.putExtra("My Occasion", occasion);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    public void makeToast(String msg){
        Toast.makeText(CreateNewExpense.this,msg,Toast.LENGTH_SHORT).show();
    }

}