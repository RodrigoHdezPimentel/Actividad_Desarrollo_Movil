package com.example.Wacamole;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class game extends AppCompatActivity implements Runnable{
    public static final int MAX_TIEMPO = 5;
    private Thread hilo;
    private boolean isOn = false;
    private TextView segundos;
    private Button boton;
    private int numSegundos = 0;
    ImageView[] topos;
    Animation appear;
    Animation disappear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        disappear = AnimationUtils.loadAnimation(game.this, R.anim.disappeard);
        appear = AnimationUtils.loadAnimation(game.this, R.anim.show);

        topos = new ImageView[]{findViewById(R.id.Topo1), findViewById(R.id.Topo2), findViewById(R.id.Topo3)
                , findViewById(R.id.Topo4), findViewById(R.id.Topo5), findViewById(R.id.Topo6)
                , findViewById(R.id.Topo7), findViewById(R.id.Topo8), findViewById(R.id.Topo9)};

        TextView contador = findViewById(R.id.contador);
        contador.setText("0 0 0 0");
        final int[] puntuacion = {0};

        for (ImageView topo: topos) {
            topo.setVisibility(View.INVISIBLE);
        }

        segundos = findViewById(R.id.textViewSegundos);

        boton = findViewById(R.id.buttonPush);
        boton.setText("Play");
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador.setText("0 0 0 0");
                puntuacion[0] = 0;

                for (ImageView topo: topos) {
                    topo.startAnimation(appear);
                    topo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            puntuacion[0]++;
                            if(puntuacion[0] < 10){
                                contador.setText("0 0 0 " + puntuacion[0]);
                            }else if(puntuacion[0] < 100){
                                contador.setText("0 0 " + String.valueOf(puntuacion[0]).charAt(0) + " " + String.valueOf(puntuacion[0]).charAt(1));

                            }

                        }
                    });
                    topo.setVisibility(View.VISIBLE);
                }

                if(!isOn) {
                    numSegundos = MAX_TIEMPO;
                    segundos.setText("0" + numSegundos);

                    isOn = true;
                    Log.d("tiempo", "estado = "+ isOn);
                    boton.setText("Stop");
                    iniciarCronometro();

                }else {
                    isOn = false;
                    Log.d("tiempo", "estado = " + isOn);
                    boton.setText("Play");
                    detenerCronometro();
                }
            }
        });

    }

    private void iniciarCronometro() {

        hilo = new Thread(this);
        hilo.start();

    }

    private void detenerCronometro() {

        hilo = null;

    }

    //Run de los hilos
    @Override
    public void run() {

        try {

            while (isOn) {

                Thread.sleep(1000);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        numSegundos--;
                        if (numSegundos < 10) {
                            segundos.setText("0" + numSegundos);

                        }else{
                            segundos.setText(String.valueOf(numSegundos));
                        }
                        if(numSegundos == 1){
                            boton.setText("Play");
                            isOn = false;
                        }
                    }
                });

            }
            for (ImageView topo: topos) {
                topo.startAnimation(disappear);
                topo.setVisibility(View.INVISIBLE);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}