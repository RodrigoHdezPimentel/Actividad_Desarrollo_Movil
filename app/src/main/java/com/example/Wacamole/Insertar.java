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
        //Variables activity
        Button cancel = findViewById(R.id.Cancelar);
        Button insert = findViewById(R.id.Insert_Datos);
        error = findViewById(R.id.ErrorText);
        error.setVisibility(View.INVISIBLE);

        //Boton para insertar usuario
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertValues();
            }
        });
        //Boton para volver al main sin crear user
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nuevoIntent = new Intent(Insertar.this, MainActivity.class);
                startActivity(nuevoIntent);
            }
        });
    }
    //Metodo para insertar los valores
    @SuppressLint("SetTextI18n")
    public void InsertValues() {
        //Leemos variables
        UsernameTextView = findViewById(R.id.DB_UserName);
        EmailTextView = findViewById(R.id.DB_Email);

        //Conexion a usuarios para insertar los valores
        firestoreDB.collection("Usuarios")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //Comprueba usuarios repetidos
                            if (!registroEncontrado(task, UsernameTextView)) {
                                //Comprueba registros vacios
                                if (EmailTextView.getText().toString().isEmpty()
                                        || UsernameTextView.getText().toString().isEmpty()) {
                                    error.setVisibility(View.VISIBLE);
                                    error.setText("RELLENE TODOS LOS CAMPOS");
                                //Si esta bien rellenado, inserta los valores
                                } else {
                                    Map<String, String> user = new HashMap<>();
                                    user.put("Username", UsernameTextView.getText().toString().trim());
                                    user.put("Email", EmailTextView.getText().toString().trim());

                                    firestoreDB.collection("Usuarios")
                                            .document(UsernameTextView.getText().toString())
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    error.setVisibility(View.INVISIBLE);
                                                    Log.d("_Debug", "TODO ON");
                                                    changeToCuenta();
                                                    UsernameTextView.setText("");
                                                    EmailTextView.setText("");
                                                }
                                            });
                                }
                            } else {
                                error.setVisibility(View.VISIBLE);
                                error.setText("NOMBRE DE USUARIO YA EXISTENTE");
                            }
                        }
                    }
                });
    }
    //Metodo para encontrar registro duplicado
    public boolean registroEncontrado(Task<QuerySnapshot> task, TextView tv) {
        boolean encontrado = false;
        //Recorre el registro de datos buscando coincidencias
        for (QueryDocumentSnapshot document : task.getResult()) {
            if (Objects.equals(document.get("Username"), tv.getText().toString().trim())) {
                encontrado = true;
                break;
            }
        }
        return encontrado;
    }

    //Cambio a cuenta con los datos necesarios
    public void changeToCuenta() {
        Boolean newCuenta = true;
        Intent nuevoIntent = new Intent(Insertar.this, Cuenta.class);
        nuevoIntent.putExtra("Nombre", UsernameTextView.getText().toString());
        nuevoIntent.putExtra("userNew", newCuenta);
        startActivity(nuevoIntent);
    }
}