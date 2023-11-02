package com.example.miaplicacionxd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button Ajustes = findViewById(R.id.SettingsBut);
        Ajustes.setVisibility(View.INVISIBLE);
        Ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToGame = new Intent(menu.this, Ajustes.class);
                startActivity(intentToGame);
            }
        });
        Button Cuenta = findViewById(R.id.AcountBut);
        Cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToGame = new Intent(menu.this, Cuenta.class);
                startActivity(intentToGame);
            }
        });
        Cuenta.setVisibility(View.INVISIBLE);

        TextView ClickToStart = findViewById(R.id.startbutton);
        ClickToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToGame = new Intent(menu.this, game.class);
                startActivity(intentToGame);
            }
        });
        Button logOut = findViewById(R.id.logOut);
        logOut.setVisibility(View.INVISIBLE);
        ImageView flecha = findViewById(R.id.closePopup);
        ConstraintLayout Fondo = findViewById(R.id.Fondo);
        ImageView ImgFondo = findViewById(R.id.ImgFondo);
        ImageView profileimg = findViewById(R.id.ProfileImg);
        profileimg.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                logOut.setVisibility(View.VISIBLE);
                flecha.setVisibility(View.VISIBLE);
                Fondo.setBackground(getDrawable(R.color.background_ligth_green));
                Ajustes.setVisibility(View.VISIBLE);
                Cuenta.setVisibility(View.VISIBLE);
                ClickToStart.setEnabled(false);
                profileimg.setAlpha(0.3f);
                ImgFondo.setAlpha(0.3f);
                ClickToStart.setAlpha(0.1f);
                profileimg.setEnabled(false);
            }
        });

        flecha.setVisibility(View.INVISIBLE);
        flecha.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                logOut.setVisibility(View.INVISIBLE);
                flecha.setVisibility(View.INVISIBLE);
                Fondo.setBackground(getDrawable(R.color.background_green));
                Ajustes.setVisibility(View.INVISIBLE);
                Cuenta.setVisibility(View.INVISIBLE);
                ClickToStart.setEnabled(true);
                profileimg.setAlpha(1f);
                ImgFondo.setAlpha(1f);
                ClickToStart.setAlpha(1f);
                profileimg.setEnabled(true);
            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMain = new Intent(menu.this, MainActivity.class);
                startActivity(toMain);
            }
        });
    }
}