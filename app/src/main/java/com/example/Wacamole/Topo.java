package com.example.Wacamole;

import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class Topo extends Thread {
    Animation appear;
    Animation disappeard;
    private int Id;
    private ImageView topo;

    public Topo (Animation ap, Animation disap,  int id, ImageView img){
        this.appear = ap;
        this.disappeard = disap;
        this.Id = id;
        this.topo = img;
    }

    public void run() {
        topo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topo.startAnimation(disappeard);
                topo.setTranslationY(200);
            }
        });
        while (game.isOn) {
            try {
                Random rm = new Random();
                sleep(200 + rm.nextInt(4000));
                topo.startAnimation(appear);
                sleep(300);
                topo.setVisibility(View.VISIBLE);
                sleep(800 + rm.nextInt(1000));
                topo.startAnimation(disappeard);
                sleep(300);
                topo.setVisibility(View.INVISIBLE);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
