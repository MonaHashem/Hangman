package com.test.android.hashem.mona.hangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GameActivity extends AppCompatActivity {

    GridLayout mGridLayout, mGridString;
    char word [];
    String token;
    Button mButtons[];
    TextView mRemaining, mLetters[];
    int rem = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        mGridString = (GridLayout) findViewById(R.id.grid_string);
        mRemaining = (TextView) findViewById(R.id.tv_remaining);
        mRemaining.setText(rem+"");
        mButtons = new Button [26];
        setButtons();
        getWord();
    }

    protected void getWord(){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
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
                    mLetters[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                    mLetters[i].setText(word[i]+"");
                    mLetters[i].getLayoutParams().width=140;
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
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String url ="http://hangman-api.herokuapp.com/hangman";

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("letter", ch);
            jsonObject.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.POST,url,jsonObject,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //handle response
                String wrd= null;
                boolean correct = false;
                try {
                    wrd= response.getString("hangman");
                    correct = response.getBoolean("correct");
                } catch (JSONException e) {
                    e.printStackTrace();

                }

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

    protected void setButtons(){

        for(int i = 0; i < 26; i++){
            mButtons[i] = new Button(this);
            mButtons[i].setLayoutParams(new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
            mButtons[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            String ch = (char)('A'+i)+"";
            mButtons[i].setText(ch);
            mButtons[i].getLayoutParams().width=140;
            mGridLayout.addView(mButtons[i]);

            mButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // make actions for clicking
                }
            });
        }
    }
}
