package com.example.piggybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class CardSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_settings);
        String clientID = getIntent().getStringExtra("clientID");
        int NO_CARD = -1;
        int cardID = getIntent().getIntExtra("cardID", NO_CARD);
        String cardType = getIntent().getStringExtra("cardType");
        TextView cardNumTV = findViewById(R.id.card_set_num_tv);
        TextView expDateTV = findViewById(R.id.exp_date_cv);
        TextView CVV = findViewById(R.id.cvv_cv);
        TextView debt = findViewById(R.id.cs_debt_tv);
        TextView limit = findViewById(R.id.cs_limit_tv);
        Button payDebt = findViewById(R.id.cs_payoff_button);
        TextView changePin = findViewById(R.id.change_pin);
        CardView creditInfo = findViewById(R.id.credit_card_info_cv);
        creditInfo.setVisibility(View.GONE);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(cardType)
                .whereEqualTo("cardID", cardID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(!task.getResult().getDocuments().isEmpty()){
                                if(cardType.equals("BankCard")){
                                    BankCardModel bankCard = task.getResult().getDocuments()
                                            .get(0).toObject(BankCardModel.class);
                                    cardNumTV.setText(bankCard.getCardNum());
                                    expDateTV.setText(bankCard.getExpDate());
                                    CVV.setText(String.valueOf(bankCard.getCVV()));
                                    changePin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent =  new Intent(CardSettings.this, ChangePin.class);
                                            intent.putExtra("cardNum",bankCard.getCardNum());
                                            intent.putExtra("cardType", "BankCard");
                                            startActivity(intent);




                                        }
                                    });
                                } else if (cardType.equals("CreditCard")) {

                                    CreditCardModel creditCard = task.getResult().getDocuments()
                                            .get(0).toObject(CreditCardModel.class);
                                    cardNumTV.setText(creditCard.getCardNum());
                                    expDateTV.setText(creditCard.getExpDate());
                                    CVV.setText("***");
                                    debt.setText(String.valueOf(creditCard.getDebt()));
                                    limit.setText(String.valueOf(creditCard.getCardLimit()));
                                    creditInfo.setVisibility(View.VISIBLE);
                                    changePin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent =  new Intent(CardSettings.this, ChangePin.class);
                                            intent.putExtra("cardNum",creditCard.getCardNum());
                                            intent.putExtra("cardType", "CreditCard");
                                            startActivity(intent);




                                        }
                                    });





                                }


                            }
                        }
                    }
                });
        payDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CardSettings.this, payOffCreditCardDebt.class));
            }
        });





    }
}