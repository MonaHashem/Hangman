package com.test.android.hashem.mona.hangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    GridLayout gridLayout;
    char word [];
    Button buttons [];
    TextView remaining;
    int rem = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        remaining = (TextView) findViewById(R.id.tv_remaining);
        remaining.setText(rem+"");
        buttons = new Button [26];
        setButtons();
    }

    protected void setButtons(){

        for(int i = 0; i < 26; i++){
            buttons[i] = new Button(this);
            buttons[i].setLayoutParams(new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
            buttons[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            String ch = (char)('A'+i)+"";
            buttons[i].setText(ch);
            buttons[i].getLayoutParams().width=140;
            gridLayout.addView(buttons[i]);

            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // make actions for clicking
                }
            });
        }
    }
}
