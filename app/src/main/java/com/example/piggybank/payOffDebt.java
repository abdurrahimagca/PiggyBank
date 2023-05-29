package com.example.piggybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

public class payOffDebt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payy_off_debt);
        Gson gson = new Gson();
        String json = getSharedPreferences("client", MODE_PRIVATE).getString("clientInfo", "");
        ClientModel client = gson.fromJson(json, ClientModel.class);
        TextView debtTv = findViewById(R.id.credi_debt_tv);
        Button paydebtButton = findViewById(R.id.pay_off_credi);
        EditText amountTv  = findViewById(R.id.amount_credi_debt);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Debt")
                .whereEqualTo("ID", client.getID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.getDocuments().isEmpty()){
                            DebtModel debt = queryDocumentSnapshots.getDocuments()
                                                .get(0).toObject(DebtModel.class);
                            if(debt.getInstallmentStatus()){
                                String info = "Toplam kalan borcunuz: " + String.valueOf(debt.getAmount())
                                        + " TL'dir. Ödemeniz gereken taksit tutarı: 0 Tl'dir. Düzenli ödemeniz için teşekkürler";
                                debtTv.setText(info);
                                paydebtButton.setVisibility(View.GONE);
                                amountTv.setVisibility(View.GONE);
                            }
                            else{
                                String info = "Toplam kalan borcunuz: " + String.valueOf(debt.getAmount())
                                        + " TL'dir. Ödemeniz gereken taksit tutarı: " + String.valueOf(debt.getInstallment())
                                        +"TL'dir. Lütfen ödemek istediğiniz tutarı giriniz.";
                                debtTv.setText(info);
                                paydebtButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        int balance = getSharedPreferences("balance", MODE_PRIVATE)
                                                                            .getInt("balanceVal", 0);
                                        int amount = Integer.parseInt(amountTv.getText().toString());

                                        if(balance < amount) {
                                            Toast.makeText(payOffDebt.this, "Bakiyeniz yetersiz", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            update(firebaseFirestore, client,balance-amount);
                                            try {
                                                TimeUnit.SECONDS.sleep(1);
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }

                                            queryDocumentSnapshots.getDocuments().get(0)
                                                    .getReference().update("installmentStatus",true);
                                            queryDocumentSnapshots.getDocuments().get(0)
                                                    .getReference().update("amount",debt.getAmount()-amount);

                                            Toast.makeText(payOffDebt.this, "Ödeme başarılı, teşekkürler", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(payOffDebt.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);



                                        }


                                    }
                                });

                            }

                        }
                        else{
                            Toast.makeText(payOffDebt.this, "Borcunuz bulunmamaktadır", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(payOffDebt.this, MainActivity.class));
                        }

                    }
                });


        }
    public void update(FirebaseFirestore firebase, ClientModel clientModel, int val){
        firebase.collection("Accounts")
                .whereEqualTo("ID", clientModel.getID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        task.getResult().getDocuments().get(0).getReference().update("balance", val);
                    }
                });
    }
}