package com.example.Wacamole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    EditText nameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView error = findViewById(R.id.error);
        nameText = findViewById(R.id.nameText);
        Button Login = findViewById(R.id.Login);

        Button register = findViewById(R.id.Register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(MainActivity.this, Insertar.class);
                startActivity(newIntent);
            }
        });

        firestoreDB.collection("Usuarios")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            error.setVisibility(View.INVISIBLE);
                            Login.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onClick(View v) {
                                        if (registroEncontrado(task, nameText)) {
                                            //buscarCuentaPrincipal(nameText);                                            error.setVisibility(View.INVISIBLE);
                                            goToMenu(nameText);
                                        } else {
                                            error.setVisibility(View.VISIBLE);
                                            error.setText("USUARIO NO ENCONTRADO");
                                        }
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setVisibility(View.VISIBLE);
                        error.setText("NO SE PUDO ACCEDER A LA BASE DE DATOS");
                    }
                });
    }

    public boolean registroEncontrado(Task<QuerySnapshot> task, TextView tv) {
        boolean encontrado = false;

        for (QueryDocumentSnapshot document : task.getResult()) {
            if(Objects.equals(document.get("Username"), tv.getText().toString())){
                encontrado = true;
                break;
            }
        }
        return encontrado;
    }
    String username;
    public void goToMenu(EditText tv){
        Intent intentToMenu = new Intent(MainActivity.this, menu.class);
        intentToMenu.putExtra("Nombre", username);
        startActivity(intentToMenu);
    }
    public void buscarCuentaPrincipal(EditText tv){

        firestoreDB.collection("Cuentas")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //Comprobar cual es la cuenta principal
                            Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    //public void goToShowCuenta(View view){
       // Intent intentToMenu = new Intent(MainActivity.this,showCuenta.class);
        //startActivity(intentToMenu);
   // }
    public void toPrueba(){
        Intent FBIntent = new Intent(MainActivity.this, FireBase_Prueba.class);
        startActivity(FBIntent);
    }
}