package com.example.piggybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


public class SendMoney extends AppCompatActivity {
    AccountModel receiverAccount = new AccountModel();
    AccountModel senderAccount = new AccountModel();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    final int NO_AMOUNT = -1;
    int amount = NO_AMOUNT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
        ImageButton back = findViewById(R.id.sm_main_back_button);

        EditText IBAN = findViewById(R.id.iban_edit_text);
        EditText amountEditText = findViewById(R.id.amount_edit_text);
        Button sendMoneyButton = findViewById(R.id.send_money_confirm_button);

        Gson gson = new Gson();
        String json = getSharedPreferences("client", MODE_PRIVATE).getString("clientInfo", "");
        ClientModel client = gson.fromJson(json, ClientModel.class);

        int balance = getSharedPreferences("balance",MODE_PRIVATE).getInt("balanceVal",0);

        sendMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    amount = Integer.parseInt(amountEditText.getText().toString());

                } catch (Exception e) {
                }

                if (!(balance < amount || amount == NO_AMOUNT || TextUtils.isEmpty(IBAN.getText().toString()))) {



                firebaseFirestore.collection("Accounts")
                        .whereEqualTo("ID", client.getID())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()
                                        && !task.getResult().getDocuments().isEmpty()) {
                                    task.getResult().getDocuments().
                                            get(0).getReference().update("balance", balance - amount);
                                }
                            }
                        });
                firebaseFirestore.collection("Accounts")
                        .whereEqualTo("IBAN", IBAN.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                if (task2.isSuccessful()
                                        && !task2.getResult().getDocuments().isEmpty()) {
                                    int recevierBalance = task2.getResult().getDocuments().get(0).get("balance", Integer.class);
                                    task2.getResult().getDocuments().get(0).getReference().update("balance", recevierBalance + amount);
                                    Toast.makeText(SendMoney.this, "İşlem Başarılı", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SendMoney.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SendMoney.this, "Iban eksik ya  da hatalı", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
                else{
                    Toast.makeText(SendMoney.this, "Boş değer ya da bakiyenizden fazla bir tutar girdiniz", Toast.LENGTH_SHORT).show();

                }

            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SendMoney.this, MainActivity.class);

                startActivity(intent);
                finish();

            }
        });


    }
}