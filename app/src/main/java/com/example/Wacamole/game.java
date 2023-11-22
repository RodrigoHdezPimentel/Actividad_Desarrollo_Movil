package com.example.Wacamole;

/*Cosas que hacer:
Primero: al final del while en el run del cronometro(Al final del codigo) hacer un intent a una actividad
    que te muestre tu puntuacion y tenga un boton para ver la clasificacion de puntos global, te permita
    reiniciar el juego y volver al menu principal (menu).

Segundo: Cambiar el boton de arriba en medio por un TextView que empiece poniendo empezar y un boton
    para pausar la partida. Cuando se pulse, aparecerá de nuevo el TextView pero pondrá pausa.
    Cuando el TextView se esté mostrando, de deberá enborronar el fondo como en menú cuando le damos
    al perfil.

Tercero: hacer la consulta para el podio
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class game extends AppCompatActivity implements Runnable{
    public static final int MAX_TIEMPO = 15;
    private Thread hilo;
    public static boolean isOn = false;
    private TextView segundos;
    private TextView start;
    private int numSegundos = 0;
    ImageView[] topos;
    Animation appear;
    Animation disappear;
    int puntuacion = 0;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent recibrCuenta = getIntent();
        username = recibrCuenta.getStringExtra("Nombre");

        ImageView game = findViewById(R.id.background);
        game.setAlpha(0.3f);


        //Animaciones de aparecer y desaparecer
        disappear = AnimationUtils.loadAnimation(game.this, R.anim.disappeard);
        appear = AnimationUtils.loadAnimation(game.this, R.anim.show);
        //Array de topos
        topos = new ImageView[]{findViewById(R.id.Topo1), findViewById(R.id.Topo2), findViewById(R.id.Topo3)
                , findViewById(R.id.Topo4), findViewById(R.id.Topo5), findViewById(R.id.Topo6)
                , findViewById(R.id.Topo7), findViewById(R.id.Topo8), findViewById(R.id.Topo9)};

        //Contador de puntos
        TextView contador = findViewById(R.id.contador);
        contador.setText("0 0 0 0");

        //Escondemos los topos
        for (ImageView topo: topos) {
            topo.setVisibility(View.INVISIBLE);
            topo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    puntuacion++;
                    if(puntuacion < 10){
                        contador.setText("0 0 0 " + puntuacion);
                    }else if(puntuacion < 100){
                        contador.setText("0 0 " + String.valueOf(puntuacion).charAt(0) + " " + String.valueOf(puntuacion).charAt(1));
                    }else if(puntuacion < 1000){
                        contador.setText("0 " + String.valueOf(puntuacion).charAt(0) + " " + String.valueOf(puntuacion).charAt(1));
                    }
                    topo.setEnabled(false);
                    topo.startAnimation(disappear);
                }
            });
        }
        //Cronometro
        segundos = findViewById(R.id.textViewSegundos);

        //Boton para empezar
        start = findViewById(R.id.buttonPush);
        start.setText("click to start");
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setVisibility(View.INVISIBLE);
                game.setAlpha(1f);
                //Restablecemos variables
                contador.setText("0 0 0 0");
                puntuacion = 0;

                Log.d("Topos", "****");

                if(!isOn) {
                    numSegundos = MAX_TIEMPO;
                    segundos.setText("0" + numSegundos);

                    isOn = true;
                    //Log.d("tiempo", "estado = "+ isOn);
                    start.setText("Stop");
                    iniciarCronometro();

                }else {
                    isOn = false;
                    //Log.d("tiempo", "estado = " + isOn);
                    start.setText("Play");
                    detenerCronometro();
                }
            }
        });
    }
    private void iniciarCronometro() {
        hilo = new Thread(this);
        hilo.start();

        for (ImageView topo: topos) {
            //Lanzamos los topos
            Topo newTopo = new Topo(appear, disappear, topo);
            newTopo.start();
        }
    }

    private void detenerCronometro() {
        hilo = null;
    }

    //Run de los hilos
    @Override
    public void run() {

        try {
            while (isOn) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (numSegundos < 10) {
                            segundos.setText("0" + numSegundos);

                        }else{
                            segundos.setText(String.valueOf(numSegundos));
                        }
                        if(numSegundos == 0){
                            start.setText("Play");
                            isOn = false;
                        }
                        numSegundos--;
                    }
                });
                Thread.sleep(1000);
            }
            Intent toResultado = new Intent(this, resultado.class);
            toResultado.putExtra("Nombre", username);
            toResultado.putExtra("puntuacion", Integer.toString(puntuacion));
            startActivity(toResultado);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}