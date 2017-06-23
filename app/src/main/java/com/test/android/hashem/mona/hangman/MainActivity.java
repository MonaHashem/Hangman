package com.test.android.hashem.mona.hangman;

import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView mScoreDisplay;
    Button mNewGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScoreDisplay = (TextView) findViewById(R.id.tv_score);
        mNewGame = (Button) findViewById(R.id.button_new_game);
        editScore();

    }

    protected void editScore(){
        SharedPreferences sharedPreference = getSharedPreferences(getResources().getString(R.string.shared_preference),MODE_PRIVATE);
        int score = sharedPreference.getInt(getResources().getString(R.string.score),0);
        mScoreDisplay.setText(score+"");
    }
}
