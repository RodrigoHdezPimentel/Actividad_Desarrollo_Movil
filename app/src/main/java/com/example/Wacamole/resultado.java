package com.example.Wacamole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class resultado extends AppCompatActivity {
    String username = "";
    String puntuacion = "10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        ImageView goTop = findViewById(R.id.goTablatop);
        Button play = findViewById(R.id.toGame);
        Button menu = findViewById(R.id.toMenu);
        TextView newBest = findViewById(R.id.newBest);
        TextView result = findViewById(R.id.result);

        username = getIntent().getStringExtra("Nombre");
        puntuacion = getIntent().getStringExtra("puntuacion");

        newBest.setVisibility(View.INVISIBLE);
        result.setText("Tu puntuacion: " + puntuacion);

        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

        firestoreDB.collection("Cuentas").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc.get("UserName").equals(username) && doc.get("CuentaPrincipal").equals("true")) {
                                if (Integer.parseInt(doc.get("Highest Score").toString()) < Integer.parseInt(puntuacion)) {
                                    newBest.setVisibility(View.VISIBLE);
                                    Map<String, String> cuenta = new HashMap<>();
                                    cuenta.put("AccountName", doc.get("AccountName").toString());
                                    cuenta.put("CuentaPrincipal", doc.get("CuentaPrincipal").toString());
                                    cuenta.put("FotoPerfil", doc.get("FotoPerfil").toString());
                                    cuenta.put("Highest Score", puntuacion);
                                    cuenta.put("UserName", doc.get("UserName").toString());

                                    firestoreDB.collection("Cuentas")
                                            .document(doc.get("AccountName").toString())
                                            .set(cuenta);
                                }
                            }
                        }
                    }
                });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toPlay = new Intent(resultado.this, game.class);
                toPlay.putExtra("Nombre", username);
                startActivity(toPlay);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMenu = new Intent(resultado.this, menu.class);
                toMenu.putExtra("Nombre", username);
                startActivity(toMenu);

            }
        });

        goTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent toTabla = new Intent(resultado.this, TablaTop.class);
                //toTabla.putExtra("Nombre", username);
                //startActivity(toTabla);
            }
        });
    }
}