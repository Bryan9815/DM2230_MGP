package com.sidm.mgp_2016;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Space;

/**
 * Created by Alienware Pc on 021, 21-Nov-2016.
 */
public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering

    // 1a) Variables used for background rendering
    private Bitmap Background;
    private Bitmap ScaledBackground;
    // 1b) Define Screen width and Screen height as integer
    int ScreenWidth, ScreenHeight;
    // 1c) Variables for defining background start and end point
    private short BackgroundX = 0, BackgroundY = 0;
    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap[] SpaceShip = new Bitmap[4];
    // 4b) Variable as an index to keep track of the spaceship images
    private short SpaceShipIndex = 0;

    // Spaceship Location
    int ShipX = 0, ShipY = 0;

    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;

    // Variable for Game State check
    private short GameState;

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView (Context context){

        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics Metrics  = context.getResources().getDisplayMetrics();
        ScreenHeight = Metrics.heightPixels;
        ScreenWidth = Metrics.widthPixels;

        // 1e)load the image when this class is being instantiated
        Background = BitmapFactory.decodeResource(getResources(), R.drawable.gamescene);
        ScaledBackground = Bitmap.createScaledBitmap(Background, ScreenWidth, ScreenHeight, true);
        // 4c) Load the images of the spaceships
        SpaceShip[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_1);
        SpaceShip[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_2);
        SpaceShip[2] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_3);
        SpaceShip[3] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_4);

        SpaceShip[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ship2_1), ScreenWidth /10, ScreenWidth/10, true);


        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder){
        // Create the thread
        if (!myThread.isAlive()){
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        // Destroy the thread
        if (myThread.isAlive()){
            myThread.startRun(false);


        }
        boolean retry = true;
        while (retry) {
            try {
                myThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    public void RenderGameplay(Canvas Canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if (Canvas == null)
        {
            return;
        }
        Canvas.drawBitmap(ScaledBackground, BackgroundX, BackgroundY, null);
        Canvas.drawBitmap(ScaledBackground, BackgroundX + ScreenWidth, BackgroundY, null);
        // 4d) Draw the spaceships
        Canvas.drawBitmap(SpaceShip[SpaceShipIndex], BackgroundX, BackgroundY, null);


        // Bonus) To print FPS on the screen

    }


    //Update method to update the game play
    public void update(float dt, float fps){
        FPS = fps;

        switch (GameState) {
            case 0: {
                // 3) Update the background to allow panning effect
                BackgroundX -= 500 * dt; // Change pan speed
                if (BackgroundX < -ScreenWidth)
                {
                    BackgroundX = 0;
                }


                // 4e) Update the spaceship images / shipIndex so that the animation will occur.
                SpaceShipIndex++;
                if (SpaceShipIndex > 3)
                {
                    SpaceShipIndex = 0;
                }
            }
            break;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas){
        switch (GameState)
        {
            case 0:
                RenderGameplay(canvas);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        // 5) In event of touch on screen, the spaceship will relocate to the point of touch
        short x = (short)event.getX();
        short y = (short)event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            ShipX = (short) (x - SpaceShip[SpaceShipIndex].getWidth()*0.5f);
            ShipY = (short) (y - SpaceShip[SpaceShipIndex].getHeight()*0.5f);
        }
        return super.onTouchEvent(event);
    }
}
