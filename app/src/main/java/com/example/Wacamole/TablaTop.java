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
    ConstraintLayout filas;

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

                //GuideLines
                Guideline separador1 = findViewById(R.id.tablaLimit1);
                Guideline separador2 = findViewById(R.id.tablaLimit2);
                Guideline separador3 = findViewById(R.id.tablaLimit3);
                Guideline separador4 = findViewById(R.id.tablaLimit4);

                for (int i = 0; i < maxRegistros; i++) {
                    
                    TextView TV_UserName = new TextView(TablaTop.this);      TV_UserName.setText(cuentas.get(i)[0]);
                    TextView TV_AccountName = new TextView(TablaTop.this);   TV_AccountName.setText(cuentas.get(i)[1]);
                    TextView TV_Score = new TextView(TablaTop.this);         TV_Score.setText(cuentas.get(i)[2]);

                    filas.addView(TV_UserName);
                    filas.addView(TV_AccountName);
                    filas.addView(TV_Score);

                    /*ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(filas);

                    // Si es el primer TextView, anclarlo al inicio del ConstraintLayout
                    constraintSet.connect(TV_UserName.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                    constraintSet.connect(TV_AccountName.getId(), ConstraintSet.START, separador1.getId(), ConstraintSet.END);
                    constraintSet.connect(TV_Score.getId(), ConstraintSet.END, separador2.getId(), ConstraintSet.START);*/
                }
            }
        });
    }
}