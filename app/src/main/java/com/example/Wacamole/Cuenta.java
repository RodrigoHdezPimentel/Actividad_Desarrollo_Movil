package com.example.Wacamole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
                newAccount(accountName);
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
}