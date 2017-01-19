package com.sidm.mgp_2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;

public class MainMenu extends Activity implements OnClickListener
{
    private Button Btn_Start;
    private Button Btn_Options;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Hide App Title Bar and Menu Top Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide the Menu Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_menu);

        Btn_Start = (Button)findViewById(R.id.Btn_Start);
        Btn_Start.setOnClickListener(this);

        Btn_Options = (Button)findViewById(R.id.Btn_Options);
        Btn_Options.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        Intent i = new Intent();
        if (v == Btn_Start)
        {
            i.setClass(this, GamePage.class);
        }
        else if (v == Btn_Options)
        {
            i.setClass(this, ScorePage.class);
        }
        startActivity(i);
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
