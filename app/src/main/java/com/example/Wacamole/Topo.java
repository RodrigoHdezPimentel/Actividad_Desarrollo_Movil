package com.example.Wacamole;

import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class Topo extends Thread{
    Animation appear;
    Animation disappeard;
    private int id;
    private ImageView topo;
    private int contador;

    public void run(){
        topo.startAnimation(appear);
        topo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador++;
                topo.startAnimation(disappeard);
                topo.setTranslationY(200);
            }
        });
    }
}
