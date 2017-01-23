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

public class Options extends Activity implements OnClickListener
{

    private Button Btn_Return;
    // Volume Slider
    SharedPreferences SharePrefVolume;
    SharedPreferences.Editor EditVolume;
    SeekBar volumeSlider;

    // Vibration On/Off
    SharedPreferences SharePrefVibration;
    SharedPreferences.Editor EditVibration;
    ToggleButton vibrateToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Hide App Title Bar and Menu Top Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide the Menu Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_options);

        // Return to Main Menu button
        Btn_Return = (Button)findViewById(R.id.Btn_Return);
        Btn_Return.setOnClickListener(this);

        // Volume Slider
        SharePrefVolume = getSharedPreferences("Volume", MODE_PRIVATE);
        EditVolume = SharePrefVolume.edit();

        volumeSlider = (SeekBar)findViewById(R.id.volumeSlider_Bar);
        volumeSlider.setProgress(SharePrefVolume.getInt("Volume", 0));

        // Vibration toggle button
        SharePrefVibration = getSharedPreferences("VibrationToggle", MODE_PRIVATE);
        EditVibration = SharePrefVibration.edit();

        vibrateToggle = (ToggleButton)findViewById(R.id.vibrateToggle_Button);
        vibrateToggle.setChecked(SharePrefVibration.getBoolean("VibrationToggle", true));
    }

    public void onClick(View v)
    {
        Intent i = new Intent();
        if (v == Btn_Return)
        {
            EditVolume.putInt("Volume", volumeSlider.getProgress());
            EditVolume.commit();
            EditVibration.putBoolean("VibrationToggle", vibrateToggle.isChecked());
            EditVibration.commit();
            i.setClass(this, MainMenu.class);
        }
        startActivity(i);
    }
}
