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
// Sets values for My Occasions
public class ListAdapter extends ArrayAdapter<Occasion> {
    public ListAdapter(Context context, ArrayList<Occasion> occasionArrayList){
        super(context,R.layout.occasion_activity_list_item,occasionArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Occasion occasion = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.occasion_activity_list_item,parent,false);
        }
        TextView activityName = convertView.findViewById(R.id.activity_name);
        TextView membersCount = convertView.findViewById(R.id.members_count);
        TextView dateOfOccasion = convertView.findViewById(R.id.date_of_occasion);

        activityName.setText(occasion.getOccasionName());
        membersCount.setText("Members: "+occasion.getMembers().size());
        dateOfOccasion.setText(occasion.getCreatedOnDate());
        return convertView;
    }
}
