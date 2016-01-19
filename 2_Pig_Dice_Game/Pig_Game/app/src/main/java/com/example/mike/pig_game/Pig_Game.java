package com.example.mike.pig_game;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import android.os.Handler;

public class Pig_Game extends AppCompatActivity {

    public static int player_score = 0;
    public static int cpu_score = 0;
    public static int player_turn_score = 0;
    public static int cpu_turn_score = 0;
    private final Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pig__game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pig__game, menu);
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

    public void diceRoll(View v){

        int val = getDiceValue();
        System.out.printf("player roll: %d\n", val);
        if (val != 1){
            player_turn_score += val;
        }else{
            System.out.println("Rolled 1");
            player_turn_score = 0;
            cpuTurn();
        }

    }

    private int getDiceValue(){
        int ret_val = r.nextInt(6) + 1;
        String png = "dice" + Integer.toString(ret_val);
        int drawableResourceId = this.getResources().getIdentifier(png, "drawable", this.getPackageName());
        Drawable d = getDrawable(drawableResourceId);
        ImageView image = (ImageView)findViewById(R.id.diepic);
        image.setImageDrawable(d);
        waitTime();
        return ret_val;
    }

    private void cpuTurn(){
        disableButtons();

        System.out.println("cpu turn");

        boolean rolled_one = false;

        for(int max_turns = r.nextInt(5) + 3; !rolled_one && max_turns > 0; --max_turns){
            int val = getDiceValue();
            System.out.printf("cpu roll: %d\n", val);
            if (val != 1){
                cpu_turn_score += val;
            }else{
                rolled_one = true;
            }
        }


        for(int diff = player_score - (cpu_turn_score + cpu_score); !rolled_one && diff > 0 ;){
            int val = getDiceValue();

            if (val != 1){
                cpu_turn_score += val;
            }else{
                rolled_one = true;
            }
        }

        if(!rolled_one){
            cpu_score += cpu_turn_score;
        }else{
            System.out.println("cpu rolled 1");
        }

        cpu_turn_score = 0;
        updateScore();
        enableButtons();
    }

    public void holdScore(View v){
        System.out.println("hold score");
        player_score += player_turn_score;
        player_turn_score = 0;
        updateScore();
        cpuTurn();
    }

    private void updateScore(){
        String p = Integer.toString(player_score);
        String c = Integer.toString(cpu_score);
        String newScore = "Your Score: "+p+" | Computer Score: "+c;
        TextView textView = (TextView)findViewById(R.id.scoreboard);
        textView.setText(newScore);
    }

    public void resetGame(View v){
        System.out.println("reset game");
        player_score = 0;
        cpu_score = 0;
        player_turn_score = 0;
        cpu_turn_score = 0;
        updateScore();
    }

    private void disableButtons(){
        Button roll_button = (Button) findViewById(R.id.roll_button);
        Button hold_button = (Button) findViewById(R.id.hold_button);
        Button reset_button = (Button) findViewById(R.id.reset_button);

        roll_button.setEnabled(false);
        hold_button.setEnabled(false);
        reset_button.setEnabled(false);
    }

    private void enableButtons(){
        Button roll_button = (Button) findViewById(R.id.roll_button);
        Button hold_button = (Button) findViewById(R.id.hold_button);
        Button reset_button = (Button) findViewById(R.id.reset_button);

        roll_button.setEnabled(true);
        hold_button.setEnabled(true);
        reset_button.setEnabled(true);
    }

    private void waitTime(){
        disableButtons();
        for (long i = 0; i < 500000000; i++) { }
        enableButtons();
    }
}
