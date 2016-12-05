package com.sidm.mgp_2016;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Paint;
import android.os.Vibrator;


import java.util.Random;
import java.util.Vector;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // Implement this interface to receive information about changes to the surface.

    protected static final String TAG = null;

    private GameThread myThread = null; // Thread to control the rendering

    // 1) Variables used for background rendering
    private Bitmap bg, scaledbg;

    //Stone Animation
    //private SpriteAnimation stone_anim;

    int ScreenWidth, ScreenHeight;

    public float FPS;
    long dt;

    private short GameState;

    // Paint object
    Paint paint = new Paint();

    // Font
    Typeface Font;

    private short bgX = 0, bgY = 0;

    private short mX = 100, mY = 0;

    private int aX = 300, aY = 300;

    // 5) bitmap array to stores 4 images of the spaceship
    private Bitmap[] ship = new Bitmap[4];

    // 6) Variable as an index to keep track of the spaceship images
    private short shipIndex = 0;

    //Week 7 to move move ship
    private boolean moveShip = false;

    //Button variables
    private boolean Button_active;
    private Bitmap Button_bitmap, Button_Background;
    private Vector<Bubble> ListOfBubbles;
    //Score
    private int Score;

    //Touch position
    private short touch_x,touch_y;

    Vibrator v;

    private Bitmap create_BitMap(int img, int scale_x, int scale_y)
    {
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), img),scale_x,scale_y,true);
    }
    private void Standard_Init(Context context)
    {
        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // Set things to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        // 2)load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.gamescene);
        scaledbg = Bitmap.createScaledBitmap(bg, (int) (ScreenWidth), (int) (ScreenHeight), true);


        // 7) Load the images of the spaceships
        ship[0] = Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.ship2_1)), (int) (ScreenWidth) / 10, (int) (ScreenHeight) / 10, true);
        ship[1] = Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.ship2_2)), (int) (ScreenWidth) / 10, (int) (ScreenHeight) / 10, true);
        ship[2] = Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.ship2_3)), (int) (ScreenWidth) / 10, (int) (ScreenHeight) / 10, true);
        ship[3] = Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.ship2_4)), (int) (ScreenWidth) / 10, (int) (ScreenHeight) / 10, true);

        // Font
        Font.createFromAsset(getContext().getAssets(), "fonts/ITCBLKAD.TTF");

        // Week 7 Load images for Flying Stone animation
        //stone_anim = new SpriteAnimation(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.flystar)), (int) (ScreenWidth)/4, (int) (ScreenHeight)/10, true), 320, 64, 5, 5);
        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);
        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

        Score = 0;

        v = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }
    private void Button_Init()
    {
        Button_active = true;
        Button_bitmap = create_BitMap(R.drawable.blue_button,ScreenWidth/5,ScreenWidth/5);
        Button_Background = create_BitMap(R.drawable.button_background,ScreenWidth,ScreenHeight);

        //Create Bubbles
        ListOfBubbles = new Vector<Bubble>();
        Bubble NewBubble = new Bubble();
        ListOfBubbles.add(NewBubble);

        for (int i = 0; i < ListOfBubbles.size(); i++)
        {
            ListOfBubbles.get(i).Init();
        }
    }
    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView(Context context)
    {
        // Context is the current state of the application/object
        super(context);
        Standard_Init(context);
        Button_Init();
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder)
    {
        // Create the thread
        if (!myThread.isAlive())
        {
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
    }
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;

        // Destroy the thread
        if (myThread.isAlive())
        {
            myThread.startRun(false);
        }

        // Stop the thread
        while (retry)
        {
            try
            {
                myThread.join();
                retry = false;
            } catch (InterruptedException e)
            {

            }
        }
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    //public void update(){
    public void update(float dt, float fps)
    {
        FPS = fps;

        switch (GameState)
        {
            case 0:
            {
                // 4) An update function to update the game
                bgX -= 500 * dt; //Change the number of panning speed
                if (bgX < -ScreenWidth)
                {
                    bgX = 0;
                }
                // 9) Update the spaceship images / shipIndex so that the animation will occur.
                shipIndex++;
                shipIndex %= 4;

                // Draw stone animation
                //stone_anim.update(System.currentTimeMillis());

                break;
            }
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas)
    {

        switch (GameState)
        {
            case 0:
                RenderGameplay(canvas);

                break;

        }
    }
    public void RenderScore(Canvas canvas)
    {
        RenderTextOnScreen(canvas,"Score: " + Integer.toString(Score),130, 105, 30);
    }
    public void RenderBubbles(Canvas canvas)
    {
        canvas.drawBitmap(Button_Background,0,0,null);
        if (Button_active == true)
        {
            canvas.drawBitmap(Button_bitmap,ScreenWidth/2,ScreenHeight/2,null);
            canvas.drawBitmap(Button_bitmap,(ScreenWidth/2) - (ScreenWidth/5),ScreenHeight/2,null);
        }
    }
    public void RenderGameplay(Canvas canvas)
    {
        // 3) Re-draw 2nd image after the 1st image ends
        if (canvas == null)
        {
            return;
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        /*canvas.drawBitmap(scaledbg, bgX, bgY, null);
        canvas.drawBitmap(scaledbg, bgX + ScreenWidth, bgY, null);

        // 8) Draw the spaceships
        canvas.drawBitmap(ship[shipIndex], mX, mY, null);

        // Week 7 Render FPS


        // Draw sprite
        stone_anim.draw(canvas);

        //Week 7
        //Edit after adding collision onTouch event & comment code above  -- stone_anim.setY(600);
        stone_anim.setX(aX);
        stone_anim.setY(aY);*/

        RenderBubbles(canvas);
        RenderScore(canvas);
        //FPS
        RenderTextOnScreen(canvas,"FPS: " + FPS, 130, 75, 30);
        //Touch position
        RenderTextOnScreen(canvas,"X: " + Short.toString(touch_x) + "Y:" + Short.toString(touch_y),130, 145, 30);
    }
    // Week 7 Print text on screen
    public void RenderTextOnScreen (Canvas canvas, String text, int posX, int posY, int textsize)
    {
        if (canvas != null && text.length() != 0)
        {
            Paint paint = new Paint();

            paint.setARGB(255, 1, 1, 1);
            paint.setStrokeWidth(100);
            paint.setTextSize(textsize);
            paint.setTypeface(Font);

            canvas.drawText(text, posX, posY, paint);
        }
    }
    // Week 7 AABB
    public boolean CheckCollision (int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2)
    {
        //top left
        if (x2 >= x1 && x2 <= x1 + w1)
        {
            if (y2 >= y1 && y2 <= y1 + h1)
                return true;
        }
        //top right
        if (x2 + w2 >= x1 && x2 + w2 <= x1 + w1)
        {
            if (y2 >= y1 && y2 <= y1 + h1)
                return true;
        }
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();// Check for the action of touch

        short X = touch_x = (short) event.getX();
        short Y = touch_y = (short) event.getY();



        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
                if (CheckCollision(ScreenWidth/2,ScreenHeight/2, ScreenWidth/5,ScreenWidth/5, X, Y, 0, 0) && Button_active)
                {
                    Button_active = false;
                    Score += 2;

                    // Vibrate for 500 milliseconds
                    //v.vibrate(500);

                }
                if (CheckCollision((ScreenWidth/2) - (ScreenWidth/5),ScreenHeight/2, ScreenWidth/5,ScreenWidth/5, X, Y, 0, 0)&& Button_active)
                {
                    Button_active = false;
                    Score += 2;
                    //v.vibrate(500);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (moveShip == true)
                {
                    // New location where the image to land on
                    mX = (short) (X - ship[shipIndex].getWidth() / 2);
                    mY = (short) (Y - ship[shipIndex].getHeight() / 2);
                }
                //Check if ship and stone collide
                /*if (CheckCollision(mX, mY, ship[shipIndex].getWidth(), ship[shipIndex].getHeight(), aX, aY, stone_anim.getSpriteWidth(), stone_anim.getSpriteHeight()))
                {
                    Random random = new Random();

                    aX = random.nextInt(ScreenWidth-50);
                    aY = random.nextInt(ScreenHeight-50);
                }*/
                break;

            case MotionEvent.ACTION_UP:

                break;
        }return true;
    }
    private class Bubble
    {
        short Position_x ,Position_y, Scale;
        boolean Active;
        //Bubble[] Linked_Bubbles;

        public void Init()
        {
            Position_x = (short)(ScreenWidth/2);
            Position_y = (short)(ScreenHeight/2);
            Active = true;
            Scale = (short)(ScreenWidth/5);
        }
    }
}