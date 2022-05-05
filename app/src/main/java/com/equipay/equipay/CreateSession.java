package com.equipay.equipay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateSession extends AppCompatActivity {
    ListView membersListView;
    ArrayList<String> members;
    Occasion occasion;
    ArrayAdapter<String> adapter;
    EditText addMember,occasionName;
    Button addMemberBtn,submitBtn;
    TextView membersPresentDisplay;
    EquiPayDatabase myEquiPayDB;
    int memberCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);
        membersListView = findViewById(R.id.members_list);
        addMember = findViewById(R.id.add_member);
        addMemberBtn = findViewById(R.id.add_member_btn);
        membersPresentDisplay = findViewById(R.id.members_present_display);
        members  = new ArrayList<>();
        myEquiPayDB = new EquiPayDatabase(this);

        membersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //makeToast(name);
            }
        });
        membersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = members.get(i);
                name = name.trim();
                promptForRemoving(name);
                // makeToast("Long Press: " + members.get(i));
                return false;
            }
        });

        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,members);
        membersListView.setAdapter(adapter);

        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = addMember.getText().toString();
                name = name.trim();
                if(name == null || name.length() == 0) {
                    makeToast("Enter an item.");
                }
                else{
                    addItem(name);
                    addMember.setText("");
                    makeToast("Added '"+name+"'");

                }
            }
        });
        occasionName = findViewById(R.id.occasion);
        submitBtn = findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String occ = occasionName.getText().toString().trim();

                if(members.size()<2){
                    makeToast("Minimum 2 Members required for an Occasion.");
                }
                if(members.size() >=2 && occ!=null && occ!=""){
                    makeToast(occ+" with "+members.size()+" members");
                    createNewOccasion();
                }
            }
        });
    }

    private void createNewOccasion() {
        String occName = occasionName.getText().toString().trim();

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        occasion = new Occasion();
        occasion.setOccasionName(occName);
        occasion.setExpensesList(new ArrayList<Expense>());
        occasion.setMembers(members);
        occasion.setCreatedOnDate(date);
        if(myEquiPayDB.createOccasion(occasion)) {
            Intent intent = new Intent(CreateSession.this, OccasionsActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void promptForRemoving(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateSession.this);

        builder.setMessage("Are you sure you want to Remove "+s+"?")
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeMember(s);
                    }
                }).setNegativeButton("Cancel",null);

        AlertDialog alert = builder.create();
        // Showing Alert Message
        alert.show();
    }

    public void removeMember(String s) {
        makeToast("Removed '"+s+"'");
        members.remove(s);
        adapter.notifyDataSetChanged();
        if(members.size()==0){
            membersPresentDisplay.setText("No Members Present");
        }
        else{
            membersPresentDisplay.setText("Members Present: "+members.size());
        }

    }

    public void addItem(String text) {
        members.add(text);
        adapter.notifyDataSetChanged();
        if(members.size()!=0){
            membersPresentDisplay.setText("Members Present: "+members.size());
        }
    }

    Toast t;
    private void makeToast(String name) {
        if (t!=null) t.cancel();
        t = Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT);
        t.show();
    }
}