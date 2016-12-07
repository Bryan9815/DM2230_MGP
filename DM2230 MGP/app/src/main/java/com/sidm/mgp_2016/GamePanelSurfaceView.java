package com.sidm.mgp_2016;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
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

public class GamePanelSurfaceView extends ParticleSystem implements SurfaceHolder.Callback {
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

    //Bubble variables
    private boolean Bubble_active;
    private Bitmap Bubble_bitmap, Bubble_Background;
    private Vector<Bubble> ListOfBubbles;
    private float timer = 0;
    // Red Bubble Index
    private Bitmap[] redBubble = new Bitmap[5];

    //Score
    private int Score;

    //Touch position
    private short touch_x,touch_y;

    //Sound
    //MediaPlayer SoundPlayer;
    MediaPlayer BGM;

    Vibrator v;

    //int i1 = r.nextInt(max - min + 1) + min;

    // Done by guan hui-------------------------------------------
    private Bitmap create_BitMap(int img, int scale_x, int scale_y)
    {
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), img),scale_x,scale_y,true);
    }
    //------------------------------------------------------------
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

        // Font
        Font = Typeface.createFromAsset(getContext().getAssets(), "fonts/cambriaz.ttf");

        // Week 7 Load images for Flying Stone animation
        //stone_anim = new SpriteAnimation(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.flystar)), (int) (ScreenWidth)/4, (int) (ScreenHeight)/10, true), 320, 64, 5, 5);
        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);
        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

        Score = 0;

        v = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE); // Done by Guan Hui
    }

    private void Bubble_Init() // Done by Bryan
    {
        Bubble_active = true;
        Bubble_bitmap = create_BitMap(R.drawable.blue_button,ScreenWidth/5,ScreenWidth/5);
        Bubble_Background = create_BitMap(R.drawable.button_background,ScreenWidth,ScreenHeight);
        redBubble[0] = create_BitMap(R.drawable.red_bubble1, ScreenWidth/5, ScreenWidth/5);
        redBubble[1] = create_BitMap(R.drawable.red_bubble2, ScreenWidth/5, ScreenWidth/5);
        redBubble[2] = create_BitMap(R.drawable.red_bubble3, ScreenWidth/5, ScreenWidth/5);
        redBubble[3] = create_BitMap(R.drawable.red_bubble4, ScreenWidth/5, ScreenWidth/5);
        redBubble[4] = create_BitMap(R.drawable.red_bubble5, ScreenWidth/5, ScreenWidth/5);

        //Create Bubbles
        ListOfBubbles = new Vector<Bubble>();
        Bubble NewBubble = new Bubble();
        ListOfBubbles.add(NewBubble);

        for (int i = 0; i < ListOfBubbles.size(); i++)
        {
            ListOfBubbles.get(i).Init();
        }
    }

    private void Sound_Init() // Done by Guan Hui
    {
        BGM = MediaPlayer.create(getContext(),R.raw.kasger_reflections);
        BGM.setLooping(true);
        BGM.start();
    }

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView(Context context)
    {
        // Context is the current state of the application/object
        super(context);
        Standard_Init(context);
        Bubble_Init();
        Sound_Init();
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
                Spawn_Bubbles(dt);
                if(ListOfBubbles.size() >= 10) // Done by Bryan
                {
                    GameState = 1;
                }
                for (int i = 0; i < ListOfBubbles.size(); i++) // Done by Bryan
                {
                    if (ListOfBubbles.get(i).Pop)
                    {
                        ListOfBubbles.get(i).Index++;
                        if (ListOfBubbles.get(i).Index == 4)
                        {
                            Score += 1;
                            ListOfBubbles.remove(i);
                        }
                    }
                }
                break;
            }
            case 1:
            {
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
            case 1:
                RenderGameplay(canvas);
                break;

        }
    }
    public void RenderScore(Canvas canvas) // Done by Guan Hui
    {
        RenderTextOnScreen(canvas,"Score: " + Integer.toString(Score),130, 105, 30);
    }

    private void Spawn_Bubbles(float dt) // Done by Bryan
    {
        if (timer < 2)//Done by Guan hui
            timer = timer + dt;
        else//(timer >= 2)
        {
            Bubble NewBubble = new Bubble();
            ListOfBubbles.add(NewBubble);
            NewBubble.Init();
            timer = 0;
        }
    }

    public void RenderBubbles(Canvas canvas) // Done by Bryan
    {
        canvas.drawBitmap(Bubble_Background,0,0,null);
        /*if (Bubble_active == true)
        {
            canvas.drawBitmap(Bubble_bitmap,ScreenWidth/2,ScreenHeight/2,null);
            canvas.drawBitmap(Bubble_bitmap,(ScreenWidth/2) - (ScreenWidth/5),ScreenHeight/2,null);
        }*/
        for (int i = 0; i < ListOfBubbles.size(); i++)
        {
            if (ListOfBubbles.get(i).Active)
                canvas.drawBitmap(redBubble[ListOfBubbles.get(i).Index],ListOfBubbles.get(i).Position_x,ListOfBubbles.get(i).Position_y,null);
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

        switch (GameState)
        {
            case 0:
            {
                RenderBubbles(canvas);
                RenderScore(canvas);
                //FPS
                RenderTextOnScreen(canvas, "FPS: " + FPS, 130, 75, 30);
                //Touch position
                RenderTextOnScreen(canvas, "X: " + Short.toString(touch_x) + "Y:" + Short.toString(touch_y), 130, 135, 30);
                //Timer
                RenderTextOnScreen(canvas, "Timer: " + (2 - timer), 130, 165, 30);
                //Game State
                RenderTextOnScreen(canvas, "Game State: " + GameState, 540, 75, 30);
                //Number of Bubbles
                RenderTextOnScreen(canvas, "Number of Bubbles: " + ListOfBubbles.size(), 540, 105, 30);
                break;
            }
            case 1: // Done by Bryan
            {
                canvas.drawBitmap(Bubble_Background,0,0,null);
                // Game State
                RenderTextOnScreen(canvas, "Game State: " + GameState, 400, 75, 30);
                // Game Over
                RenderTextOnScreen(canvas, "Game Over", ScreenWidth/4, ScreenHeight/2, 100);
                // Score
                RenderTextOnScreen(canvas,"Score: " + Integer.toString(Score),ScreenWidth/4, ScreenHeight/4*3, 80);
            }
        }
    }

    // Week 7 Print text on screen
    public void RenderTextOnScreen (Canvas canvas, String text, int posX, int posY, int textsize) // Done by Bryan
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

    public boolean CheckCollision (int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) // Done by Bryan
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

    public boolean CheckSphereCollision(int ball_x, int ball_y, int scale, int x2, int y2, int scale2) // Done by Bryan
    {
        return false;
    }

    public boolean CheckSphereOverlap(int ball_x1, int ball_y1, int scale1, int ball_x2, int ball_y2, int scale2)
    {
        float Difference_X = ball_x2 - ball_x1;
        float Difference_Y = ball_y2 - ball_y1;
        float distanceSquared = Difference_X * Difference_X + Difference_Y * Difference_Y;
        float combinedRadius = scale1 + scale2;

        if(distanceSquared <= combinedRadius * combinedRadius)
            return true;
        else
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
                for (int i = 0; i < ListOfBubbles.size(); i++) // Done by Bryan
                {
                    if (ListOfBubbles.get(i).Active)
                    {
                        for(int j = i+1; j < ListOfBubbles.size(); j++)
                        {
                            if (CheckCollision(ListOfBubbles.get(i).Position_x, ListOfBubbles.get(i).Position_y, ListOfBubbles.get(i).Scale, ListOfBubbles.get(i).Scale, X, Y, 0, 0))
                            {
                                ListOfBubbles.get(i).Pop = true;
                                /*if (CheckSphereOverlap(ListOfBubbles.get(i).Position_x, ListOfBubbles.get(i).Position_y, ListOfBubbles.get(i).Scale, ListOfBubbles.get(j).Position_x, ListOfBubbles.get(j).Position_y, ListOfBubbles.get(j).Scale))
                                {
                                    Score += 1;
                                    ListOfBubbles.get(j).Active = false;
                                }*/
                            }
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_UP:

                break;
        }return true;
    }
    private class Bubble // Done by guan hui
    {
        public int Position_x ,Position_y, Scale;
        public boolean Active;
        public int Anim,Max_Anim, Time_between_anim;
        public short Index; // Done by Bryan

        public boolean Pop;

        public void Init()
        {
            Position_x = getRandomInt(Scale, (ScreenWidth/5)*4); // Done by Bryan
            Position_y = getRandomInt(200+Scale, (ScreenHeight/5)*4); // Done by Bryan
            Active = true;
            Pop = false;
            Scale = (short)(ScreenWidth/5);
            Index = 0; // Done by Bryan
            Anim = 0;
            Max_Anim = 0;
            Time_between_anim = 0;
        }

        public void Update(float dt)
        {
            if (!Pop)
                return;

            if (Time_between_anim <= 200)
                Time_between_anim += dt;
            else if (Time_between_anim > 200)
            {
                if (Anim <= Max_Anim)
                {
                    Anim++;
                }
                else
                {
                    Active = false;
                    Anim = 0;
                }
            }
        }
    }
}