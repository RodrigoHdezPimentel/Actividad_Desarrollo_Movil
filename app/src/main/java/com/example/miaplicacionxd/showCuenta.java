package com.example.miaplicacionxd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class showCuenta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cuenta);
        //Para cuando le den click la flecha, te manda a la clase Cuenta.java
        ImageView ImgFlecha = findViewById(R.id.Flecha);
    ImgFlecha.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intentToMenu = new Intent(showCuenta.this,Cuenta.class);
        startActivity(intentToMenu);
                }
            });
        colorBorde();

        //SOLO FALTARIA REALIZAR EL LLAMADO DE LAS CUENTAS EN LA FIREBASE PARA EL SCROLL
        //Y EN DONDE DICE Pepito ahí se colocaria el Username DE LA PERSONA QUE INICIO SESION
    }

//Solamente sirve para poner un puto borde al texview que dice "Cuentas" xD
public void colorBorde(){
        //el borde puede ser para el scroll o para el textView
    //ScrollView scrollView = findViewById(R.id.scrollView);
    TextView textView = findViewById(R.id.String_cuenta);

    ShapeDrawable backgroundDrawable = new ShapeDrawable(new RectShape());
    backgroundDrawable.getPaint().setColor(getResources().getColor(R.color.background_light_green));

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

