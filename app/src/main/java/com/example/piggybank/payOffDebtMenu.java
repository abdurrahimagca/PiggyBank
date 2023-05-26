package com.example.piggybank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class payOffDebtMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_off_debt_menu);
        Button payOffCreditCardDebt = findViewById(R.id.creditCard);
        Button payoffDebt = findViewById(R.id.Credit);

        payOffCreditCardDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(payOffDebtMenu.this, payOffCreditCardDebt.class));
                finish();
            }
        });

        payoffDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(payOffDebtMenu.this, payOffDebt.class));
            }
        });


    }
}