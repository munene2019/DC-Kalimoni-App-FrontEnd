package com.example.invoiceapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home_page extends AppCompatActivity {
    Dialog dialog;
    Button apply;
    private static final String BASE_URL = "http://192.168.1.5:7001/LoanApplication/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        apply=findViewById(R.id.loan);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i= new Intent(Home_page.this, loanApplication_page.class);
                startActivity(i);
            }
        });
    }
}
