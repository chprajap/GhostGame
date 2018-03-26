/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private FastDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private static String wordfragment="";
    public static TextView label ;
    public static TextView text;
    public static Button restart;
    public static Button challenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try{
            InputStream io = assetManager.open("words.txt");
            dictionary = new FastDictionary(io);

        }catch (Exception e){
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        text = (TextView) findViewById(R.id.ghostText);
        challenge=(Button)findViewById(R.id.challenge);
        restart=(Button)findViewById(R.id.restart);

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartmethod();
            }
        });
        onStart(null);

        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challenged();
            }
        });
    }

    private void challenged() {
        wordfragment=text.getText().toString();
        if(dictionary.isWord(wordfragment)==true && wordfragment.length()>=4){
            label.setText("User won");
            challenge.setEnabled(false);
        }
        else if(dictionary.getAnyWordStartingWith(wordfragment)==null && wordfragment.length()>=4){
            label.setText("User won");
        }
        else{
            label.setText("You lost the game");
            challenge.setEnabled(false);
        }
    }

    private void restartmethod() {

        text.setText("");
        challenge.setEnabled(true);
        wordfragment="";

        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        Log.d("Ghost", "onStart: started");
        userTurn = random.nextBoolean();
        label = (TextView) findViewById(R.id.gameStatus);

        text.setText("");
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }

        return true;
    }

    private void computerTurn() {
        wordfragment = text.getText().toString();
        if (dictionary.isWord(wordfragment)==true&&wordfragment.length()>=4) {
            label.setText("computer win");
            challenge.setEnabled(false);

        }else {
            String x = dictionary.getGoodWordStartingWith(wordfragment);
            if(x==null){
                label.setText("Computer Win the game"+"");
                challenge.setEnabled(false);
            }
            else {
                wordfragment=x;
                text.setText(wordfragment);
                userTurn = true;
                label.setText("Your Turn");
            }
        }
    }




    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char c = (char) event.getUnicodeChar();
        if (c>='a'&&c<='z') {
            Log.d("onkeyup","on keyup started");
            wordfragment=text.getText().toString();
            wordfragment += c;
            text.setText(wordfragment);
            if (dictionary.isWord(wordfragment)) {
                label.setText("user lost the game");
                challenge.setEnabled(false);
            }
            else if(dictionary.getAnyWordStartingWith(wordfragment)==null){
                label.setText("computer win the game");
                challenge.setEnabled(false);
            }
            else {
                computerTurn();
            }
        }
        else
            return super.onKeyUp(keyCode, event);
        return true;

    }
}
