package com.example.Wacamole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

Boolean firstAccount;
FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);
        Intent usernamerecibido = getIntent();
        Intent firstRecibido = getIntent();
        //Variables de la activity
        username = usernamerecibido.getStringExtra("Nombre").trim();
        firstAccount = firstRecibido.getBooleanExtra("userNew", false);
        EditText accountName = findViewById(R.id.AccountName);
        ImageView flecha = findViewById(R.id.flecha);
        Button confirmar = findViewById(R.id.confirm_button);

        //Si queremos obligar al usuario a crear una cuenta, escondemos la flecha
        if(firstAccount){
            flecha.setVisibility(View.INVISIBLE);
        }
        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toUser = new Intent(Cuenta.this, User.class);
                toUser.putExtra("Nombre", username);
                startActivity(toUser);
            }
        });
        //Boton para confirmar la creacion de la cuenta
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreDB.collection("Cuentas")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    //Si el registro no esta repetido, se crea la cuenta nueva
                                    if (!registroEncontrado(task, accountName)) {
                                        newAccount(accountName);
                                    } else {
                                        Toast.makeText(Cuenta.this, "Nombre Duplicado", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

            }
        });
    }
    //Metodo de creacion de cuenta nueva
    public void newAccount(EditText name){
        Map<String, String> cuenta = new HashMap<>();

        //Validacion para registros vacios
        if(name.getText().toString().isEmpty()){
            Toast.makeText(this, "Coloca un nombre", Toast.LENGTH_SHORT).show();
        }else{
            cuenta.put("AccountName", name.getText().toString().trim());
            cuenta.put("FotoPerfil", "0");
            cuenta.put("Highest Score", "0");
            cuenta.put("UserName", username);

            //Si es la primera cuenta creada, se asigna como principal
            if (firstAccount){
                cuenta.put("CuentaPrincipal", "true");
            }else{
                cuenta.put("CuentaPrincipal", "false");
            }
            //Creamos la cuenta nueva
            firestoreDB.collection("Cuentas")
                    .document(cuenta.get("AccountName"))
                    .set(cuenta)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void unused) {
                            //Depende de cuando creemos la cuenta, guiamos al usuario a un sitio o a otro
                            Toast.makeText(Cuenta.this, "Cuenta creada", Toast.LENGTH_SHORT).show();
                            if(firstAccount){
                                Intent goUser = new Intent(Cuenta.this, menu.class);
                                goUser.putExtra("Nombre", username);
                                startActivity(goUser);
                            }else{
                                Intent goUser = new Intent(Cuenta.this, User.class);
                                goUser.putExtra("Nombre", username);
                                startActivity(goUser);
                            }
                        }
                    });
        }
    }
    //Metodo para buscar registrosduplicados
    public boolean registroEncontrado(Task<QuerySnapshot> task, TextView tv) {
        boolean encontrado = false;
        //Leemos los registros buscando coincidencias
        for (QueryDocumentSnapshot document : task.getResult()) {
            if (Objects.equals(document.get("AccountName"), tv.getText().toString())) {
                encontrado = true;
                break;
            }
        }
        return encontrado;
    }

}