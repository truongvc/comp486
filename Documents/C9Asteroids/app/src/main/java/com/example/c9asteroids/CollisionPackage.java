package com.example.c9asteroids;
// All objects which can collide have a collision package.
// Asteroids, ship, bullets. The structure seems like slight
// overkill for bullets but it keeps the code generic,
// and the use of vertexListLength means there isn't any
// actual speed overhead. Also if we wanted line, triangle or
// even spinning bullets the code wouldn't need to change.

import android.content.pm.ComponentInfo;
import android.graphics.PointF;

public class CollisionPackage {
    //all the members are public to avoid multiple calls to the getter

    //the facing angle allows us to calculate the current world coordinates of each vertex
    //using the model-space coordinates in the vertexList

    public float facingAngle;

    //the model-space coordinates
    public PointF[] vertexList;

    /*
    the number of vertices in vertexList is kept in the next int because it is pre-calculated
    and we can use it in out loops instead of continually calling vertexList.length
     */
    public int vertexListLength;

    //where is the center of the object?
    public PointF worldLocation;

    /*
    this next float will be used to detect if the circle shaped hitboxes collide. it represents
    the furthest point from the center of any given object.
    each object will set this slightly differently.
    the ship will use height/2 an asteroid will use 25 to allow for a max length rotated coordinate.
     */
    public float radius;

    //a couple of points to store results and avoid creating new objects during intensive
    //collision detection.
    public PointF currentPoint = new PointF();
    public PointF currentPoint2 = new PointF();

    //constructor
    public CollisionPackage(PointF[] vertexList, PointF worldLocation,
                            float radius, float facingAngle){
        vertexListLength = vertexList.length;
        this.vertexList = new PointF[vertexListLength];
        //make a copy of the array

        for(int i = 0; i < vertexListLength; i++){
            this.vertexList[i] = new PointF();
            this.vertexList[i].x = vertexList[i].x;
            this.vertexList[i].y = vertexList[i].y;
        }

        this.worldLocation = new PointF();
        this.worldLocation = worldLocation;

        this.radius = radius;

        this.facingAngle = facingAngle;
    }


}//end class



























