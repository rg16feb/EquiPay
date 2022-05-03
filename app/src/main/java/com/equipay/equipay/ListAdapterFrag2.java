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

public class ListAdapterFrag2 extends ArrayAdapter<Transaction> {
    public ListAdapterFrag2(Context context, ArrayList<Transaction> transactions){
        super(context,R.layout.transaction_info_list_item,transactions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Transaction transaction = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_info_list_item,parent,false);
        }
        TextView transactionName = convertView.findViewById(R.id.transaction_name);
        TextView payee = convertView.findViewById(R.id.payee);
        TextView payingFor = convertView.findViewById(R.id.payingFor);
        TextView dateOfTransaction = convertView.findViewById(R.id.date_of_transaction);
        TextView amt = convertView.findViewById(R.id.amountPaidValue);

        transactionName.setText(transaction.getEvent());
        payee.setText("Payee: "+transaction.getPayee());
        payingFor.setText("Paying For Members: "+transaction.getOnBehalfOf().size());
        dateOfTransaction.setText(transaction.getDateOfTransaction());
        amt.setText(Float.toString(transaction.getAmount()));
        return convertView;
    }
}
