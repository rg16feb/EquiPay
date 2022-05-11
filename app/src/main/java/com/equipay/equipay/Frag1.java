package com.equipay.equipay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Frag1 extends Fragment {
    ListView membersListViewFrag1;
    TextView occasionName;
    ArrayList<String> members = null;
    ArrayAdapter adapter;
    Button goToMyOccasionsFrag1,addNewMemberFrag1;
    EditText newMemberInput;
    EquiPayDatabase myEquiPayDB;
    ExpensesActivity ta = new ExpensesActivity();
    Toast t;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View frag1 = inflater.inflate(R.layout.frag1_layout,container,false);
        return frag1;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        Occasion occasion;
        occasion = ta.getOccasionDetails();
        goToMyOccasionsFrag1 = view.findViewById(R.id.go_to_my_occasion_frag1);
        addNewMemberFrag1 = view.findViewById(R.id.add_new_member_frag1);
        occasionName = view.findViewById(R.id.occasionTextViewHeader);
        membersListViewFrag1 = view.findViewById(R.id.members_list_frag1);
        myEquiPayDB = new EquiPayDatabase(this.getContext());
        if(occasion == null){
            occasionName.setText("NO RECORDS FOUND.");
        }
        else {
            members = occasion.getMembers();
            occasionName.setText("* " + occasion.getOccasionName() + " *");
            adapter = new ArrayAdapter<>(view.getContext(), R.layout.yellow_text_list_item,members);
            membersListViewFrag1.setAdapter(adapter);
        }
        membersListViewFrag1.setClickable(true);
        membersListViewFrag1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                promptForRemoving(occasion,i);
                return true;
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Add New Member");
        newMemberInput = new EditText(view.getContext());
        builder.setView(newMemberInput);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String memberName = newMemberInput.getText().toString();
                newMemberInput.setText("");
                Toast.makeText(getActivity(),memberName,Toast.LENGTH_SHORT).show();
                members.add(memberName);
                occasion.setMembers(members);
                myEquiPayDB.updateMembersForOccasion(occasion);
                adapter = new ArrayAdapter<>(view.getContext(), R.layout.yellow_text_list_item,members);
                membersListViewFrag1.setAdapter(adapter);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog ad = builder.create();

        addNewMemberFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();
            }
        });

        goToMyOccasionsFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),OccasionsActivity.class);
                startActivity(intent);
            }
        });

    }
    // Prompt For Removing
    public void promptForRemoving(Occasion occasion, int position) {

        ArrayList<String> membersList =  occasion.getMembers();
        String memberToBeDeleted = membersList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
        builder.setMessage("Are you sure you want to Remove "+memberToBeDeleted+"?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        membersList.remove(memberToBeDeleted);
                        occasion.setMembers(membersList);
                        if (membersList.size()==0){
                            myEquiPayDB.deleteOccasion(occasion);
                            makeToast("Deleting Occasion '"+occasion.getOccasionName()+"'");
                            Intent intent = new Intent(getContext(), OccasionsActivity.class);
                            startActivity(intent);

                        }
                        adapter = new ArrayAdapter<>(getView().getContext(), R.layout.yellow_text_list_item,members);
                        myEquiPayDB.updateMembersForOccasion(occasion);
                        membersListViewFrag1.setAdapter(adapter);

                    }
                }).setNegativeButton("Cancel",null);

        AlertDialog alert = builder.create();
        // Showing Alert Message
        alert.show();
    }
    private void makeToast(String name) {
        if (t!=null) t.cancel();
        t = Toast.makeText(getActivity(),name,Toast.LENGTH_SHORT);
        t.show();
    }
}

