package com.example.platformgame;

import android.content.Context;

/**
 * Created by victor.truong on 2/28/2018.
 */

public class Player extends GameObject {
    RectHitbox rectHitboxFeet;
    RectHitbox rectHitboxHead;
    RectHitbox rectHitboxLeft;
    RectHitbox rectHitboxRight;

    final float MAX_X_VELOCITY = 10;

    public boolean isFalling;
    private boolean isJumping;
    private long jumpTime;
    private long maxJumpTime = 700;//jump 7 10ths of a second

    boolean isPressingRight = false;
    boolean isPressingLeft = false;

    public MachineGun bfg;

    Player(Context context, float worldStartX,
           float worldStartY, int pixelsPerMeter) {

        final float HEIGHT = 2;
        final float WIDTH = 1;

        setHeight(HEIGHT);//2 meter tall
        setWidth(WIDTH);//1 meter wide

        //standing still to start with
        setxVelocity(0);
        setyVelocity(0);
        setFacing(LEFT);
        isFalling = false;

        //now for the player's other attributes
        //our game engine will use these
        setMoves(true);
        setActive(true);
        setVisible(true);

        setType('p');

        //choose a bitmap
        //this is a sprite sheet with multiple frames
        //of animation. so it will look silly unitl we anitmate it

        setBitmapName("player");

        final int ANIMATION_FPS = 16;
        final int ANIMATION_FRAME_COUNT = 5;

        //set this object up to be animated
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setAnimated(context, pixelsPerMeter, true);

        //x and y locations from constructor parameters

        setWorldLocation(worldStartX, worldStartY, 0);

        rectHitboxFeet = new RectHitbox();
        rectHitboxHead = new RectHitbox();
        rectHitboxLeft = new RectHitbox();
        rectHitboxRight = new RectHitbox();

        bfg = new MachineGun();
    }//end constructor

    public void update(long fps, float gravity) {
        if (isPressingRight) {
            this.setxVelocity((MAX_X_VELOCITY));
        } else if (isPressingLeft) {
            this.setxVelocity(-MAX_X_VELOCITY);
        } else {
            this.setxVelocity(0);
        }

        //which way is player facing
        if (this.getxVelocity() > 0) {
            //facing right
            setFacing(RIGHT);
        } else if (this.getxVelocity() < 0) {
            //facing left
            setFacing(LEFT);
        }//if 0 then unchanged

        //jumping and gravity
        if (isJumping) {
            long timeJumping = System.currentTimeMillis() - jumpTime;
            if (timeJumping < maxJumpTime) {
                if (timeJumping < maxJumpTime / 2) {
                    this.setyVelocity(-gravity);//on the way up
                } else if (timeJumping > maxJumpTime / 2) {
                    this.setyVelocity(gravity);//going down
                }
            } else {
                isJumping = false;
            }
        } else {
            this.setyVelocity(gravity);
            //READ ME!
            //remove the next line to make the game easier
            //it means the long jumps are less punishing
            //because the player can take off just after the platform
            //they will also be able to cheat by jumping in thin air

            // isFalling = true;
        }

        bfg.update(fps, gravity);

        //let's go!
        this.move(fps);
        //Update all the hitboxes to the new location
        // get the current world location of the player
        // and save them as local variables we will use next
        Vector2Point5D location = getWorldLocation();
        float lx = location.x;
        float ly = location.y;

        //update the player feet hitbox
        rectHitboxFeet.top = ly + getHeight() * .95f;
        rectHitboxFeet.left = lx + getWidth() * .2f;
        rectHitboxFeet.bottom = ly + getHeight() * .98f;
        rectHitboxFeet.right = lx + getWidth() * .8f;

        //update player head hitbox
        rectHitboxHead.top = ly;
        rectHitboxHead.left = lx + getWidth() * .4f;
        rectHitboxHead.bottom = ly + getHeight() * .2f;
        rectHitboxHead.right = lx + getWidth() * .6f;

        // Update player left hitbox
        rectHitboxLeft.top = ly + getHeight() * .2f;
        rectHitboxLeft.left = lx + getWidth() * .2f;
        rectHitboxLeft.bottom = ly + getHeight() * .8f;
        rectHitboxLeft.right = lx + getWidth() * .3f;

        // Update player right hitbox
        rectHitboxRight.top = ly + getHeight() * .2f;
        rectHitboxRight.left = lx + getWidth() * .8f;
        rectHitboxRight.bottom = ly + getHeight() * .8f;
        rectHitboxRight.right = lx + getWidth() * .7f;

    }//end update

    public void restorePreviousVelocity(){
        if(!isJumping && !isFalling){
            if(getFacing() == LEFT){
                isPressingLeft = true;
                setxVelocity(-MAX_X_VELOCITY);
            } else {
                isPressingRight = true;
                setxVelocity(MAX_X_VELOCITY);
            }
        }
    }

    public boolean pullTringger(){
        //try and fire a shot
        return bfg.shoot(this.getWorldLocation().x,
                this.getWorldLocation().y, getFacing(), getHeight());
    }

    public int checkCollisions(RectHitbox rectHitbox) {
        int collided = 0;//no collisions

        //left
        if (this.rectHitboxLeft.intersects(rectHitbox)) {
            //left collision
            //move player just to right of current hitbox
            this.setWorldLocationX(rectHitbox.right - getWidth() * .2f);
            //setxVelocity(0);
            //setPressingLeft(false);
            collided = 1;
        }

        //right
        if (this.rectHitboxRight.intersects(rectHitbox)) {
            //right collision
            //move player to the left of current hitbox
            this.setWorldLocationX(rectHitbox.left - getWidth() * .8f);
            //setxVelocity(0);
            //setPressingRight(false);
            collided = 1;
        }

        //feet
        if (this.rectHitboxFeet.intersects(rectHitbox)) {
            //feet collision
            //move feet to just above current hitbox
            this.setWorldLocationY(rectHitbox.top - getHeight());
            collided = 2;
            //isFalling = false;
        }

        //head
        if (this.rectHitboxHead.intersects(rectHitbox)) {
            //head collision
            //move head to just below current hitbox bottom
            this.setWorldLocationY(rectHitbox.bottom);
            collided = 3;
        }
        return collided;
    }//end checkCollision

    public void setPressingRight(boolean isPressingRight) {
        this.isPressingRight = isPressingRight;
    }

    public void setPressingLeft(boolean isPressingLeft) {
        this.isPressingLeft = isPressingLeft;
    }

    public void startJump(SoundManager sm) {
        if (!isFalling) {//can't jump if falling
            if(!isJumping) {
                isJumping = true;
                jumpTime = System.currentTimeMillis();
                sm.playSound("jump");
            }
        }
    }
}
