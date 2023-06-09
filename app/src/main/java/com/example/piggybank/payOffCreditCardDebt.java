package com.example.piggybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

public class payOffCreditCardDebt extends AppCompatActivity {

    CreditCardModel creditCard  =new CreditCardModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payy_off_credit_card_debt);
        Gson gson = new Gson();
        String json = getSharedPreferences("client", MODE_PRIVATE).getString("clientInfo", "");
        ClientModel client = gson.fromJson(json, ClientModel.class);
        TextView debtTv = findViewById(R.id.debt_tv);
        Button paydebtButton = findViewById(R.id.pay_off);
        EditText amountTv  = findViewById(R.id.amount_debt);
        ImageButton back  = findViewById(R.id.pcd_main_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(payOffCreditCardDebt.this, CardSettings.class);
                startActivity(intent);
                finish();
            }
        });


        firebaseFirestore.collection("CreditCard")
                .whereEqualTo("ID", client.getID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            CreditCardModel creditCard = task.getResult().getDocuments().get(0).toObject(CreditCardModel.class);
                                String infoString = "Güncel Borcunuz: " + creditCard.getDebt() + " TL. 'dir." +
                                        " Minimum Ödeme Tutarı " + creditCard.getMin();
                                debtTv.setText(infoString);


                        }

                    }
                });
        paydebtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("CreditCard")
                        .whereEqualTo("ID", client.getID())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){

                                    creditCard = task.getResult().getDocuments().get(0).toObject(CreditCardModel.class);
                                    int amount = Integer.parseInt(amountTv.getText().toString());
                                    int newDebt = creditCard.getDebt() - amount;
                                    int newLimit = creditCard.getCardLimit() + amount;
                                    task.getResult().getDocuments().get(0).getReference().update("debt", newDebt);
                                    task.getResult().getDocuments().get(0).getReference().update("cardLimit", newLimit);
                                    int min = creditCard.getMin();
                                    if(amount >= min){
                                        task.getResult().getDocuments().get(0).getReference().update("min", 0);
                                    }
                                    else{
                                        task.getResult().getDocuments().get(0).getReference().update("min",min-amount );

                                    }
                                    startActivity(new Intent(payOffCreditCardDebt.this,MainActivity.class));
                                    Toast.makeText(payOffCreditCardDebt.this, "Ödemeniz için teşekkürler", Toast.LENGTH_SHORT).show();
                                    finish();




                                }
                            }
                        });
            }
        });


            }

}