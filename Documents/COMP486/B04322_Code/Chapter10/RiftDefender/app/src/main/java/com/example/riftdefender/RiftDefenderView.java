package com.example.riftdefender;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class RiftDefenderView extends SurfaceView implements Runnable{

    private boolean debugging = true;
    volatile boolean playing; //playing is running in platform
    Thread gameThread = null;

    //for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    private Context context;

    //game engine classes
    private LevelManager levelManager;
    private Viewport viewport;
    InputController inputController;
    SoundManager soundManager;

    //timing
    long startFrameTime;
    long timeThisFrame;
    long fps;

    //game objects

    //for saving and loading high score
//    private SharedPreferences prefs;
  //  private SharedPreferences.Editor editor;

    RiftDefenderView(Context context,int screenWidth, int screenHeight){
        super(context);
        this.context = context;

        //intialize drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        //initialize viewport
        viewport = new Viewport(screenWidth, screenHeight);

        //load level
        loadLevel("LevelOne",15,5);

    }

    public void loadLevel(String level, float px, float py) {

        levelManager = null;

        // Create a new LevelManager
        // Passing in a Context, screen details, level name and player location
        levelManager = new LevelManager(context, viewport.getPixelsPerMetreX(),
                viewport.getScreenWidth(), inputController, level, px, py);
        inputController = new InputController(viewport.getScreenWidth(), viewport.getScreenHeight());

        //set the players location as the world centre of the viewport
//        viewport.setWorldCentre(levelManager.gameObjects.get(levelManager.playerIndex)
//                        .getWorldLocation().x,
//                levelManager.gameObjects.get(levelManager.playerIndex)
//                        .getWorldLocation().y);
        viewport.setWorldCentre(8,5);

    }

    public void run(){
        while (playing) {
            startFrameTime = System.currentTimeMillis();
            //update game state
            update();

            //render game state to screen
            draw();

            //calc fps for animation and movement
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    public void update(){
//        for (GameObject go : levelManager.gameObjects) {
//            if (go.isActive()) {
//                // Clip anything off-screen
//                if (!viewport.clipObjects(go.getWorldLocation().x, go.getWorldLocation().y,
//                        go.getWidth(), go.getHeight())) {
//
//                    // Set visible flag to true
//                    go.setVisible(true);
//
//                } else {
//                    // Set visible flag to false
//                    go.setVisible(false);
//                    // Now draw() can ignore them
//                }
//            }
//
//        }
        for (GameObject go : levelManager.gameObjects) {
            // check collisions with player
            int hit = levelManager.player.checkCollisions(go.getHitbox());
            if (hit > 0) {
            //collision! Now deal with different types
                switch (go.getType()) {
                    default:// Probably a regular tile
                        if (hit == 1) {// Left or right
                            levelManager.player.setxVelocity(0);
                            //levelManager.player.setPressingRight(false);
                        }
                        if (hit == 2) {// Feet
                            //levelManager.player.isFalling = false;
                        }
                        break;
                }
            }

            if (levelManager.isPlaying()) {
                // Run any un-clipped updates
                go.update(fps);
            }
        }
    }

    public void draw() {
        if (ourHolder.getSurface().isValid()) {
            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();
            // Rub out the last frame with arbitrary color
            paint.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawColor(Color.argb(255, 0, 0, 255));

            // Draw all the GameObjects
            Rect toScreen2d = new Rect();

            // Draw a layer at a time
            for (int layer = -1; layer <= 1; layer++) {

                for (GameObject go : levelManager.gameObjects) {
                    if (go.isVisible() && go.getWorldLocation().z == layer) { //Only draw if visible and this layer
                        toScreen2d.set(viewport.worldToScreen
                                (go.getWorldLocation().x,
                                        go.getWorldLocation().y,
                                        go.getWidth(),
                                        go.getHeight()));


                        //collision hitbox debugging
                        paint.setColor(Color.argb(255, 0, 0, 0));
                        //canvas.drawRect(go.getHitbox().left, go.getHitbox().top, go.getHitbox().right, go.getHitbox().bottom, paint);
                        canvas.drawRect(toScreen2d.left, toScreen2d.top, toScreen2d.right, toScreen2d.bottom, paint);

                        paint.setTextSize(16);

                        paint.setColor(Color.argb(255, 255, 255, 255));
                        canvas.drawText("x:" + go.getWorldLocation().x, toScreen2d.left,
                                +toScreen2d.top, paint);
                        canvas.drawText("y:" + go.getWorldLocation().y, toScreen2d.left + 50,
                                +toScreen2d.top, paint);

                        if (go.isAnimated()) {
// Get the next frame of the bitmap
// Rotate if necessary
                            if (go.getFacing() == 1) {
// Rotate
                                Matrix flipper = new Matrix();
                                flipper.preScale(-1, 1);
                                Rect r = go.getRectToDraw(System.currentTimeMillis());
                                Bitmap b = Bitmap.createBitmap(
                                        levelManager.bitmapsArray[levelManager.getBitmapIndex(go.getType())],
                                        r.left,
                                        r.top,
                                        r.width(),
                                        r.height(),
                                        flipper,
                                        true);
                                canvas.drawBitmap(b, toScreen2d.left, toScreen2d.top, paint);
                            } else {
// draw it the regular way round
                                canvas.drawBitmap(
                                        levelManager.bitmapsArray[levelManager.getBitmapIndex(go.getType())],
                                        go.getRectToDraw(System.currentTimeMillis()),
                                        toScreen2d, paint);
                            }
                        } else { // Just draw the whole bitmap
                            canvas.drawBitmap(
                                    levelManager.bitmapsArray[levelManager.getBitmapIndex(go.getType())],
                                    toScreen2d.left,
                                    toScreen2d.top, paint);
                        }
                    }
                }
            }
                    // Text for debugging
                    if (debugging) {
                        paint.setTextSize(16);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setColor(Color.argb(255, 255, 255, 255));
                        canvas.drawText("fps:" + fps, 10, 60, paint);
                        canvas.drawText("num objects:" + levelManager.gameObjects.size(), 10, 80, paint);
                        //canvas.drawText("num clipped:" + viewport.getNumClipped(), 10, 100, paint);
                        canvas.drawText("playerX:" + levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().x, 10, 120, paint);
                        canvas.drawText("playerY:" + levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().y, 10, 140, paint);
                        canvas.drawText("X velocity:" +
                                        levelManager.gameObjects.get(levelManager.playerIndex).getxVelocity(),
                                10, 180, paint);
                        canvas.drawText("Y velocity:" +
                                        levelManager.gameObjects.get(levelManager.playerIndex).getyVelocity(),
                                10, 200, paint);
                        //for reset the number of clipped objects each frame
                        //viewport.resetNumClipped();

                    }
                    //draw buttons
                    paint.setColor(Color.argb(80, 255, 255, 255));
                    ArrayList<Rect> buttonsToDraw;
                    buttonsToDraw = inputController.getButtons();

                    for (Rect rect : buttonsToDraw) {
                        RectF rf = new RectF(rect.left, rect.top, rect.right, rect.bottom);
                        canvas.drawRoundRect(rf, 15f, 15f, paint);
                    }
                    // Unlock and draw the scene
                    ourHolder.unlockCanvasAndPost(canvas);

        }
    }//end draw

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (levelManager != null) {
            inputController.handleInput(motionEvent, levelManager, soundManager, viewport);
        }
        //invalidate();
        return true;
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

}//end class
