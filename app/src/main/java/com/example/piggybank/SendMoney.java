package com.example.piggybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
        EditText IBAN = findViewById(R.id.iban_edit_text);
        EditText amountEditText = findViewById(R.id.amount_edit_text);
        Button sendMoneyButton = findViewById(R.id.send_money_confirm_button);
        Gson gson = new Gson();
        String json = getSharedPreferences("client", MODE_PRIVATE).getString("clientInfo", "");
        ClientModel client = gson.fromJson(json, ClientModel.class);

        //göndericinin hesap bilgilerini oluşturma


        sendMoneyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //try-catch
                firebaseFirestore.collection("Accounts")
                        .whereEqualTo("IBAN", IBAN.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                        receiverAccount = task.getResult().getDocuments().get(0).toObject(AccountModel.class);
                                        System.out.println("line 79" + receiverAccount.getBalance());
                                    int amount = Integer.parseInt(amountEditText.getText().toString());

                                    System.out.println("recevier account balance" + receiverAccount.getBalance());
                                    int newReceiverBalance = receiverAccount.getBalance() + amount;
                                    System.out.println("new balance"+ newReceiverBalance);



                                    firebaseFirestore.collection("Accounts")
                                            .whereEqualTo("ID", client.getID())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> taskSender) {
                                                    if (taskSender.isSuccessful()) {
                                                        senderAccount = taskSender.getResult().getDocuments().get(0).toObject(AccountModel.class);
                                                        if(senderAccount.getBalance() >= amount){
                                                            int newSenderBalance = senderAccount.getBalance()-amount;
                                                           taskSender.getResult().getDocuments().get(0).getReference().update("balance", newSenderBalance);

                                                        }
                                                        else{
                                                            //todo: toast message bakiye yetersiz
                                                        }
                                                    }
                                                }
                                            });


                                    task.getResult().getDocuments().get(0).getReference().update("balance", newReceiverBalance);







                                }
                            }
                        });





            }

        });

    }

    }
