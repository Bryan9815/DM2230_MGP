package com.sidm.mgp_2016;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;

// Done by Bryan

public class ScorePage extends Activity implements OnClickListener // Done by Bryan
{
    private Button Btn_Return;

    // Define Shared Preferences
    SharedPreferences SharePrefScore;
    SharedPreferences SharePrefHighScore;
    SharedPreferences SharePrefName;
    SharedPreferences SharePrefHighName;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Hide App Title Bar and Menu Top Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide the Menu Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_score_page);

        Btn_Return = (Button)findViewById(R.id.Btn_Return);
        Btn_Return.setOnClickListener(this);

        // Define a text view for display score
        TextView CurrentScoreText ;
        CurrentScoreText = (TextView)findViewById(R.id.Current_Score);

        TextView HighScoreText;
        HighScoreText = (TextView)findViewById(R.id.High_Score);

        // Retrieve high score and player name
        int HighScore = 0;
        int CurrentScore = 0;
        String PlayerName = "Player";
        String HighPlayerName = "Player";

        SharePrefScore = getSharedPreferences("Current Score", Context.MODE_PRIVATE);
        CurrentScore = SharePrefScore.getInt("Current Score", 0);

        SharePrefHighScore = getSharedPreferences("High Score", Context.MODE_PRIVATE);
        HighScore = SharePrefHighScore.getInt("High Score", 0);

        SharePrefName = getSharedPreferences("Player Name", Context.MODE_PRIVATE);
        PlayerName = SharePrefName.getString("Player Name", "DEFAULT");

        SharePrefHighName = getSharedPreferences("High Player Name", Context.MODE_PRIVATE);
        HighPlayerName = SharePrefHighName.getString("High Player Name", "DEFAULT");

        CurrentScoreText.setText(String.format("Current Score: " + PlayerName + ": " + CurrentScore));
        HighScoreText.setText(String.format("High Score: " + HighPlayerName + ": " + HighScore));
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
