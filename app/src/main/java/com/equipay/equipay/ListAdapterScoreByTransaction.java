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
import java.util.HashMap;

// Sets values for My Occasions
public class ListAdapterScoreByTransaction extends ArrayAdapter<PayerDetails> {
    public ListAdapterScoreByTransaction(Context context, ArrayList<PayerDetails> payerDetailsArrayList){
        super(context,R.layout.split_detail_list_item,payerDetailsArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PayerDetails payerDetails = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.split_detail_list_item,parent,false);
        }
        TextView payerName = convertView.findViewById(R.id.paying_on_behalf_of_textview);
        TextView splitAmount = convertView.findViewById(R.id.amountPaidValue);

        payerName.setText(payerDetails.getPayerName());
        splitAmount.setText("Rs."+payerDetails.getAmount());
        return convertView;
    }
}
