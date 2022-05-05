package com.equipay.equipay;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.equipay.equipay.databinding.ActivityExpensesBinding;
import com.equipay.equipay.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class ExpensesActivity extends AppCompatActivity {
    EquiPayDatabase myEquiPayDB;
    private ActivityExpensesBinding binding;
    static ArrayList<String> members;
    static Occasion occasion = null;
    Cursor data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityExpensesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        myEquiPayDB = new EquiPayDatabase(this);
        Intent i = getIntent();
        occasion= (Occasion) i.getParcelableExtra("My Occasion");
        ArrayList<Expense> ExpenseArrayList = new ArrayList<>();
    }
    Toast t;
    private void makeToast(String name) {
        if (t!=null) t.cancel();
        t = Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT);
        t.show();
    }
    public Occasion getOccasionDetails(){
        return occasion;
    }

}
