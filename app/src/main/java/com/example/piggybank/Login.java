package com.example.piggybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        EditText userID = findViewById(R.id.user_id_tv);
        EditText userPass = findViewById(R.id.user_password_tv);
        Button login = findViewById(R.id.login_button);

        SharedPreferences sharedPreferences = getSharedPreferences("client", MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        FirebaseFirestore loginFirestore = FirebaseFirestore.getInstance();
        CollectionReference clientReference = loginFirestore.collection("Clients");
        TextView forgetPass = findViewById(R.id.forget_pass);

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgetPassword.class));
            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               Task<QuerySnapshot> query = clientReference.whereEqualTo("ID", userID.getText().toString())
                       .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                   @Override
                   public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       if(!queryDocumentSnapshots.getDocuments().isEmpty()){
                           ClientModel client = queryDocumentSnapshots.getDocuments().get(0).toObject(ClientModel.class);
                           if(client.getPassword()==Integer.parseInt(userPass.getText().toString())){
                               Gson gson = new Gson();
                               String json = gson.toJson(client);
                               sharedPreferencesEditor.putString("clientInfo",json);
                               sharedPreferencesEditor.apply();
                               startActivity(new Intent(Login.this,MainActivity.class));
                               finish();
                           }
                           else{
                               Toast.makeText(Login.this, "Parola hatalı!", Toast.LENGTH_SHORT).show();
                           }
                       }
                       else{
                           Toast.makeText(Login.this, "Kullanıcı bulunamadı!", Toast.LENGTH_SHORT).show();
                       }


                   }
               });


            }
        });

    }
}