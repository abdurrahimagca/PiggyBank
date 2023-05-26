package com.example.piggybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgetPassword extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        EditText IDet = findViewById(R.id.fp_ID);
        EditText cvvEt = findViewById(R.id.fp_cvv);
        EditText newPassEt = findViewById(R.id.fp_new_pass);
        EditText cardNum = findViewById(R.id.fp_card_num);
        Button setPass = findViewById(R.id.set_pass);



        setPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = IDet.getText().toString();
                int CVV = Integer.parseInt(cvvEt.getText().toString());
                int newPass = Integer.parseInt(newPassEt.getText().toString());
                String cardNumber = stringManipulation(cardNum.getText().toString(),4);

                firebaseFirestore = FirebaseFirestore.getInstance();

                firebaseFirestore.collection("BankCard")
                        .whereEqualTo("ID", ID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(!task.getResult().getDocuments().isEmpty())
                                {
                                    BankCardModel bankCard = task.getResult().getDocuments().get(0).toObject(BankCardModel.class);
                                    if(cardNumber.equals(bankCard.getCardNum()) && CVV == bankCard.getCVV())
                                    {
                                        //todo: update passsword at clients field
                                        startActivity(new Intent(ForgetPassword.this, Login.class));
                                    }
                                    else{
                                        Toast.makeText(ForgetPassword.this, "Hatalı Kart Bilgisi!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else{
                                    Toast.makeText(ForgetPassword.this, "Hatalı Kimlik No!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

    }
    public String stringManipulation(String input, int interval) {
        StringBuilder builder = new StringBuilder(input);




        int offset = 0;

        for (int i = interval; i < builder.length(); i += interval) {
            builder.insert(i + offset, '-');
            offset++;
        }
        builder.deleteCharAt(builder.length()-1);

        return builder.toString();
    }
}