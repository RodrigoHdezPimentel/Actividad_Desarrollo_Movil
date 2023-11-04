package com.example.Wacamole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
        Intent usernamerebido = getIntent();
        username = usernamerebido.getStringExtra("Nombre");
        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();

        Button Ajustes = findViewById(R.id.SettingsBut);
        Ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToGame = new Intent(menu.this, Ajustes.class);
                startActivity(intentToGame);
            }
        });
        Ajustes.setVisibility(View.INVISIBLE);
        Button Cuenta = findViewById(R.id.AcountBut);
        Cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToGame = new Intent(menu.this, Cuenta.class);
                intentToGame.putExtra("Nombre", username);
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

        ConstraintLayout Fondo = findViewById(R.id.Fondo);
        ImageView ImgFondo = findViewById(R.id.ImgFondo);
        ImageView profileimg = findViewById(R.id.ProfileImg);
        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fondo.setBackground(getDrawable(R.color.background_light_green));
                Ajustes.setVisibility(View.VISIBLE);
                Cuenta.setVisibility(View.VISIBLE);
                ClickToStart.setEnabled(false);
                profileimg.setAlpha(0.3f);
                ImgFondo.setAlpha(0.3f);
                ClickToStart.setAlpha(0.3f);
                profileimg.setEnabled(false);
            }
        });
        Fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }
}