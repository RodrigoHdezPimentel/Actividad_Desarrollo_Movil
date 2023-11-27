package com.example.Wacamole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.PipedInputStream;
import java.util.ArrayList;

public class TablaTop extends AppCompatActivity {

    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    final int MAX_TOP = 10;
    ArrayList<String[]> cuentas = new ArrayList<String[]>();
    LinearLayout filas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_top);
        ImageView flecha = findViewById(R.id.flecha);
        filas = findViewById(R.id.Filas);

        ordenarPodio();





        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goResultado = new Intent(TablaTop.this, MainActivity.class);
                startActivity(goResultado);

            }
        });


    }

    public void ordenarPodio() {
        firestoreDB.collection("Cuentas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (cuentas.size() > 0) {
                        for (int x = 0; x < cuentas.size(); x++) {
                            if (Integer.parseInt(doc.get("Highest Score").toString()) > Integer.parseInt(cuentas.get(x)[2])) {
                                cuentas.add(x, new String[]{doc.get("UserName").toString(),
                                                            doc.get("AccountName").toString(),
                                                            doc.get("Highest Score").toString()});
                                break;
                            } else if (x == cuentas.size() - 1) {
                                cuentas.add(new String[]{doc.get("UserName").toString(),
                                                         doc.get("AccountName").toString(),
                                                         doc.get("Highest Score").toString()});
                                break;
                            }
                        }
                    } else {
                        cuentas.add(new String[]{doc.get("UserName").toString(),
                                                 doc.get("AccountName").toString(),
                                                 doc.get("Highest Score").toString()});
                    }
                }
                int maxRegistros = 25;
                if (cuentas.size() < 25) {
                    maxRegistros = cuentas.size();
                }

                for (int i = 0; i < maxRegistros; i++) {
                    ConstraintLayout column = new ConstraintLayout(TablaTop.this);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(column);
                    //GuideLines
                    Guideline separador1 = new Guideline(TablaTop.this);    Guideline separador2 = new Guideline(TablaTop.this);
                    column.addView(separador1);     column.addView(separador2);
                    separador1.setGuidelinePercent(0.33f);     separador2.setGuidelinePercent(0.66f);

                    TextView TV_UserName = new TextView(TablaTop.this);      TV_UserName.setText(cuentas.get(i)[0]);
                    TextView TV_AccountName = new TextView(TablaTop.this);   TV_AccountName.setText(cuentas.get(i)[1]);
                    TextView TV_Score = new TextView(TablaTop.this);         TV_Score.setText(cuentas.get(i)[2]);

                    column.addView(TV_UserName);
                    column.addView(TV_AccountName);
                    column.addView(TV_Score);

                    // Si es el primer TextView, anclarlo al inicio del ConstraintLayout
                    //                    Objetivo           Lado del objetivo     Enlace           lado del enlace
                    constraintSet.connect(TV_UserName.getId(), ConstraintSet.TOP, TV_Score.getId(), ConstraintSet.BOTTOM);
                    constraintSet.connect(TV_UserName.getId(), ConstraintSet.START, column.getId(), ConstraintSet.START);
                    constraintSet.connect(TV_UserName.getId(), ConstraintSet.END, column.getId(), ConstraintSet.END);
                    constraintSet.connect(TV_UserName.getId(), ConstraintSet.BOTTOM, column.getId(), ConstraintSet.BOTTOM);

                    // Aplica las restricciones al ConstraintLayout
                    constraintSet.applyTo(column);
                    filas.addView(column);
                }
            }
        });
    }
}