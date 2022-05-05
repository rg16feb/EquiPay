package com.equipay.equipay;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.equipay.equipay.databinding.ActivityOccasionsBinding;

import java.util.ArrayList;


public class OccasionsActivity extends AppCompatActivity {
    private ImageButton createSessionBtn;
    ListAdapter listAdapter;
    ListView myOccasionsListView;

    EquiPayDatabase myEquiPayDB;
    TextView recordStatusTextView;
    ActivityOccasionsBinding binding;
    ArrayList<Occasion> occasionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occasions);
        binding = ActivityOccasionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myEquiPayDB = new EquiPayDatabase(this);
        myOccasionsListView = findViewById(R.id.myOccasionsListView);
        recordStatusTextView = findViewById(R.id.records_status_text_View);

        // Retrieve Occasions List From DB
        occasionsList = getMyOccasionsList();
        if (occasionsList.size() > 0) {
            recordStatusTextView.setText("");
        }

        // Set list of occasions in format
        listAdapter = new ListAdapter(OccasionsActivity.this, occasionsList);
        binding.myOccasionsListView.setAdapter(listAdapter);
        binding.myOccasionsListView.setClickable(true);
        binding.myOccasionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override

            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                promptForRemoving(occasionsList,i);
                return true;
            }
        });
        binding.myOccasionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(OccasionsActivity.this, ExpensesActivity.class);
                intent.putExtra("My Occasion", occasionsList.get(i));
                startActivity(intent);
                finish();

            }
        });

        // Create new Session
        createSessionBtn = findViewById(R.id.createSessionBtn);
        createSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OccasionsActivity.this, CreateSession.class);
                startActivity(intent);
                finish();
            }
        });
    }
    // Prompt For Removing
    public void promptForRemoving(ArrayList<Occasion> occasionsList, int position) {
        Occasion occasionToBeDeleted = occasionsList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(OccasionsActivity.this);
        builder.setMessage("Are you sure you want to Delete Records for "+occasionToBeDeleted.getOccasionName()+"?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        occasionsList.remove(occasionToBeDeleted);
                        if (occasionsList.size()==0){
                            recordStatusTextView.setText("NO RECORDS FOUND");
                        }
                        myEquiPayDB.deleteOccasion(occasionToBeDeleted);
                        binding.myOccasionsListView.setAdapter(listAdapter);

                    }
                }).setNegativeButton("Cancel",null);

        AlertDialog alert = builder.create();
        // Showing Alert Message
        alert.show();
    }


    public ArrayList<Occasion> getMyOccasionsList() {
        Cursor data = myEquiPayDB.getOccasionsList();
        ArrayList<Occasion> occasionsList = new ArrayList<>();

        while(data.moveToNext()){
            Occasion o1 = new Occasion();
            o1.setOccasionId(data.getString(0));
            o1.setOccasionName(data.getString(1));
            String retrievedMembers = data.getString(2);
            ArrayList<String> results= splitBy(retrievedMembers,",");
            o1.setCreatedOnDate(data.getString(3));

            o1.setMembers(results);
            occasionsList.add(o1);
        }
        return occasionsList;
    }
    // Spliting String by comma and returning list of members
    private ArrayList<String> splitBy(String retrievedMembers, String s) {
        ArrayList<String> list = new ArrayList<>();
        String[] data = retrievedMembers.split(s);
        for(String i:data){
            list.add(i);
        }
        return list;
    }

}
