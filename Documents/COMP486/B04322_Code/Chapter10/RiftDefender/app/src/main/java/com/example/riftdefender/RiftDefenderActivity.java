package com.example.riftdefender;

import android.app.Activity;
import android.graphics.Point;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class RiftDefenderActivity extends Activity {

    //object to handle the view
    private RiftDefenderView riftDefenderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //get a display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        //load the resolution in to a Point object
        Point resolution = new Point();
        display.getSize(resolution);

        //set the view for the game
        riftDefenderView = new RiftDefenderView(this, resolution.x, resolution.y);

        //make riftDefenderView the view for the activity
        setContentView(riftDefenderView);

    }
    // If the Activity is paused make sure to pause our thread
    @Override
    protected void onPause() {
        super.onPause();
        riftDefenderView.pause();
    }

    // If the Activity is resumed make sure to resume our thread
    @Override
    protected void onResume() {
        super.onResume();
        riftDefenderView.resume();
    }

}
