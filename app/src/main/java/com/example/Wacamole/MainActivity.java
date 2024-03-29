package com.example.Wacamole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    EditText nameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Variables de la activity
        ImageView martillo = findViewById(R.id.martillo);
        ImageView topoPortada = findViewById(R.id.Portada);
        ImageView kaboom = findViewById(R.id.kaboom);

        TextView error = findViewById(R.id.error);
        nameText = findViewById(R.id.nameText);
        Button Login = findViewById(R.id.Login);
        Button register = findViewById(R.id.Register);

        //animacion

        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);
        topoPortada.startAnimation(slideAnimation);
        martillo.startAnimation(slideAnimation);

        Animation hide = AnimationUtils.loadAnimation(MainActivity.this, R.anim.disappeard);
        Animation show = AnimationUtils.loadAnimation(this, R.anim.show);
        show.setDuration(300);
        hide.setDuration(400);
        kaboom.setVisibility(View.INVISIBLE);
        topoPortada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation shakeAnimation =  AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                martillo.startAnimation(shakeAnimation);

                kaboom.startAnimation(show);
                kaboom.startAnimation(hide);

            }

        });

        // Aplicar la AnimationSet a la variable martillo

        //Te lleva a crear user
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(MainActivity.this, Insertar.class);
                startActivity(newIntent);
            }
        });
        //Comprueba que el user esta registrado
        firestoreDB.collection("Usuarios")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            error.setVisibility(View.INVISIBLE);
                            Login.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onClick(View v) {
                                        //Si el registro esta en la BD, entra al menu
                                        if (registroEncontrado(task, nameText)) {
                                            error.setVisibility(View.INVISIBLE);
                                            //Va al menu una vez haces log in
                                            Intent intentToMenu = new Intent(MainActivity.this, menu.class);
                                            intentToMenu.putExtra("Nombre", nameText.getText().toString());
                                            startActivity(intentToMenu);
                                        //Si no encuentra el usuario, salta error
                                        } else {
                                            error.setVisibility(View.VISIBLE);
                                            error.setText("USUARIO NO ENCONTRADO");
                                        }
                                }
                            });
                        }
                    }
                });
    }
    //Metodo para encontrar registro duplicado
    public boolean registroEncontrado(Task<QuerySnapshot> task, TextView tv) {
        boolean encontrado = false;
        //Recorre el registro de datos buscando coincidencias
        for (QueryDocumentSnapshot document : task.getResult()) {
            if(Objects.equals(document.get("Username"), tv.getText().toString())){
                encontrado = true;
                break;
            }
        }
        return encontrado;
    }
}