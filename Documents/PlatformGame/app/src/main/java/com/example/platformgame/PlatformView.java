package com.example.platformgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class PlatformView extends SurfaceView implements Runnable {

    private boolean debugging = true;
    private volatile boolean running;
    private Thread gameThread = null;

    // For drawing
    private Paint paint;
    // Canvas could initially be local.
    // But later we will use it outside of the draw() method
    private Canvas canvas;
    private SurfaceHolder ourHolder;


    Context context;

    // Our new engine classes
    private LevelManager lm;
    private Viewport vp;
    InputController ic;
    SoundManager sm;
    private PlayerState ps;

    long startFrameTime;
    long timeThisFrame;
    long fps;

    PlatformView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        // Initialize the viewport
        vp = new Viewport(screenWidth, screenHeight);

        //Initialize SoundManager
        sm = new SoundManager();
        sm.loadSound(context);

        ps = new PlayerState();

        loadLevel("LevelCave", 15, 2);
    }

    public void loadLevel(String level, float px, float py) {

        lm = null;

        // Create a new LevelManager
        // Passing in a Context, screen details, level name and player location
        lm = new LevelManager(context, vp.getPixelsPerMetreX(), vp.getScreenWidth(), ic, level, px, py);
        ic = new InputController(vp.getScreenWidth(), vp.getScreenHeight());

        PointF location = new PointF(px,py);
        ps.saveLocation(location);

        //set the players location as the world centre of the viewport
        vp.setWorldCentre(lm.gameObjects.get(lm.playerIndex)
                        .getWorldLocation().x,
                lm.gameObjects.get(lm.playerIndex)
                        .getWorldLocation().y);
    }

    @Override
    public void run() {

        while (running) {
            startFrameTime = System.currentTimeMillis();

            update();

            draw();

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update() {

        for (GameObject go : lm.gameObjects) {
            if (go.isActive()) {
                // Clip anything off-screen
                if (!vp.clipObjects(go.getWorldLocation().x, go.getWorldLocation().y, go.getWidth(), go.getHeight())) {

                    // Set visible flag to true
                    go.setVisible(true);

                    //check collision with player
                    int hit = lm.player.checkCollisions(go.getHitbox());
                    if(hit > 0){
                        //collision! now deal with diff types
                        switch (go.getType()){
                            case 'c':
                                sm.playSound("coin_pickup.xml");
                                go.setActive(false);
                                go.setActive(false);
                                ps.getCredits();

                                //now restore state that was
                                //removed by collision detection
                                if (hit != 2) {
                                    //any hit except feet
                                    lm.player.restorePreviousVelocity();
                                }
                                break;

                            case 'u':
                                sm.playSound("gun_upgrade.ogg");
                                go.setActive(false);
                                go.setVisible(false);
                                lm.player.bfg.upgradeRateOfFire();
                                ps.increaseFireRate();
                                //now restore state that was
                                //removed by collision detection
                                if (hit != 2) {
                                    //any hit except feet
                                    lm.player.restorePreviousVelocity();
                                }
                                break;

                            case 'e':
                                //extra life

                                go.setActive(false);
                                go.setVisible(false);
                                sm.playSound("extra_life.ogg");

                                //now restore state that was
                                //removed by collision detection
                                if (hit != 2) {
                                    //any hit except feet
                                    lm.player.restorePreviousVelocity();
                                }
                                break;

                            case 'd':
                                PointF location;
                                //hit by drone
                                sm.playSound("player_burn.ogg");
                                ps.loseLife();
                                location = new PointF(ps.loadLocation().x, ps.loadLocation().y);
                                lm.player.setWorldLocationX(location.x);
                                lm.player.setWorldLocationY(location.y);
                                lm.player.setxVelocity(0);
                                break;

                            case 'g':
                                //hit by guard
                                sm.playSound("player_burn");
                                ps.loseLife();
                                location = new PointF(ps.loadLocation().x,
                                        ps.loadLocation().y);

                                lm.player.setWorldLocationX(location.x);
                                lm.player.setWorldLocationY(location.y);
                                lm.player.setxVelocity(0);
                                break;


                            default://probably a reg tile
                                if(hit == 1){//left or right
                                    lm.player.setxVelocity(0);
                                    lm.player.setPressingRight(false);
                                }

                                if(hit == 2){//feet
                                    lm.player.isFalling = false;
                                }
                                break;

                        }
                    }

                    //check bullet collisions
                    for (int i = 0; i < lm.player.bfg.getNumBullets(); i++){
                        //make a hitbox out of the current bullet
                        RectHitbox r = new RectHitbox();
                        r.setLeft(lm.player.bfg.getBulletX(i));
                        r.setTop(lm.player.bfg.getBulletY(i));
                        r.setRight(lm.player.bfg.getBulletX(i) + .1f);
                        r.setBottom(lm.player.bfg.getBulletY(i) + .1f);

                        if(go.getHitbox().intersects(r)){
                            //collision detected
                            //make bullet disappear until it
                            //is respawned as a new bullet
                            lm.player.bfg.hideBullet(i);

                            //Now respond depending upon the type of object hit
                            if(go.getType() != 'g' && go.getType() != 'd'){
                                sm.playSound("richochet");
                            }else if(go.getType() == 'g'){
                                //knock the guard back
                                go.setWorldLocationX(go.getWorldLocation().x +
                                2 * (lm.player.bfg.getDirection(i)));

                                sm.playSound("hit_guard");
                            }else if (go.getType() == 'd'){
                                //destroy the droid
                                sm.playSound("explode");
                                //permanently clip this drone
                                go.setWorldLocation(-100,-100,0);
                            }
                        }
                    }

                    if(lm.isPlaying()){
                        //run any unclipped updates
                        go.update(fps, lm.gravity);

                        if(go.getType() == 'd'){
                            //let any near by drones know where the player is
                            Drone d = (Drone) go;
                            d.setWaypoint(lm.player.getWorldLocation());
                        }
                    }

                } else {
                    // Set visible flag to false
                    go.setVisible(false);
                    // Now draw() can ignore them
                }
            }

        }

        if(lm.isPlaying()){
            //reset the players location as the center of the viewport
            vp.setWorldCentre(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x,
                    lm.gameObjects.get(lm.playerIndex).getWorldLocation().y);
        }

    }//end update

    private void draw() {

        if (ourHolder.getSurface().isValid()) {
            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();

            // Rub out the last frame with arbitrary color
            paint.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawColor(Color.argb(255, 0, 0, 255));

            // Draw all the GameObjects
            Rect toScreen2d = new Rect();

            // Draw a layer at a time
            for (int layer = -1; layer <= 1; layer++)

                for (GameObject go : lm.gameObjects) {
                    if (go.isVisible() && go.getWorldLocation().z == layer) { //Only draw if visible and this layer

                        toScreen2d.set(vp.worldToScreen
                                (go.getWorldLocation().x,
                                        go.getWorldLocation().y,
                                        go.getWidth(),
                                        go.getHeight()));
                        if(go.isAnimated()){
                            //get the next frame of the bitmap
                            //rotate if necessary
                            if(go.getFacing() == 1){
                                //rotate
                                Matrix flipper = new Matrix();
                                flipper.preScale(-1,1);
                                Rect r = go.getRectToDraw(System.currentTimeMillis());
                                Bitmap b =Bitmap.createBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())],
                                r.left,
                                r.top,
                                r.width(),
                                r.height(),
                                flipper,
                          true);
                                canvas.drawBitmap(b, toScreen2d.left, toScreen2d.top, paint);
                            } else {
                                //draw it the regular way round
                                canvas.drawBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())],
                                        go.getRectToDraw(System.currentTimeMillis()), toScreen2d,
                                        paint);
                            }
                        } else {
                            // Draw the appropriate bitmap
                            canvas.drawBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())],
                                    toScreen2d.left,
                                    toScreen2d.top, paint);
                        }
                    }
                }

                // draw the bullets
            paint.setColor(Color.argb(255,255,255,255));
            for(int i = 0; i < lm.player.bfg.getNumBullets(); i++){
                //pass in the x and y coords as usual
                //then .25 and .05 for the bullet width and height
                toScreen2d.set(vp.worldToScreen(lm.player.bfg.getBulletX(i),
                        lm.player.bfg.getBulletY(i),.25f,.05f));
                canvas.drawRect(toScreen2d, paint);
            }
            // Text for debugging
            if (debugging) {
                paint.setTextSize(16);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                canvas.drawText("fps:" + fps, 10, 60, paint);
                canvas.drawText("num objects:" + lm.gameObjects.size(), 10, 80, paint);
                canvas.drawText("num clipped:" + vp.getNumClipped(), 10, 100, paint);
                canvas.drawText("playerX:" + lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, 10, 120, paint);
                canvas.drawText("playerY:" + lm.gameObjects.get(lm.playerIndex).getWorldLocation().y, 10, 140, paint);

                canvas.drawText("Gravity: " + lm.gravity, 10, 160, paint);;
                canvas.drawText("X velocity: " + lm.gameObjects.get(lm.playerIndex).getxVelocity(),
                        10, 200, paint);
                canvas.drawText("Y velocity: " + lm.gameObjects.get(lm.playerIndex).getyVelocity(),
                        10, 200, paint);


                //for reset the number of clipped objects each frame
                vp.resetNumClipped();

            }

            //draw buttons
            paint.setColor(Color.argb(80,255,255,255));
            ArrayList<Rect> buttonsToDraw;
            buttonsToDraw = ic.getButtons();

            for(Rect rect : buttonsToDraw){
                RectF rf = new RectF(rect.left, rect.top, rect.right, rect.bottom);
                canvas.drawRoundRect(rf,15f,15f,paint);
            }

            //draw paused text
            if(!this.lm.isPlaying()){
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.argb(255,255,255,255));

                paint.setTextSize(120);
                canvas.drawText("Paused", vp.getScreenWidth() / 2,
                        vp.getScreenHeight() / 2, paint);
            }
            // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
//        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
//            case MotionEvent.ACTION_DOWN:
//                lm.switchPlayingStatus();
//                break;
//        }
        if(lm != null){
            ic.handleInput(motionEvent, lm, sm, vp);
        }
        //invalidate();
        return true;
    }

    // Clean up our thread if the game is interrupted or the player quits
    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("error", "failed to pause thread");
        }
    }

    // Make a new thread and start it
    // Execution moves to our run method
    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}// End of PlatformView