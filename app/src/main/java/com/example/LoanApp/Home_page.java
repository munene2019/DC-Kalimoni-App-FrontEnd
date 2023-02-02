package com.example.LoanApp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home_page extends AppCompatActivity {
    Dialog dialog;
    Button apply;
    private static final String BASE_URL = "http://10.20.33.127:7001/Loan_App_API/";
    @SuppressLint("MissingInflatedId")
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

        apply=findViewById(R.id.payloan);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i= new Intent(Home_page.this, PayLoan.class);
                startActivity(i);
            }
        });
    }
}
