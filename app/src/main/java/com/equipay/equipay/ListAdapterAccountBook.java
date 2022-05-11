package com.equipay.equipay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;
// Sets values for My Occasions
public class ListAdapterAccountBook extends ArrayAdapter<AccountBook> {
    public ListAdapterAccountBook(Context context, ArrayList<AccountBook> accountBookArrayList){
        super(context,R.layout.occasion_activity_list_item,accountBookArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AccountBook account = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_score_list_item,parent,false);
        }

        TextView payer = convertView.findViewById(R.id.payer);
        TextView accountDetails = convertView.findViewById(R.id.account_book_detail);

        payer.setText(account.getPersonWhoOwes());
        accountDetails.setText(getOwesToList(account.getAccountDetailsForPersonWhoOwes()));
        return convertView;
    }

    private String getOwesToList(ArrayList<AccountDetails> accountDetailsForPersonWhoOwes) {
        String owesToList="";
        for(AccountDetails ad:accountDetailsForPersonWhoOwes){
            double amount = (double) Math.round(ad.getAmount() * 100) / 100;
            owesToList+=("- Owes "+ad.getPersonWhoWillGet()+" Rs. "+amount+"\n\n");
        }
        owesToList = owesToList.trim();
        return owesToList;
    }
}
