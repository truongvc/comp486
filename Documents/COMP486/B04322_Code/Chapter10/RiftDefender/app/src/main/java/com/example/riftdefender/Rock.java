package com.example.riftdefender;

public class Rock extends GameObject {
    Rock (float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 1;
        final float WIDTH = 1;

        setHeight(HEIGHT); // 1 metre tall
        setWidth(WIDTH); // 1 metre wide

        setType(type);


        // Choose a Bitmap
        setBitmapName("rock");

        // Where does the tile start
        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);

        //collision detection
        setRectHitbox();
    }

    public void update(long fps) {
    }
}
