package com.example.platformgame;

/**
 * Created by victor.truong on 2/28/2018.
 */
import  java.util.ArrayList;
public class LevelCave extends LevelData {
    LevelCave(){
        tiles = new ArrayList<String>();
        this.tiles.add("p.............................................");
        this.tiles.add("..............................................");
        this.tiles.add("......d.......................................");
        this.tiles.add("..............................................");
        this.tiles.add("....................c.........................");
        this.tiles.add("....................1.....d..u................");
        this.tiles.add(".................c..........u1................");
        this.tiles.add(".................1.........u1.................");
        this.tiles.add("..............c...........u1.............d....");
        this.tiles.add("..............1..........u1...................");
        this.tiles.add("......................e..1....e.....e.........");
        this.tiles.add("....11111111111111111111111111111111111111....");
    }
}
