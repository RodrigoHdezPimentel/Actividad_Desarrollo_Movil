package com.example.Wacamole;

import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class Topo extends Thread {
    Animation appear;
    Animation disappeard;
    private ImageView topo;

    public Topo (Animation ap, Animation disap, ImageView img){
        this.appear = ap;
        this.disappeard = disap;
        this.topo = img;
    }

    public void run() {
        topo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topo.startAnimation(disappeard);
            }
        });
        while (game.isOn) {
            try {
                Random rm = new Random();
                sleep(200 + rm.nextInt(2000));
                topo.startAnimation(appear);
                sleep(200);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
