package com.sidm.mgp_2016;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Paint;
import android.os.Vibrator;
import android.widget.EditText;
import android.widget.Toast;


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
    public float dt;

    private short GameState;

    // Paint object
    Paint paint = new Paint();

    // 5) bitmap array to stores 4 images of the spaceship
    private Bitmap[] ship = new Bitmap[4];

    // 6) Variable as an index to keep track of the spaceship images
    private short shipIndex = 0;

    // Font
    Typeface Font;

    private short bgX = 0, bgY = 0;

    private short mX = 100, mY = 0;

    private int aX = 300, aY = 300;

    //Bubble variables
    private boolean Bubble_active;
    private Bitmap Bubble_bitmap, Bubble_Background;
    private float timer = 0;
    // Red Bubble Index
    private Bitmap[] redBubble = new Bitmap[5];

    //Score
    private int Score;
    private int Energy;
    private int tempEnergy;

    //Touch position
    private short touch_x,touch_y;

    //Sound
    //MediaPlayer SoundPlayer;
    MediaPlayer BGM;

    Vibrator v;

    // Week 13
    CharSequence toast_Text;
    int toast_Time;
    Toast toast;

    public boolean showAlert = false;
    AlertDialog.Builder alert = null;
    private Alert AlertObj;
    Activity activityTracker;

    // High Score
    SharedPreferences SharePrefScore;
    SharedPreferences.Editor EditScore;
    SharedPreferences SharePrefHighScore;
    SharedPreferences.Editor EditHighScore;
    int HighScore;

    // Player Name
    SharedPreferences SharePrefName;
    SharedPreferences.Editor EditName;
    SharedPreferences SharePrefHighName;
    SharedPreferences.Editor EditHighName;
    String PlayerName;

    //int i1 = r.nextInt(max - min + 1) + min;

    // Done by guan hui-------------------------------------------
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

        // Font
        Font = Typeface.createFromAsset(getContext().getAssets(), "fonts/cambriaz.ttf");

        // Week 7 Load images for Flying Stone animation
        //stone_anim = new SpriteAnimation(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.flystar)), (int) (ScreenWidth)/4, (int) (ScreenHeight)/10, true), 320, 64, 5, 5);
        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);
        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

        v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE); // Done by Guan Hui
        
        Score = 0;
        GameState = 0;
        Energy = 1000;
        tempEnergy = Energy;

        // 7) Load the images of the spaceships
        ship[0] = Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.ship2_1)), (int) (ScreenWidth) / 10, (int) (ScreenHeight) / 10, true);
        ship[1] = Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.ship2_2)), (int) (ScreenWidth) / 10, (int) (ScreenHeight) / 10, true);
        ship[2] = Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.ship2_3)), (int) (ScreenWidth) / 10, (int) (ScreenHeight) / 10, true);
        ship[3] = Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.ship2_4)), (int) (ScreenWidth) / 10, (int) (ScreenHeight) / 10, true);
    }
    //------------------------------------------------------------

    private void Sound_Init() // Done by Guan Hui
    {
        BGM = MediaPlayer.create(getContext(),R.raw.kasger_reflections);
        BGM.setLooping(true);
        BGM.start();
    }

    public void startVibrate() // Done by Guan Hui
    {
        long pattern[] = {0,50,0};
        v.vibrate(pattern,-1);
        Log.v(TAG,"test if vibrate occurs");
    }

    public void stopVibrate()
    {
        v.cancel();
    } // Done by Guan Hui

    public void ToastMessage(Context context) // Done by Bryan
    {
        // For Toast
        toast_Text = "HIT!";
        toast_Time = Toast.LENGTH_SHORT;
        toast = Toast.makeText(context, toast_Text, toast_Time);
    }

    public void AlertInit(Activity activity) // Done by Bryan
    {
        // To track an activity
        activityTracker = activity;

        // Create Alert Dialog
        AlertObj = new Alert(this);
        alert = new AlertDialog.Builder(getContext());

        // Allow plaers to input their name
        final EditText input = new EditText(getContext());

        // Define the input method where 'enter' key is disabled
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        // Define max of 20 characters to be entered for 'Name' field
        int maxLength = 20;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        input.setFilters(FilterArray);

        // Set up the alert dialog
        alert.setTitle("Game Over");
        alert.setMessage("Please enter your name");
        alert.setCancelable(false);

        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1)
            {
                PlayerName = input.getText().toString();
                if(Score > HighScore)
                {
                    EditHighName.putString("High Player Name", PlayerName);
                    EditHighName.commit();
                }
                else
                {
                    EditName.putString("Player Name", PlayerName);
                    EditName.commit();
                }
                if (Score > HighScore)
                {
                    HighScore = Score;
                    EditHighScore.putInt("High Score", HighScore);
                    EditHighScore.commit();
                }
                Intent intent = new Intent();
                intent.setClass(getContext(), ScorePage.class);
                activityTracker.startActivity(intent);
            }
        });
    }

    public void SharedPreferencesInit() // Done by Bryan
    {
        SharePrefScore = getContext().getSharedPreferences("Current Score", Context.MODE_PRIVATE);
        EditScore = SharePrefScore.edit();
        
        SharePrefHighScore = getContext().getSharedPreferences("High Score", Context.MODE_PRIVATE);
        EditHighScore = SharePrefHighScore.edit();
        HighScore = SharePrefHighScore.getInt("High Score", 0);

        SharePrefName = getContext().getSharedPreferences("Player Name", Context.MODE_PRIVATE);
        EditName = SharePrefName.edit();
        PlayerName = "Player";

        SharePrefHighName = getContext().getSharedPreferences("High Player Name", Context.MODE_PRIVATE);
        EditHighName = SharePrefHighName.edit();
    }

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView(Context context, Activity activity)
    {
        // Context is the current state of the application/object
        super(context);

        Standard_Init(context);
        Sound_Init();
        ToastMessage(context);
        AlertInit(activity);
        SharedPreferencesInit();
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
            stopVibrate();
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

    public void update(float dt, float fps)
    {
        FPS = fps;
        this.dt = dt;

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
                // Done by Bryan
                // Energy and Score stuff
                // ************************************
                if (tempEnergy >= (Energy + 50))
                {
                    tempEnergy = Energy;
                    Score++;
                }
                if(tempEnergy < Energy)
                    tempEnergy = Energy;
                if(Energy > 0)
                    Energy -= 1;
                else
                {
                    if(!showAlert)
                    {
                        AlertObj.RunAlert();
                        showAlert = true;

                        EditScore.putInt("Current Score", Score);
                        EditScore.commit();
                    }
                    startVibrate();
                }
                // Character
                shipIndex++;
                shipIndex %= 4;
                mY += 10;

                if(mY >= ScreenHeight)
                {
                    if(!showAlert)
                    {
                        AlertObj.RunAlert();
                        showAlert = true;
                    }
                    startVibrate();
                }
                // ************************************
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
        RenderTextOnScreen(canvas,"Energy: " + Integer.toString(Energy),130, 45, 30);
        RenderTextOnScreen(canvas,"Temp Energy: " + Integer.toString(tempEnergy),300, 45, 30);
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
                canvas.drawBitmap(scaledbg, bgX, bgY, null);
                canvas.drawBitmap(scaledbg, bgX + ScreenWidth, bgY, null);

                // 8) Draw the spaceships
                canvas.drawBitmap(ship[shipIndex], mX, mY, null);

                RenderScore(canvas);
                //FPS
                RenderTextOnScreen(canvas, "FPS: " + FPS, 130, 75, 30);
                //Touch position
                RenderTextOnScreen(canvas, "X: " + Short.toString(touch_x) + "   Y:" + Short.toString(touch_y), 130, 135, 30);
                //Timer
                //RenderTextOnScreen(canvas, "Timer: " + (2 - timer), 130, 165, 30);
                //Game State
                RenderTextOnScreen(canvas, "Game State: " + GameState, 540, 75, 30);

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

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();// Check for the action of touch

        short X = touch_x = (short) event.getX();
        short Y = touch_y = (short) event.getY();

        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                toast.show();
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                startVibrate();
                break;
            }
        }return true;
    }
}