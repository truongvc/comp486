package com.example.riftdefender;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
        loadLevel("LevelOne",15,2);

    }

    public void loadLevel(String level, float px, float py) {

        levelManager = null;

        // Create a new LevelManager
        // Passing in a Context, screen details, level name and player location
        levelManager = new LevelManager(context, viewport.getPixelsPerMetreX(),
                viewport.getScreenWidth(), inputController, level, px, py);
        inputController = new InputController(viewport.getScreenWidth(), viewport.getScreenHeight());

        //set the players location as the world centre of the viewport
        viewport.setWorldCentre(levelManager.gameObjects.get(levelManager.playerIndex)
                        .getWorldLocation().x,
                levelManager.gameObjects.get(levelManager.playerIndex)
                        .getWorldLocation().y);
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
            for (int layer = -1; layer <= 1; layer++)

                for (GameObject go : levelManager.gameObjects) {
                    if (go.isVisible() && go.getWorldLocation().z == layer) { //Only draw if visible and this layer
                            toScreen2d.set(viewport.worldToScreen
                                    (go.getWorldLocation().x,
                                            go.getWorldLocation().y,
                                            go.getWidth(),
                                            go.getHeight()));


                            // Draw the appropriate bitmap
                            canvas.drawBitmap(levelManager.bitmapsArray[levelManager.getBitmapIndex(go.getType())],
                                    toScreen2d.left,
                                    toScreen2d.top, paint);
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

                        //for reset the number of clipped objects each frame
                        //viewport.resetNumClipped();

                    }

                    // Unlock and draw the scene
                    ourHolder.unlockCanvasAndPost(canvas);
                }

    }//end draw





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
