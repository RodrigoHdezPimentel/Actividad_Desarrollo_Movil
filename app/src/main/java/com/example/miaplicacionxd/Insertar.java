package com.example.miaplicacionxd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Insertar extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar);
        
        Button insert = findViewById(R.id.Insert_Datos);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertValues();
            }
        });
    }

    public void changeToMain(View view) {
        Intent nuevoIntent = new Intent(Insertar.this, MainActivity.class);
        startActivity(nuevoIntent);
    }


    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    public void InsertValues() {
        //Leemos variables
        TextView UsernameTextView = findViewById(R.id.DB_UserName);
        TextView EmailTextView = findViewById(R.id.DB_Email);

        Map<String, String> user = new HashMap<>();
        user.put("Username", UsernameTextView.getText().toString());
        user.put("Email", EmailTextView.getText().toString());

        firestoreDB.collection("Usuarios")
                .document(UsernameTextView.getText().toString())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Insertar.this, "Insertado correctamente", Toast.LENGTH_SHORT).show();
                        Log.d("_Debug", "TODO ON");
                        UsernameTextView.setText("");
                        EmailTextView.setText("");
                       changeToMain();              //despues de ingresar los datos, lo envió al main
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Insertar.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", e.getMessage());
                    }
                });
    }


    public void changeToMain(){
        Intent nuevoIntent = new Intent(Insertar.this, MainActivity.class);
        startActivity(nuevoIntent);
    }
}


