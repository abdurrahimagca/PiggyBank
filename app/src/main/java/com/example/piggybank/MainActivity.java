package com.example.piggybank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    BankCardModel bankCard = new BankCardModel();
    ArrayList<CardInfoModel> cards = new ArrayList<>();
    ClientModel client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView welcomeText = findViewById(R.id.welcome_text_name_tv);
        TextView ibanTV = findViewById(R.id.iban_num_tv);
        TextView balanceTV = findViewById(R.id.balance_tv_main);
        RecyclerView cardRecyclerView = findViewById(R.id.card_rv);


        MainRecyclerViewAdapter recyclerViewAdapter = new MainRecyclerViewAdapter(cards,this,this);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        cardRecyclerView.setHasFixedSize(true);
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));
        cardRecyclerView.setAdapter(recyclerViewAdapter);
        Gson gson = new Gson();
        String json = getSharedPreferences("client",MODE_PRIVATE).getString("clientInfo","");
        client = gson.fromJson(json, ClientModel.class);
        welcomeText.setText(client.getName());
        SharedPreferences sharedPreferences = getSharedPreferences("balance", MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesBalanceEditor = sharedPreferences.edit();

        firebaseFirestore.collection("CardInfo")
                .whereEqualTo("ID", client.getID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list){
                            cards.add(d.toObject(CardInfoModel.class));
                        }
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                });
        firebaseFirestore.collection("Accounts")
                .whereEqualTo("ID", client.getID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.getDocuments().isEmpty())

                        {
                            AccountModel accountModel = new AccountModel();
                            accountModel = queryDocumentSnapshots.getDocuments().get(0).toObject(AccountModel.class);
                            ibanTV.setText(accountModel.getIBAN());
                            balanceTV.setText(String.valueOf(accountModel.getBalance()));
                            sharedPreferencesBalanceEditor.putInt("balanceVal", accountModel.getBalance());
                            sharedPreferencesBalanceEditor.apply();
                        }
                    }
                });

        ImageButton sendMoneyButton = findViewById(R.id.send_money_button);

        ImageButton payOffDebtButton = findViewById(R.id.pay_off_debt_button);


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

                startActivity(new Intent(MainActivity.this, payOffDebt.class));
                finish();
            }
        });
    }

    @Override
    public void itemClickListener(int position) {
        Intent intent = new Intent(MainActivity.this,CardSettings.class);
        intent.putExtra("cardID", cards.get(position).getCardID());
        intent.putExtra("clientID", client.getID());
        intent.putExtra("cardType", cards.get(position).getCardType());
        startActivity(intent);


    }
}