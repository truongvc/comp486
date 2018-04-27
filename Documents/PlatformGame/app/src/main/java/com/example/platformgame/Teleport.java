package com.example.platformgame;

public class Teleport extends GameObject {

    Location target;

    Teleport(float worldStartX, float worldStartY, char type, Location target){
        final float HEIGHT  = 2; // 2 meters tall
        final float WIDTH = 2; //1 meter wide???
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("door");

        this.target = new Location(target.level, target.x, target.y);

        //where does the tile start?
        //x and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);

        setRectHitbox();

    }

    public Location getTarget() {
        return target;
    }

    public void update(long fps, float gravity){}

}
