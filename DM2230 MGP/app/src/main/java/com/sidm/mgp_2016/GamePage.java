package com.sidm.mgp_2016;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class GamePage extends Activity
{
    //Sound
    MediaPlayer SoundPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // Hide App Title Bar and Menu Top Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide the Menu Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //setContentView(R.layout.activity_game_page);
        setContentView(new GamePanelSurfaceView(this));

        //SoundPlayer = MediaPlayer.create(getApplicationContext(),R)
    }

    protected void onPause()
    {
        super.onPause();
    }

    protected void onStop()
    {
        super.onStop();
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }
}