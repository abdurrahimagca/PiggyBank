package com.example.piggybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ChangePin extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    EditText cvv;
    EditText newPin;
    Button changePin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*0.7),(int)(height*0.5));

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        getWindow().setAttributes(layoutParams);

        cvv = findViewById(R.id.cp_cvv_tv);
        newPin = findViewById(R.id.new_pin_tv);
        changePin = findViewById(R.id.change_pin_buton);

        String cardType = getIntent().getStringExtra("cardType");
        String cardNum =  getIntent().getStringExtra("cardNum");
        System.out.println(cardType);

        firebaseFirestore = FirebaseFirestore.getInstance();

        changePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection(cardType)
                        .whereEqualTo("cardNum", cardNum)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful() &&
                                        !task.getResult().getDocuments().isEmpty()){
                                    int CVV = Integer.parseInt(cvv.getText().toString());
                                    int databaseCVV = task.getResult().getDocuments().get(0)
                                                    .get("CVV",Integer.class);
                                    if(CVV == databaseCVV){
                                        //todo: yeni pin sıfırla başlamayan dört haneli pozitif integer patternine uymalı
                                        task.getResult().getDocuments().get(0).getReference()
                                                .update("pin", Integer.parseInt(newPin.getText().toString()));
                                    }
                                    else{
                                        Toast.makeText(ChangePin.this, "Hatalı cvv", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }
                        });

            }
        });






    }
}