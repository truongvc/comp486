package com.example.platformgame;

import com.example.platformgame.GameObject;

/**
 * Created by victor.truong on 2/28/2018.
 */

public class Grass extends GameObject {

    Grass(float worldStartX, float worldStartY, char type){
        final float HEIGHT = 1;
        final float WIDTH = 1;

        setHeight(HEIGHT);//1 meter tall
        setWidth(WIDTH);//1m wide

        setType(type);

        //Choose a bitmap
        setBitmapName("turf");

        //where does the tile start
        //x and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }//end constructor

    public void update(long fps, float gravity){}

}
