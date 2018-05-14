package com.example.riftdefender;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RiftDefenderView extends SurfaceView implements Runnable{

    private Context context;

    private int screenX;
    private int screenY;

    volatile boolean playing;
    Thread gameThread = null;

    //game objects

    //for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    //for saving and loading high score
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    RiftDefenderView(Context context, int screenX, int screenY){
        super(context);

        this.screenX = screenX;
        this.screenY = screenY;

        //itialize drawing objects
        ourHolder = getHolder();
        paint = new Paint();

    }

    // Clean up our thread if the game is interrupted or the player quits
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    // Make a new thread and start it
    // Execution moves to our R
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run(){
        while (playing) {
            update();
            draw();
            control();
        }
    }
}//end class
