package com.example.c9asteroids;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

public class SoundManager {
    private SoundPool soundPool;
    private int shoot = -1;
    private int thrust = -1;
    private int explode = -1;
    private int shipexplode = -1;
    private int ricochet = -1;
    private int blip = -1;
    private int nextlevel = -1;
    private int gameover = -1;
    public void loadSound(Context context){
        soundPool = new SoundPool(10,
                AudioManager.STREAM_MUSIC,0);
        /*
        try{
//Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;
//create our fx
            descriptor = assetManager.openFd("shoot.ogg");
            shoot = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("thrust.ogg");
            thrust = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("explode.ogg");
            explode = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("shipexplode.ogg");
            shipexplode = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("ricochet.ogg");
            ricochet = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("blip.ogg");
            blip = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("nextlevel.ogg");
            nextlevel = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("gameover.ogg");
            gameover = soundPool.load(descriptor, 0);
        }catch(IOException e){
//Print an error message to the console
            Log.e("error", "failed to load sound files");
        }
        */
        shoot = soundPool.load(context,R.raw.shoot,0);
        thrust = soundPool.load(context,R.raw.thrust,0);
        explode = soundPool.load(context,R.raw.explode,0);
        shipexplode = soundPool.load(context,R.raw.shipexplode,0);
        ricochet = soundPool.load(context,R.raw.ricochet,0);
        blip = soundPool.load(context,R.raw.blip,0);
        nextlevel = soundPool.load(context,R.raw.nextlevel,0);
        gameover = soundPool.load(context,R.raw.gameover,0);

    }
    public void playSound(String sound){
        switch (sound){
            case "shoot":
                soundPool.play(shoot, 1, 1, 0, 0, 1);
                break;
            case "thrust":
                soundPool.play(thrust, 1, 1, 0, 0, 1);
                break;
            case "explode":
                soundPool.play(explode, 1, 1, 0, 0, 1);
                break;
            case "shipexplode":
                soundPool.play(shipexplode, 1, 1, 0, 0, 1);
                break;
            case "ricochet":
                soundPool.play(ricochet, 1, 1, 0, 0, 1);
                break;
            case "blip":
                soundPool.play(blip, 1, 1, 0, 0, 1);
                break;
            case "nextlevel":
                soundPool.play(nextlevel, 1, 1, 0, 0, 1);
                break;
            case "gameover":
                soundPool.play(gameover, 1, 1, 0, 0, 1);
                break;
        }
    }
}