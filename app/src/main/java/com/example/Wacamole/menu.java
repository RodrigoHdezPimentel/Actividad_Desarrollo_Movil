package com.example.Wacamole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class menu extends AppCompatActivity {
String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent usernameRebido = getIntent();
        //Variables de la activity
        username = usernameRebido.getStringExtra("Nombre");
        ConstraintLayout Fondo = findViewById(R.id.Fondo);
        ImageView ImgFondo = findViewById(R.id.ImgFondo);
        ImageView Flecha = findViewById(R.id.backMenu);
        ImageView profileimg = findViewById(R.id.ProfileImg);
        TextView userTextname = findViewById(R.id.userTextViewName);
        TextView ClickToStart = findViewById(R.id.startbutton);
        Button Ajustes = findViewById(R.id.SettingsBut);
        Button Cuenta = findViewById(R.id.AcountBut);
        Button CerrarSesion = findViewById(R.id.cerrarSesion);

        userTextname.setText(username);

        //Boton a settings
        Ajustes.setVisibility(View.INVISIBLE);
        Ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentToSetting = new Intent(menu.this, Ajustes.class);
                intentToSetting.putExtra("Nombre", username);
                startActivity(intentToSetting);
            }
        });

        //Ir a ajustes cuenta
        Cuenta.setVisibility(View.INVISIBLE);
        Cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToCuenta = new Intent(menu.this, User.class);
                intentToCuenta.putExtra("Nombre", username);
                startActivity(intentToCuenta);
            }
        });

        //Cerrar sesion y te lleva al main
        CerrarSesion.setVisibility(View.INVISIBLE);
        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToMain = new Intent(menu.this, MainActivity.class);
                startActivity(intentToMain);
            }
        });
        //Boton para empezar el juego
        ClickToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToGame = new Intent(menu.this, game.class);
                startActivity(intentToGame);
            }
        });
        //Flecha quitar las opciones de cuenta, ajustes y cerrar sesion
        Flecha.setVisibility(View.INVISIBLE);
        Flecha.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                Fondo.setBackground(getDrawable(R.color.background_green));
                Ajustes.setVisibility(View.INVISIBLE);
                Cuenta.setVisibility(View.INVISIBLE);
                CerrarSesion.setVisibility(View.INVISIBLE);
                Flecha.setVisibility(View.INVISIBLE);
                ClickToStart.setEnabled(true);
                profileimg.setAlpha(1f);
                ImgFondo.setAlpha(1f);
                ClickToStart.setAlpha(1f);
                profileimg.setEnabled(true);
            }
        });
        //Boton para mostrar las opciones de cuenta, ajustes y cerrar sesion
        profileimg.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                Fondo.setBackground(getDrawable(R.color.background_light_green));
                Ajustes.setVisibility(View.VISIBLE);
                Cuenta.setVisibility(View.VISIBLE);
                CerrarSesion.setVisibility(View.VISIBLE);
                Flecha.setVisibility(View.VISIBLE);
                ClickToStart.setEnabled(false);
                profileimg.setAlpha(0.3f);
                ImgFondo.setAlpha(0.3f);
                ClickToStart.setAlpha(0.15f);
                profileimg.setEnabled(false);
            }
        });
    }
}