package com.example.riftdefender;

import android.content.Context;

public class Player extends GameObject {

    final float MAX_VELOCITY = 10;
    RectHitbox rectHitboxFeet;
    RectHitbox rectHitboxHead;
    RectHitbox rectHitboxLeft;
    RectHitbox rectHitboxRight;


//moving
    boolean isPressingRight = false;
    boolean isPressingLeft = false;
    boolean isPressingUp = false;
    boolean isPressingDown = false;

    Player(Context context, float worldStartX, float worldStartY, int pixelsPerMetre) {
        final float HEIGHT = 4;
        final float WIDTH = 2;
        setHeight(HEIGHT); // 4 metre tall
        setWidth(WIDTH); // 2 metre wide
        setType('p');
        // Standing still to start with
        setxVelocity(0);
        setyVelocity(0);
        setFacing(LEFT);

// Now for the player's other attributes
// Our game engine will use these
        setMoves(true);
        setActive(true);
        setVisible(true);
//...

        // Choose a Bitmap
        // This is a sprite sheet with multiple frames
        // of animation. So it will look silly until we animate it
        // In chapter 6.
        setBitmapName("player");

        final int ANIMATION_FPS = 16;
        final int ANIMATION_FRAME_COUNT = 5;
// Set this object up to be animated
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setAnimated(context, pixelsPerMetre, true);

        // X and y locations from constructor parameters
        // TODO: 5/15/2018  camera follows start point must fix should start point be random?
       // setWorldLocation(worldStartX, worldStartY, 0);
        setWorldLocation(5,5, 0);
        rectHitboxFeet = new RectHitbox();
        rectHitboxHead = new RectHitbox();
        rectHitboxLeft = new RectHitbox();
        rectHitboxRight = new RectHitbox();
    }

    public void update(long fps) {
        if (isPressingRight) {
            this.setxVelocity(MAX_VELOCITY);
        } else if (isPressingLeft) {
            this.setxVelocity(-MAX_VELOCITY);
        } else if (isPressingUp) {
            this.setyVelocity(MAX_VELOCITY);
        } else if (isPressingDown) {
            this.setyVelocity(-MAX_VELOCITY);
        } else {
            this.setxVelocity(0);
            this.setyVelocity(0);
        }
        //which way is player facing?
        if (this.getxVelocity() > 0) {
//facing right
            setFacing(RIGHT);
        } else if (this.getxVelocity() < 0) {
            //facing left
            setFacing(LEFT);
        }else if (this.getyVelocity() > 0) {
            //facing up
            setFacing(UP);
        }else if (this.getyVelocity() < 0) {
            //facing down
            setFacing(DOWN);
        }//if 0 then unchanged
        this.move(fps);

// Update all the hitboxes to the new location
// Get the current world location of the player
// and save them as local variables we will use next
        Vector2Point5D location = getWorldLocation();
        float lx = location.x;
        float ly = location.y;
//update the player feet hitbox
        rectHitboxFeet.top = ly + getHeight();
        rectHitboxFeet.left = lx + getWidth();
        rectHitboxFeet.bottom = ly + getHeight();
        rectHitboxFeet.right = lx + getWidth();
// Update player head hitbox
        rectHitboxHead.top = ly;
        rectHitboxHead.left = lx + getWidth();
        rectHitboxHead.bottom = ly + getHeight();
        rectHitboxHead.right = lx + getWidth();
// Update player left hitbox
        rectHitboxLeft.top = ly + getHeight();
        rectHitboxLeft.left = lx + getWidth();
        rectHitboxLeft.bottom = ly + getHeight();
        rectHitboxLeft.right = lx + getWidth();
// Update player right hitbox
        rectHitboxRight.top = ly + getHeight();
        rectHitboxRight.left = lx + getWidth();
        rectHitboxRight.bottom = ly + getHeight();
        rectHitboxRight.right = lx + getWidth();

    }//end update

    public int checkCollisions(RectHitbox rectHitbox) {
        int collided = 0;//no collision

        //the left
        if (this.rectHitboxLeft.intersects(rectHitbox)) {
            //left has collided
            //move player just to right of current hitbox
            this.setWorldLocationX(rectHitbox.right - getWidth() * .2f);
            //setxVelocity(0);
            //setPressingLeft(false);
            collided = 1;
        }

        //the right
        if (this.rectHitboxRight.intersects(rectHitbox)) {
            //right has collided
            //move player just to left of current hitbox
            this.setWorldLocationX(rectHitbox.left - getWidth() * .8f);
            //setxVelocity(0);
            //setPressingRight(false);
            collided = 1;
        }

        //the feet
        if (this.rectHitboxFeet.intersects(rectHitbox)) {
            //feet have collided
            //move feet to just above current hitbox
            this.setWorldLocationY(rectHitbox.top - getHeight());
            collided = 2;
            //isFalling = false;
        }

        //Now the head
        if (this.rectHitboxHead.intersects(rectHitbox)) {
            //head has collided
            //move head to just below current hitbox bottom
            this.setWorldLocationY(rectHitbox.bottom);
            collided = 3;
        }

        return collided;

    }

    public void setPressingRight(boolean isPressingRight) {
        this.isPressingRight = isPressingRight;
    }

    public void setPressingLeft(boolean isPressingLeft) {
        this.isPressingLeft = isPressingLeft;
    }

    public void setPressingUp(boolean isPressingUp) {
        this.isPressingUp = isPressingUp;
    }

    public void setPressingDown(boolean isPressingDown) {
        this.isPressingDown = isPressingDown;
    }
}
