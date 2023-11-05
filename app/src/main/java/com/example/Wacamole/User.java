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

public class User extends AppCompatActivity {
String username;
FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent usernamerecibido = getIntent();
        username = usernamerecibido.getStringExtra("Nombre");

        //CAMBIOS MIOS (diego)--------------- ACÄ LO QUE HAGO ES PARA COLOCAR EL EMAIL EN EDIT TEXT
            firestoreDB.collection("Usuarios")
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
            if(document.get("Username").equals(username)){
             EditText textEmial = findViewById(R.id.DB_Email);
            textEmial.setText(document.get("Email").toString());
            //ACÁ LE COLOCO EL USERNAME EN EL EDITTEXT
            EditText textUsername= findViewById(R.id.DB_UserName);
            textUsername.setText(username);

                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
            //BOTON DE IR A LA CLASE CUENTA CUANDO LE DAN CLICK AÑADIR CUENTA
            Button goCuenta = findViewById(R.id.newAccountBut);
            goCuenta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goCuenta = new Intent(User.this, Cuenta.class);
                    goCuenta.putExtra("Nombre",username);
                    startActivity(goCuenta);
                }
            });

        //----------------------
// DAR CLICK A LAS IMGANES PEQUEÑAS TE LLEVA A LA CLASE SHOWCUENTAS
        ImageView cuentas = findViewById(R.id.cuenta_1);
        cuentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goShow = new Intent(User.this, showCuenta.class);
                goShow.putExtra("Nombre", username);
                startActivity(goShow);
            }
        });
        cuentas = findViewById(R.id.cuenta_2);
        cuentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goShow = new Intent(User.this, showCuenta.class);
                goShow.putExtra("Nombre", username);
                startActivity(goShow);
            }
        });
        //-----------------------------------------------------------------
        Button but = findViewById(R.id.goto_show_Cuentas);
        but.setText(username);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goShow = new Intent(User.this, showCuenta.class);
                goShow.putExtra("Nombre", username);
                startActivity(goShow);
            }
        });

    }


}