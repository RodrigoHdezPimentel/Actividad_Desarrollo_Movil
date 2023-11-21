package com.example.Wacamole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class TablaTop extends AppCompatActivity {

    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_top);
        ImageView flecha = findViewById(R.id.flecha);


        firestoreDB.collection("Cuentas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                TextView datosCuenta = new TextView(TablaTop.this);
                datosCuenta.setTextSize(25);


            }
        });

        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goResultado = new Intent(TablaTop.this, MainActivity.class);
                startActivity(goResultado);

            }
        });


    }
}