package com.equipay.equipay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapterFrag2 extends ArrayAdapter<Expense> {
    public ListAdapterFrag2(Context context, ArrayList<Expense> Expenses){
        super(context,R.layout.expense_info_list_item,Expenses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Expense Expense = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_info_list_item,parent,false);
        }
        TextView ExpenseName = convertView.findViewById(R.id.Expense_name);
        TextView payee = convertView.findViewById(R.id.payee);
//        TextView payingFor = convertView.findViewById(R.id.payingFor);
        TextView dateOfExpense = convertView.findViewById(R.id.date_of_Expense);
        TextView amt = convertView.findViewById(R.id.amountPaidValue);

        ExpenseName.setText(Expense.getEvent());
        payee.setText(Expense.getPayee()+" Paid for "+Expense.getOnBehalfOf().size()+" Members");
//        payingFor.setText(Expense.getOnBehalfOf().size()+" Members");
        dateOfExpense.setText(Expense.getDateOfExpense());
        amt.setText("Rs. "+Double .toString(Expense.getAmount()));
        return convertView;
    }
}
