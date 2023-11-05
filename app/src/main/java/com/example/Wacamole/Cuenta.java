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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Cuenta extends AppCompatActivity {
String username;
FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);
        Intent usernamerecibido = getIntent();
        username = usernamerecibido.getStringExtra("Nombre");

        EditText accountName = findViewById(R.id.AccountName);

        Button confirmar = findViewById(R.id.confirm_button);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreDB.collection("Cuentas")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!registroEncontrado(task, accountName)) {
                                        newAccount(accountName);
                                    } else {
                                        Toast.makeText(Cuenta.this, "Nombre Duplicado", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onFailure(@NonNull Exception e) {


                                Toast.makeText(Cuenta.this, "ERROR EN LA CONEXION", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    public void newAccount(EditText name){

        Map<String, String> cuenta = new HashMap<>();
        cuenta.put("AccountName", name.getText().toString());
        cuenta.put("CuentaPrincipal", "false");//   ACÁ LE DIGO QUE ESA CUENTA NUEVA ES FALSE
        cuenta.put("FotoPerfil", "0");
        cuenta.put("Highest Score", "0");
        cuenta.put("UserName", username); //PARA INDICAR AL USUARIO

        firestoreDB.collection("Cuentas")
                .document()
                .set(cuenta)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Cuenta.this, "Exito al crear cuenta", Toast.LENGTH_SHORT).show();
                        Intent goUser = new Intent(Cuenta.this, User.class);
                        goUser.putExtra("Nombre", username);
                        startActivity(goUser);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Cuenta.this, "Error al crear cuenta", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public boolean registroEncontrado(Task<QuerySnapshot> task, TextView tv) {
        final boolean[] encontrado = {false};

        for (QueryDocumentSnapshot document : task.getResult()) {
            if (Objects.equals(document.get("AccountName"), tv.getText().toString())) {
                encontrado[0] = true;
                break;
            }

        }
        return encontrado[0];
    }
}