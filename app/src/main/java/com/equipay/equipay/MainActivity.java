package com.equipay.equipay;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    // private static final String EXTRA_MESSAGE = "Let's Start Your Session";
    private ImageButton createSessionBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createSessionBtn = findViewById(R.id.createSessionBtn);
        createSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OccasionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}