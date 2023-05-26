package com.example.piggybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView welcomeText = findViewById(R.id.welcome_text_name_tv);
        TextView balanceTv = findViewById(R.id.balance_tv);
        TextView cardNumTv = findViewById(R.id.card_num_tv);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Gson gson = new Gson();
        String json = getSharedPreferences("client",MODE_PRIVATE).getString("clientInfo","");
        ClientModel client = gson.fromJson(json, ClientModel.class);
        welcomeText.setText(client.getName());

        firebaseFirestore.collection("Accounts")
                .whereEqualTo("ID", client.getID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        AccountModel accountModel = new AccountModel();
                        accountModel = task.getResult().getDocuments().get(0).toObject(AccountModel.class);
                        balanceTv.setText(String.valueOf(accountModel.getBalance()));
                        cardNumTv.setText(accountModel.getBankCardNum());

                    }
                });




        /*todo: ana işlem menüsü
                para çekme para gönderme borç ödeme kart işlemleri atm işlemleri vs.



         */



        ImageButton sendMoneyButton = findViewById(R.id.send_money_button);
        ImageButton atmButton = findViewById(R.id.atm_button);
        ImageButton payOffDebtButton = findViewById(R.id.pay_off_debt_button);
        ImageButton cardSettingsButton = findViewById(R.id.card_setting_button);

        sendMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SendMoney.class));
                finish();

            }
        });
        payOffDebtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, payOffDebtMenu.class));
            }
        });
    }
}