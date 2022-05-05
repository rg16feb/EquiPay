package com.equipay.equipay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.equipay.equipay.databinding.Frag2LayoutBinding;

import java.util.ArrayList;

public class Frag2 extends Fragment {
    ListView ExpensesListListView;
    TextView occasionName,amountTextView;
    ArrayList<String> members = null;
    EquiPayDatabase myEquiPayDB;
    Button goToMyOccasionsFrag2;
    Button createNewExpenseBtn, clearDuesBtn;
    Frag2LayoutBinding frag2Binding;
    ListAdapterFrag2 listAdapterFrag2;
    ExpensesActivity ta = new ExpensesActivity();
    ArrayList<Expense> ExpenseList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View frag2 = inflater.inflate(R.layout.frag2_layout,container,false);
        Occasion occasion = ta.getOccasionDetails();
        myEquiPayDB = new EquiPayDatabase(this.getContext());
        ExpensesListListView = frag2.findViewById(R.id.ExpensesListFrag2ListView);
        ExpenseList = getExpenseDetails(occasion.getOccasionId());
        ListAdapterFrag2 listAdapterFrag2 = new ListAdapterFrag2(
                getActivity(),ExpenseList
        );
        ExpensesListListView.setAdapter(listAdapterFrag2);
        ExpensesListListView.setClickable(true);
        ExpensesListListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override

            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                promptForRemoving(ExpenseList,i);
                return true;
            }
        });
        ExpensesListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ScoreByExpense.class);
                System.out.println("Expense Details: Frag2: "+ExpenseList.get(i));
                intent.putExtra("Expense", ExpenseList.get(i));
                startActivity(intent);
            }
        });


        return frag2;
    }

    public void promptForRemoving(ArrayList<Expense> ExpenseList, int position) {
        Expense ExpenseToBeDeleted = ExpenseList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Are you sure you want to Delete Records for "+ExpenseToBeDeleted.getEvent()+"?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ExpenseList.remove(ExpenseToBeDeleted);
                        myEquiPayDB.deleteExpense(ExpenseToBeDeleted);
                        setAmountTextViewValue(ExpenseList);
                        ListAdapterFrag2 listAdapterFrag2 = new ListAdapterFrag2(
                                getActivity(),ExpenseList
                        );
                        ExpensesListListView.setAdapter(listAdapterFrag2);

                    }
                }).setNegativeButton("Cancel",null);

        AlertDialog alert = builder.create();
        // Showing Alert Message
        alert.show();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Occasion occasion = ta.getOccasionDetails();

        goToMyOccasionsFrag2 = view.findViewById(R.id.go_to_my_occasion_frag2);
        occasionName = view.findViewById(R.id.occasionTextViewHeaderFrag2);
        amountTextView = view.findViewById(R.id.total_amount_value);
        myEquiPayDB = new EquiPayDatabase(view.getContext());
        occasionName.setText("* "+occasion.getOccasionName()+" *");
        occasion.setExpensesList(ExpenseList);
        setAmountTextViewValue(ExpenseList);
        goToMyOccasionsFrag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),OccasionsActivity.class);
                startActivity(intent);
            }
        });

        createNewExpenseBtn = view.findViewById(R.id.new_Expense_frag2);
        createNewExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CreateNewExpense.class);
                intent.putExtra("My Occasion", occasion);
                startActivity(intent);
            }
        });


        clearDuesBtn = view.findViewById(R.id.clear_dues);
        clearDuesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShowScores.class);
                occasion.setExpensesList(ExpenseList);
                intent.putExtra("My Occasion", occasion);
                startActivity(intent);
            }
        });
        super.onViewCreated(view, savedInstanceState);


    }

    private void setAmountTextViewValue(ArrayList<Expense> ExpenseList) {
        int totalAmt = 0;
        for(Expense i:ExpenseList){
            totalAmt+=i.getAmount();
        }
        amountTextView.setText("Rs."+totalAmt);
    }

    public ArrayList<Expense> getExpenseDetails(String occId){
        ArrayList<Expense> ExpensesList = new ArrayList<>();
        Cursor data = myEquiPayDB.getExpensesByOccasionId(occId);

        while(data.moveToNext()){
            Expense t1 = new Expense();
            t1.setExpenseId(data.getString(0));
            t1.setEvent(data.getString(1));
            t1.setOccasionId(data.getString(2));
            t1.setPayee(data.getString(3));
            t1.setAmount(data.getInt(4));

            String retrievedMembers = data.getString(5);
            ArrayList<String> results= splitBy(retrievedMembers,",");
            t1.setOnBehalfOf(results);
            t1.setDateOfExpense(data.getString(6));
            ExpensesList.add(t1);

        }


        return ExpensesList;
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
