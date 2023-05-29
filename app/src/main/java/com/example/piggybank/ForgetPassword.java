package com.example.piggybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;

public class ForgetPassword extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    int CVV=0;
    int newPass=0;
    String cardNumber = null;


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
                try{
                 CVV = Integer.parseInt(cvvEt.getText().toString());
                newPass = Integer.parseInt(newPassEt.getText().toString());
                cardNumber = stringManipulation(cardNum.getText().toString(),4);
                }
                catch (Exception exception){

                    Toast.makeText(ForgetPassword.this, "Alanlardan biri ya da tamamı boş olamaz!", Toast.LENGTH_SHORT).show();

                }



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

                                        firebaseFirestore.collection("Clients")
                                                        .whereEqualTo("ID",ID)
                                                                .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if(task.isSuccessful()){
                                                                                    if(!task.getResult().getDocuments().isEmpty()) {
                                                                                        String regex = "^[1-9]\\d{5}$";
                                                                                        if(!Pattern.matches(regex,newPassEt.getText().toString())){
                                                                                            Toast.makeText(ForgetPassword.this, "Şifreniz sıfır ile başlamayan 6 haneli rakamlardan oluşmalıdır.", Toast.LENGTH_SHORT).show();
                                                                                            newPassEt.setText("");

                                                                                        }
                                                                                        else {
                                                                                            task.getResult().getDocuments().get(0).getReference().update("password", newPass);
                                                                                            Toast.makeText(ForgetPassword.this, "Şifre Güncellendi", Toast.LENGTH_SHORT).show();
                                                                                            startActivity(new Intent(ForgetPassword.this, Login.class));
                                                                                            finish();
                                                                                        }

                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        Toast.makeText(ForgetPassword.this, "Yanlış Kullanıcı", Toast.LENGTH_SHORT).show();
                                                                                    }

                                                                                }
                                                                                else
                                                                                {
                                                                                    Toast.makeText(ForgetPassword.this, "Bir hata oluştu ERR 76", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });

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