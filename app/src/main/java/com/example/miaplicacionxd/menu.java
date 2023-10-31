package com.example.miaplicacionxd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView ClickToStart = findViewById(R.id.startbutton);
        ClickToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGame();
            }
        });

        RelativeLayout Fondo = findViewById(R.id.Fondo);
        ImageView profileimg = findViewById(R.id.ProfileImg);
        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickToStart.setEnabled(false);
                profileimg.setEnabled(false);
                Fondo.setBackground(getDrawable(R.color.background_light_green));
            }
        });
    }
    public void goToGame(){
        Intent intentToGame = new Intent(menu.this, game.class);
        startActivity(intentToGame);
    }
}