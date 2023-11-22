package com.example.Wacamole;

import android.util.Log;
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

        long last_time = System.nanoTime();
        while (game.isOn) {

            try {
                Random rm = new Random();
                Thread.sleep(1100 + rm.nextInt(7000));
                if(game.isOn) {
                    topo.startAnimation(appear);
                    topo.setEnabled(true);
                }
                Thread.sleep(900);

                long time = System.nanoTime();
                int delta_time = (int) ((time - last_time) / 1000000);
                last_time = time;
                Log.d("Topo" + getId(), "Delta_Time " + last_time);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
