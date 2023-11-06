package com.example.Wacamole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Insertar extends AppCompatActivity {
    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    TextView error;
    TextView UsernameTextView;
    TextView EmailTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar);

        error = findViewById(R.id.ErrorText);
        error.setVisibility(View.INVISIBLE);

        Button insert = findViewById(R.id.Insert_Datos);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertValues();
            }
        });
        Button cancel = findViewById(R.id.Cancelar);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToMain();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void InsertValues() {

        //Leemos variables
        UsernameTextView = findViewById(R.id.DB_UserName);
        EmailTextView = findViewById(R.id.DB_Email);

        firestoreDB.collection("Usuarios")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!registroEncontrado(task, UsernameTextView)) {
                                insertarNewuser(UsernameTextView, EmailTextView);
                                changeToCuenta();
                            } else {
                                error.setVisibility(View.VISIBLE);
                                error.setText("NOMBRE DE USUARIO YA EXISTENTE");
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setVisibility(View.VISIBLE);
                        error.setText("ERROR EN LA CONEXION A LA BASE DE DATOS");
                        Toast.makeText(Insertar.this, "ERROR EN LA CONEXION", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public boolean registroEncontrado(Task<QuerySnapshot> task, TextView tv) {
        final boolean[] encontrado = {false};
        Toast.makeText(this, "Entrado", Toast.LENGTH_SHORT).show();

        for (QueryDocumentSnapshot document : task.getResult()) {
            if (Objects.equals(document.get("Username"), tv.getText().toString())) {
                encontrado[0] = true;
                break;
            }

        }
        return encontrado[0];
    }
    @SuppressLint("SetTextI18n")
    public void insertarNewuser(TextView UsernameTextView, TextView EmailTextView) {
        if (EmailTextView.getText().toString().equals("")
                || UsernameTextView.getText().toString().equals("")) {
            error.setVisibility(View.VISIBLE);
            error.setText("RELLENE TODOS LOS CAMPOS");
        } else {
            Map<String, String> user = new HashMap<>();
            user.put("Username", UsernameTextView.getText().toString());
            user.put("Email", EmailTextView.getText().toString());

            firestoreDB.collection("Usuarios")
                    .document(UsernameTextView.getText().toString())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            error.setVisibility(View.INVISIBLE);
                            Log.d("_Debug", "TODO ON");
                            UsernameTextView.setText("");
                            EmailTextView.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            error.setVisibility(View.VISIBLE);
                            error.setText("ERROR EN LA CONEXION A LA BASE DE DATOS");
                            Toast.makeText(Insertar.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void changeToMain() {
        Intent nuevoIntent = new Intent(Insertar.this, MainActivity.class);
        startActivity(nuevoIntent);
    }
    public void changeToCuenta() {
        Boolean newCuenta = true;
        Intent nuevoIntent = new Intent(Insertar.this, Cuenta.class);
        nuevoIntent.putExtra("Nombre", UsernameTextView.getText().toString());
        nuevoIntent.putExtra("userNew", newCuenta);
        startActivity(nuevoIntent);
    }
}