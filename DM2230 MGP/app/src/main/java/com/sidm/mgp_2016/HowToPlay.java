package com.sidm.mgp_2016;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;


// Done by Bryan

public class HowToPlay extends Activity implements OnClickListener
{
    private Button Btn_Return;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Hide App Title Bar and Menu Top Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide the Menu Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_how_to_play);

        // Return to Main Menu button
        Btn_Return = (Button)findViewById(R.id.Btn_Return);
        Btn_Return.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        Intent i = new Intent();
        if (v == Btn_Return)
        {
            i.setClass(this, MainMenu.class);
        }
        startActivity(i);
    }
}
