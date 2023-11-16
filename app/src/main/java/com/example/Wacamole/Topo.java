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

        while (game.isOn) {
            try {
                Random rm = new Random();
                Thread.sleep(700 + rm.nextInt(8000));
                if(game.isOn) {
                    topo.startAnimation(appear);
                    topo.setEnabled(true);
                }
                Thread.sleep(100);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
