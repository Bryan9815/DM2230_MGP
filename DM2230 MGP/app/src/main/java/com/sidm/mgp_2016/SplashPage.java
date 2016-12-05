package com.sidm.mgp_2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class SplashPage extends Activity
{

    boolean Active = true;
    int SplashTime = 1;//5000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Hide App Title Bar and Menu Top Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide the Menu Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_page);

        //thread for displaying the Splash Screen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try
                {
                    int waited = 0;
                    while(Active && (waited < SplashTime))
                    {
                        sleep(200);
                        if(Active)
                        {
                            waited += 200;
                        }
                    }
                } catch(InterruptedException e)
                {
                    //do nothing
                } finally
                {
                    finish();
                    //Create new activity based on and intent with CurrentActivity
                    Intent intent = new Intent(SplashPage.this, MainMenu.class);
                    startActivity(intent);
                }
            }
        };
        splashTread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            Active = false;
        }
        return true;
    }
}
