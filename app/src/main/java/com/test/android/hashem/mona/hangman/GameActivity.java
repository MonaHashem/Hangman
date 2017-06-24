package com.test.android.hashem.mona.hangman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity {

    GridLayout mGridLayout, mGridString;
    char word [];
    String token;
    Button mButtons[] ,mPlayAgain;
    TextView mRemaining,mGameStatus, mLetters[];
    LinearLayout mEndGame;
    RequestQueue requestQueue;
    int rem = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        mGridString = (GridLayout) findViewById(R.id.grid_string);
        mRemaining = (TextView) findViewById(R.id.tv_remaining);
        mPlayAgain = (Button) findViewById(R.id.play_again);
        mEndGame = (LinearLayout) findViewById(R.id.game_end_layout);
        mGameStatus = (TextView) findViewById(R.id.tv_game_status);
        mRemaining.setText(rem+"");
        mButtons = new Button [26];
        requestQueue= Volley.newRequestQueue(this);
        mPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameActivity.this, GameActivity.class));
            }
        });
        setButtons();
        getWord();
    }

    protected void getWord(){

        String url ="http://hangman-api.herokuapp.com/hangman";

        JSONObject jsonObject= new JSONObject();




        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.POST,url,jsonObject,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //handle response
                String wrd= null;

                try {
                    wrd= response.getString("hangman");
                    token = response.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                word = wrd.toCharArray();

                int size = word.length;


                mGridString.setColumnCount(size);
                mLetters = new TextView[size];
                for(int i = 0; i < size; i++){
                    mLetters[i] = new TextView(GameActivity.this);
                    mLetters[i].setLayoutParams(new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
                    mLetters[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    mLetters[i].setText(word[i]+"");
                    mLetters[i].getLayoutParams().width=80;
                    mGridString.addView(mLetters[i]);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Toast.makeText(GameActivity.this,error.toString(),Toast.LENGTH_LONG);

            }
        });

        requestQueue.add(jsonObjectRequest);

    }


    protected void guessLetter(char ch){

        String url ="http://hangman-api.herokuapp.com/hangman?";
        url += "token="+token+"&letter=" + ch;
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.PUT,url,null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //handle response
                String wrd= null;
                boolean correct = false;
                try {
                    wrd= response.getString("hangman");
                    correct = response.getBoolean("correct");
                    if(correct)
                    {
                        char temp [] = wrd.toCharArray();
                        for(int i = 0; i < temp.length; i++)
                            if(temp[i] !='_') {
                                mLetters[i].setText(wrd.charAt(i) + "");
                                word[i] = temp[i];
                            }
                        boolean finish = true;
                        for(int i = 0; i < word.length; i++)
                            if(word[i] == '_') {
                                finish = false;
                                break;
                            }
                        if(finish)
                            won();
                    }
                    else
                    {
                        rem--;
                        mRemaining.setText(rem+"");
                        if(rem == -1)
                            lose();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Toast.makeText(GameActivity.this,error.toString(),Toast.LENGTH_LONG);

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                        System.out.println("res " +res);
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    protected void setButtons(){

        for(int i = 0; i < 26; i++){
            mButtons[i] = new Button(this);
            mButtons[i].setLayoutParams(new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
            mButtons[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            String ch = (char)('A'+i)+"";
            mButtons[i].setText(ch);

            mButtons[i].getLayoutParams().width=140;
            mGridLayout.addView(mButtons[i]);
            final int j = i;
            mButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guessLetter((char)(j+'a'));
                    mButtons[j].setClickable(false);
                    mButtons[j].setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    protected void won(){

        mEndGame.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreference = getSharedPreferences(getResources().getString(R.string.shared_preference),MODE_PRIVATE);

        int score = sharedPreference.getInt(getResources().getString(R.string.score),0);
        score += rem +1;
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putInt(getResources().getString(R.string.score), score);
        editor.commit();
    }

    protected void lose(){
        mGameStatus.setText("Unfortunately you lost :(");
        mEndGame.setVisibility(View.VISIBLE);
    }

}
