package com.example.Wacamole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Cuenta extends AppCompatActivity {
String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);
        Intent usernamerecibido = getIntent();
        username = usernamerecibido.getStringExtra("Nombre");

        Button but = findViewById(R.id.goto_show_Cuentas);
        but.setText(username);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goShow = new Intent(Cuenta.this, showCuenta.class);
                goShow.putExtra("Nombre", username);
                startActivity(goShow);
            }
        });
    }
}