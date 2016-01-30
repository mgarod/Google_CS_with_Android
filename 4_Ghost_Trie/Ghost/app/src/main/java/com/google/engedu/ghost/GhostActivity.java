package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import java.io.IOException;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String fragment = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        try{
            dictionary = new FastDictionary(getApplicationContext()
                    .getAssets().open("words.txt"));
           //dictionary = new SimpleDictionary(getApplicationContext()
             //       .getAssets().open("words.txt"));

        }catch (IOException e) {
            System.out.println("Caught IOException:\n"+e);
        }

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

    private void computerTurn() {
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        String s;
        // Do computer turn stuff then make it the user's turn again

        // CPU is the first turn
        if (fragment.length() == 0){
            char c = (char)(random.nextInt(26) + 97);
            fragment = new String();
            //fragment = c + fragment;
            fragment = fragment + c;
            text.setText(fragment);
            userTurn = true;
            label.setText(USER_TURN);
            return;
        }

        // Check the player's last move
        if (fragment != null && fragment.length() >= 4) {
            if (dictionary.isWord(fragment)) {
                s = fragment + " is a word!";
                text.setText(s);
                label.setText("Computer wins");
                return;
            }
        }

        // Get a word with the fragment
        s = dictionary.getGoodWordStartingWith(fragment);


        if (s == fragment){
            char c = (char)(random.nextInt(26) + 97);
            fragment = fragment + c;
            text.setText(fragment);
            userTurn = true;
            label.setText(USER_TURN);
            return;
        }

        // No possible word from fragment
        if (s == null){
            s = "No possible word " + fragment + "_";
            text.setText(s);
            label.setText("Computer wins");
            return;
        }

        if ( fragment != null /* && s != null */) {
            //s = s.substring(fragment.length()-1, fragment.length()) + fragment;
            //text.setText(s);
            //char c = (char)(random.nextInt(26) + 97);
            char c = s.charAt(fragment.length());
            fragment = fragment + c;
            text.setText(fragment);
            userTurn = true;
            label.setText(USER_TURN);
            return;
        }
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        System.out.println("Clicked reset");

        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        fragment = new String();
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);

        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    /**
     * Handler for the "Challenge" button.
     * Checks whether or not the given fragment is a valid prefix
     * @param view
     * @return true
     */
    public boolean challenge(View view) {
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);

        if (fragment.length() >= 4) {
            if (dictionary.isWord(fragment)){
                String a = fragment + " is a word!";
                text.setText(a);
                label.setText("Player wins");
                return true;
            }
        }

        String s = dictionary.getAnyWordStartingWith(fragment);

        if ( s != null ) {
            s = "This is a valid word:\n" + s;
            text.setText(s);
            label.setText("Computer wins");
        } else {
            s = "No possible word: " + fragment + "_";
            text.setText(s);
            label.setText("Player wins");
        }

        return false;
    }

    /**
     * onKeyUp
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        if( event.getUnicodeChar() < 'a' || event.getUnicodeChar() > 'z' ){
            System.out.println("Invalid key pressed");

            return super.onKeyUp(keyCode, event);
        }
        else {
            System.out.println("Valid key pressed");

            fragment = (fragment + event.getDisplayLabel()).toLowerCase();
            TextView text = (TextView) findViewById(R.id.ghostText);
            text.setText(fragment);
            computerTurn();

            return true;
        }
    }
}
