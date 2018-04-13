package com.example.platformgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import java.util.ArrayList;

public class LevelManager {

    private String level;
    int mapWidth;
    int mapHeight;

    Player player;
    int playerIndex;

    private boolean playing;
    float gravity;

    LevelData levelData;
    ArrayList<GameObject> gameObjects;

    ArrayList<Rect> currentButtons;
    Bitmap[] bitmapsArray;

    public LevelManager(Context context, int pixelsPerMetre, int screenWidth,
                        InputController ic, String level, float px, float py) {
        this.level = level;

        switch (level) {
            case "LevelCave":
                levelData = new LevelCave();
                break;

            // We can add extra levels here

        }

        // To hold all our GameObjects
        gameObjects = new ArrayList<>();

        // To hold 1 of every Bitmap
        bitmapsArray = new Bitmap[25];

        // Load all the GameObjects and Bitmaps
        loadMapData(context, pixelsPerMetre, px, py);

        //set waypoints for our guards
        setWaypoints();

        // Ready to play overwritten by switch playing status
        //playing = true;
    }


    public boolean isPlaying() {
        return playing;
    }

    public void switchPlayingStatus(){
        playing = !playing;
        if(playing){
            gravity = 6;
        } else {
            gravity = 0;
        }
    }
    // Each index Corresponds to a bitmap
    public Bitmap getBitmap(char blockType) {

        int index;
        switch (blockType) {
            case '.':
                index = 0;
                break;

            case '1':
                index = 1;
                break;

            case 'p':
                index = 2;
                break;

            case 'c':
                index = 3;
                break;

            case 'u':
                index = 4;
                break;

            case 'e':
                index = 5;
                break;

            case 'd':
                index = 6;
                break;

            case 'g':
                index = 7;
                break;

            case 'f':
                index = 8;
                break;

            case '2':
                index = 9;
                break;
            case '3':
                index = 10;
                break;
            case '4':
                index = 11;
                break;
            case '5':
                index = 12;
                break;
            case '6':
                index = 13;
                break;
            case '7':
                index = 14;
                break;

            default:
                index = 0;
                break;
        }

        return bitmapsArray[index];
    }

    // This method allows each GameObject which 'knows'
    // its type to get the correct index to its Bitmap
    // in the Bitmap array.
    public int getBitmapIndex(char blockType) {

        int index;
        switch (blockType) {
            case '.':
                index = 0;
                break;

            case '1':
                index = 1;
                break;

            case 'p':
                index = 2;
                break;

            case 'c':
                index = 3;
                break;

            case 'u':
                index = 4;
                break;

            case 'e':
                index = 5;
                break;

            case 'd':
                index = 6;
                break;

            case 'g':
                index = 7;
                break;

            case 'f':
                index = 8;
                break;

            case '2':
                index = 9;
                break;
            case '3':
                index = 10;
                break;
            case '4':
                index = 11;
                break;
            case '5':
                index = 12;
                break;
            case '6':
                index = 13;
                break;
            case '7':
                index = 14;
                break;
            default:
                index = 0;
                break;
        }

        return index;
    }

    // For now we just load all the grass tiles
    // and the player. Soon we will have many GameObjects
    void loadMapData(Context context, int pixelsPerMetre, float px, float py) {

        char c;

        //Keep track of where we load our game objects
        int currentIndex = -1;

        // how wide and high is the map? Viewport needs to know
        mapHeight = levelData.tiles.size();
        mapWidth = levelData.tiles.get(0).length();

        for (int i = 0; i < levelData.tiles.size(); i++) {
            for (int j = 0; j < levelData.tiles.get(i).length(); j++) {

                c = levelData.tiles.get(i).charAt(j);
                if (c != '.') {// Don't want to load the empty spaces
                    currentIndex++;
                    switch (c) {

                        case '1':
                            // Add a tile to the gameObjects
                            gameObjects.add(new Grass(j, i, c));
                            break;

                        case 'p':// a player
                            // Add a player to the gameObjects
                            gameObjects.add(new Player
                                    (context, px, py, pixelsPerMetre));

                            // We want the index of the player
                            playerIndex = currentIndex;
                            // We want a reference to the player object
                            player = (Player) gameObjects.get(playerIndex);

                            break;

                        case 'c':
                            //add a coin to the gameObjects
                            gameObjects.add(new Coin(j,i,c));
                            break;

                        case 'u':
                            //add a machine gun upgrade to the game objects
                            gameObjects.add(new MachineGunUpgrade(j,i,c));
                            break;

                        case 'e':
                            //add an extra life to the gameobjects
                            gameObjects.add(new ExtraLife(j,i,c));
                            break;

                        case 'd':
                            //add a drone to the game objects
                            gameObjects.add(new Drone(j,i,c));
                            break;

                        case 'g':
                            //add a guard to the gameobjects
                            gameObjects.add(new Guard(context,j,i,c,pixelsPerMetre));
                            break;

                        case 'f':
                            //add a fire tile to gameobjects
                            gameObjects.add(new Fire(context,j,i,c,pixelsPerMetre));
                            break;
                        case '2':
// Add a tile to the gameObjects
                            gameObjects.add(new Snow(j, i, c));
                            break;
                        case '3':
// Add a tile to the gameObjects
                            gameObjects.add(new Brick(j, i, c));
                            break;
                        case '4':
// Add a tile to the gameObjects
                            gameObjects.add(new Coal(j, i, c));
                            break;
                        case '5':
// Add a tile to the gameObjects
                            gameObjects.add(new Concrete(j, i, c));
                            break;
                        case '6':
// Add a tile to the gameObjects
                            gameObjects.add(new Scorched(j, i, c));
                            break;
                        case '7':
// Add a tile to the gameObjects
                            gameObjects.add(new Stone(j, i, c));
                            break;
                    }

                    // If the bitmap isn't prepared yet
                    if (bitmapsArray[getBitmapIndex(c)] == null) {
                        // Prepare it now and put it in the bitmapsArrayList
                        bitmapsArray[getBitmapIndex(c)] =
                                gameObjects.get(currentIndex).
                                        prepareBitmap(context,
                                                gameObjects.get(currentIndex).
                                                        getBitmapName(),
                                                pixelsPerMetre);

                    }
                }
            }
        }
    }

    public void setWaypoints() {
        //loop through all game objects looking for guards
        for (GameObject guard : this.gameObjects) {
            if (guard.getType() == 'g') {
                //set waypoints for this guard
                //find the tile beneath the guard
                //this relies on the designer putting
                //the guard in sensible location

                int startTileIndex = -1;
                int startGuardIndes = 0;
                float waypointX1 = -1;
                float waypointX2 = -1;

                for (GameObject tile : this.gameObjects) {
                    startTileIndex++;
                    if (tile.getWorldLocation().y == guard.getWorldLocation().y
                            + 2) {
                        //tile is two spaces below current guard
                        //now see if has same x coordinate
                        if (tile.getWorldLocation().x == guard.getWorldLocation().x) {
                            //found the tile the guard is standing on
                            //now go left as far as possible
                            //before non traversable tile is found
                            //either on guards row or tile row
                            //upto a maximum of 5 tiles.
                            // 5 is an arbitrary value you can change to suit you

                            for (int i = 0; i < 5; i++) {//left for loop
                                if (!gameObjects.get(startTileIndex - i).isTraversable()) {
                                    //set the left waypoint
                                    waypointX1 = gameObjects.get(startTileIndex -
                                            (i + 1)).getWorldLocation().x;
                                    break;//leave left for loop
                                } else {
                                    //set to max 5 tiles as
                                    // no non traversible found
                                    waypointX1 = gameObjects.get(startTileIndex -
                                            5).getWorldLocation().x;
                                }
                            }//end left

                            for (int i = 0; i < 5; i++) {//right for loop
                                if (!gameObjects.get(startTileIndex +
                                        i).isTraversable()) {

                                    //set the right waypoint
                                    waypointX2 = gameObjects.get((startTileIndex +
                                            (i - 1))).getWorldLocation().x;
                                    break;//leave right for loop
                                } else {
                                    //set to max 5 tiles away
                                    waypointX2 = gameObjects.get(startTileIndex +
                                            5).getWorldLocation().x;
                                }
                            }//end right waypoint

                            Guard g = (Guard) guard;
                            g.setWaypoints(waypointX1, waypointX2);
                        }//end if
                    }
                }
            }
        }
    }//end set waypoint

}//end class
