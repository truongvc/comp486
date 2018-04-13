package com.example.platformgame;

import android.content.Context;

public class Fire extends GameObject{

    Fire(Context context, float worldStartX, float worldStartY, char type,
         int pixelsPerMetre){

        final int ANIMATION_FPS = 3;
        final int ANIMATION_FRRAME_COUNT = 3;
        final String BITMAP_NAME = "fire";

        final float HEIGHT = 1;
        final float WIDTH = 1;

        setHeight(HEIGHT); // 1 meter tall
        setWidth(WIDTH); //1 meter wide

        setType(type);
        //now for the players other attributes
        //our game engine will use these
        setMoves(false);
        setActive(true);
        setVisible(true);

        //Choose a bitmap
        setBitmapName(BITMAP_NAME);
        //set this object up to be animated
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRRAME_COUNT);
        setBitmapName(BITMAP_NAME);
        setAnimated(context,pixelsPerMetre,true);

        //where does the tile start
        //x and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    public void update(long fps, float gravity){

    }

}
