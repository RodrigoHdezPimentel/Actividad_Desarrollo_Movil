package com.example.Wacamole;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class FireBase_Prueba extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base_prueba);
        leerDatos();
    }
    public void leerDatos(){
        ArrayList <String> Nombres = new ArrayList<>();
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

        firestoreDB.collection("miaplicacionxD")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        LinearLayout Registros = findViewById(R.id.LLRegistro);
                        Registros.removeAllViews();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Nombres.add(Objects.requireNonNull(document.getData().get("name")).toString() + ","
                                            + Objects.requireNonNull(document.getData().get("surname")).toString() + ","
                                            + Objects.requireNonNull(document.getData().get("point")).toString());

                                //Creo el textView y le doy estilos
                                TextView showInfo = new TextView(FireBase_Prueba.this);
                                showInfo.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                showInfo.setTextSize(20);
                                showInfo.setBackground(getDrawable(R.color.purple_200));
                                showInfo.setPadding(0,22,0,22);

                                //Creo un string con los datos del jugador, creo que seria mejor crear un objeto que guardar asi los datos
                                String[] Persona = Nombres.get(Nombres.size()-1).split(",");
                                //Introduzco el texto y lo añado al scrollView
                                showInfo.setText("Nombre = " + Persona[0] + " " + Persona[1]
                                                + "\nPuntos = " + Persona[2]);

                                //Boton de eliminar registro
                                Button delete = new Button(FireBase_Prueba.this);
                                delete.setTextSize(20);
                                delete.setText("Eliminar " + Persona[0] + " " + Persona[1]);
                                delete.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                delete.setTextColor(getColor(R.color.error));
                                delete.setBackground(getDrawable(R.color.error_bacground));
                                delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        firestoreDB.collection("miaplicacionxD")
                                                .document(Persona[0] + "_" + Persona[1])
                                                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        leerDatos();
                                                        Toast.makeText(FireBase_Prueba.this, "Registro eliminado con exito", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(FireBase_Prueba.this, "El registro no se ha podido eliminar", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                                Registros.addView(showInfo);
                                Registros.addView(delete);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    public void toMain(View v){
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
    }
}