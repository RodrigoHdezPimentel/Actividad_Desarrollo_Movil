package com.example.Wacamole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
            @SuppressLint({"ResourceAsColor", "ResourceType"})
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
                    Guideline separador1 = new Guideline(TablaTop.this);
                    separador1.setId(View.generateViewId());
                    constraintSet.setGuidelinePercent(separador1.getId(), 0.33f);
                    column.addView(separador1);

                    Guideline separador2 = new Guideline(TablaTop.this);
                    separador2.setId(View.generateViewId());
                    constraintSet.setGuidelinePercent(separador2.getId(), 0.66f);
                    column.addView(separador2);

                    //TextView con el nombre de user
                    TextView TV_UserName = new TextView(TablaTop.this);
                    TV_UserName.setId(View.generateViewId());
                    TV_UserName.setText(cuentas.get(i)[0]);
                    TV_UserName.setWidth(270);
                    TV_UserName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    TV_UserName.setPadding(0, 25, 0, 25);
                    column.addView(TV_UserName);

                    //TextView con el nombre de Cuenta
                    TextView TV_AccountName = new TextView(TablaTop.this);
                    TV_AccountName.setId(View.generateViewId());
                    TV_AccountName.setText(cuentas.get(i)[1]);
                    TV_AccountName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    TV_AccountName.setWidth(600);
                    TV_AccountName.setPadding(300, 25, 0, 25);
                    column.addView(TV_AccountName);

                    //TextView con el Max Score
                    TextView TV_Score = new TextView(TablaTop.this);
                    TV_Score.setId(View.generateViewId());
                    TV_Score.setText(cuentas.get(i)[2]);
                    TV_Score.setWidth(860);
                    TV_Score.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    TV_Score.setPadding(600, 25, 0, 25);
                    column.addView(TV_Score);

                    //AMrging entre registros
                    ShapeDrawable backgroundDrawable = new ShapeDrawable(new RectShape());
                    backgroundDrawable.getPaint().setColor(getResources().getColor(R.color.white));

                    ShapeDrawable borderDrawable = new ShapeDrawable(new RectShape());
                    borderDrawable.getPaint().setStrokeWidth(12f);
                    borderDrawable.getPaint().setStyle(Paint.Style.STROKE);
                    borderDrawable.getPaint().setColor(getResources().getColor(R.color.background_light_green));


                    Drawable[] layers = {backgroundDrawable, borderDrawable};
                    LayerDrawable layerDrawable = new LayerDrawable(layers);

                    column.setBackground(layerDrawable);

                    filas.addView(column);



                    // Si es el primer TextView, anclarlo al inicio del ConstraintLayout
                    //                    Objetivo           Lado del objetivo     Enlace           lado del enlace
                    /*constraintSet.connect(TV_UserName.getId(), ConstraintSet.START, column.getId(),constraintSet.START);
                    constraintSet.connect(TV_UserName.getId(), constraintSet.END, separador1.getId(), constraintSet.VERTICAL_GUIDELINE);
                    constraintSet.connect(TV_UserName.getId(), constraintSet.TOP, column.getId(), constraintSet.TOP);
                    constraintSet.connect(TV_UserName.getId(), constraintSet.BOTTOM, column.getId(), constraintSet.BOTTOM);

                    constraintSet.connect(TV_AccountName.getId(), ConstraintSet.START, separador1.getId(),constraintSet.VERTICAL_GUIDELINE);
                    constraintSet.connect(TV_AccountName.getId(), constraintSet.END, separador2.getId(), constraintSet.VERTICAL_GUIDELINE);
                    constraintSet.connect(TV_AccountName.getId(), constraintSet.TOP, column.getId(), constraintSet.TOP);
                    constraintSet.connect(TV_AccountName.getId(), constraintSet.BOTTOM, column.getId(), constraintSet.BOTTOM);

                    constraintSet.connect(TV_Score.getId(), ConstraintSet.START, separador2.getId(),constraintSet.VERTICAL_GUIDELINE);
                    constraintSet.connect(TV_Score.getId(), constraintSet.END, column.getId(), constraintSet.END);
                    constraintSet.connect(TV_Score.getId(), constraintSet.TOP, column.getId(), constraintSet.TOP);
                    constraintSet.connect(TV_Score.getId(), constraintSet.BOTTOM, column.getId(), constraintSet.BOTTOM);*/

                    // Aplica las restricciones al ConstraintLayout
                    constraintSet.applyTo(column);
                }
                filas.setPadding(14,7,14,7);

            }
        });
    }
}