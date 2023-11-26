package com.example.Wacamole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TablaTop extends AppCompatActivity {

    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    final int MAX_TOP=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_top);
        ImageView flecha = findViewById(R.id.flecha);
        ConstraintLayout columnas = findViewById(R.id.columasDatos);

        firestoreDB.collection("Cuentas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot list) {
                ArrayList<String> cuentas= new ArrayList<>();
                columnas.removeAllViews();

                TextView cuenta = findViewById(R.id.cuenta0);
                TextView puntaje = findViewById(R.id.puntaje0);
                TextView usuario = new TextView(TablaTop.this);
                TextView usuario2 = new TextView(TablaTop.this);

                for(int i = 0; i < list.size() && i < MAX_TOP; i++){
                    int score = 0;
                    String accountName = "";

                    for(QueryDocumentSnapshot document : list) {
                        Boolean cuentaRepetida = false;

                        for (int c = 0; c < cuentas.size(); c++) {
                            if (document.get("AccountName").toString().equals(cuentas.get(c))) {
                                cuentaRepetida = true;
                                break;
                            }
                        }

                        if (!cuentaRepetida) {
                            if (score < Integer.parseInt(document.get("Highest Score").toString())) {
                                score = Integer.parseInt(document.get("Highest Score").toString());
                                accountName = document.get("AccountName").toString();
                            }
                        }

                    }
                    cuentas.add(accountName);
                    // COLOCACIONNNNNN
                    int textID=100;
                    TextView textView = new TextView(TablaTop.this);
                    textView.setId(textID+ i); // Establecer un ID único para cada TextView
                    textView.setPadding(0, 15,0,15);
                   //textView.setGravity(Gravity.CENTER); // Alineación del texto al centro

                    //textView.setBackgroundColor(getResources().getColor(R.color.background_Superdark_green));
                    //textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setText(accountName); // Establecer el texto del TextView


                    // Añadir TextView al ConstraintLayout
                    columnas.addView(textView);

                    // Configurar restricciones para posicionar los TextViews uno debajo del otro
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(columnas);

                    if (i == 0) {
                        // Si es el primer TextView, anclarlo al inicio del ConstraintLayout
                        constraintSet.connect(textView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                        constraintSet.connect(textView.getId(), ConstraintSet.START, R.id.tablaLimit2_interno, ConstraintSet.END);
                        constraintSet.connect(textView.getId(), ConstraintSet.END, R.id.tablaLimit3_interno, ConstraintSet.START);


                    } else {
                        // Si no es el primer TextView, anclarlo debajo del TextView anterior
                        constraintSet.connect(textView.getId(), ConstraintSet.TOP, textID + (i - 1), ConstraintSet.BOTTOM);
                    }

                    // Aplicar las restricciones configuradas al ConstraintLayout
                    constraintSet.applyTo(columnas);
                //---------------------------------------------------------------
                }


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