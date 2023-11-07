package com.example.Wacamole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
public class Ajustes extends AppCompatActivity {
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        Intent usernamerecibido = getIntent();
        username = usernamerecibido.getStringExtra("Nombre");

        ImageView ImgFlecha = findViewById(R.id.backMenu);
        ImgFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            //DESPUES DE DARLE CLICK A LA FLECHA LO MANDARIA A LA CLASE MENU
            public void onClick(View view) {
                Intent nuevoIntent = new Intent(Ajustes.this, menu.class);
                nuevoIntent.putExtra("Nombre", username);
                startActivity(nuevoIntent);
            }
        });

    }
}