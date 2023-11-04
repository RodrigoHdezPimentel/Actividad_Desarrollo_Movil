package com.example.Wacamole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class showCuenta extends AppCompatActivity {
    String username;
    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cuenta);
        Intent usernamerecibido = getIntent();
        username = usernamerecibido.getStringExtra("Nombre");
        TextView userTextname = findViewById(R.id.Username_Text);
        userTextname.setText(username);
        //Para cuando le den click la flecha, te manda a la clase Cuenta.java
        ImageView ImgFlecha = findViewById(R.id.Flecha);
        ImgFlecha.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View view) {
                Intent intentToMenu = new Intent(showCuenta.this,Cuenta.class);
                intentToMenu.putExtra("Nombre", username);
                startActivity(intentToMenu);
            }
        });
        colorBorde();

        //SOLO FALTARIA REALIZAR EL LLAMADO DE LAS CUENTAS EN LA FIREBASE PARA EL SCROLL
        //Y EN DONDE DICE Pepito ahí se colocaria el Username DE LA PERSONA QUE INICIO SESION
        LinearLayout LLCuentas = findViewById(R.id.LLCuentas);
        LLCuentas.removeAllViews();
        firestoreDB.collection("Cuentas")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TextView NombreCuenta = new TextView(showCuenta.this);
                                NombreCuenta.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                NombreCuenta.setTextSize(25);
                                NombreCuenta.setPadding(0, 10,0,10);

                                if(document.get("UserName").equals(username)){
                                    NombreCuenta.setText(document.get("AccountName").toString());
                                    LLCuentas.addView(NombreCuenta);
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
    }

//Solamente sirve para poner un puto borde al texview que dice "Cuentas" xD
public void colorBorde(){
        //el borde puede ser para el scroll o para el textView
    //ScrollView scrollView = findViewById(R.id.scrollView);
    TextView textView = findViewById(R.id.String_cuenta);

    ShapeDrawable backgroundDrawable = new ShapeDrawable(new RectShape());
    backgroundDrawable.getPaint().setColor(getColor(R.color.background_light_green));

    ShapeDrawable borderDrawable = new ShapeDrawable(new RectShape());
    borderDrawable.getPaint().setStrokeWidth(6f);
    borderDrawable.getPaint().setStyle(Paint.Style.STROKE);
    borderDrawable.getPaint().setColor(getResources().getColor(android.R.color.black));

    Drawable[] layers = {backgroundDrawable, borderDrawable};
    LayerDrawable layerDrawable = new LayerDrawable(layers);

    textView.setBackground
            (layerDrawable);

}

}

