package com.lbcc.a9_box_puzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class setDifficulty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_difficulty);

        final Button easy,medium,hard;
        easy = findViewById(R.id.easybutton);
        medium = findViewById(R.id.mediumbutton);
        hard = findViewById(R.id.hardbutton);

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easy.startAnimation(myAnim);
                Intent launchResult = new Intent(getApplicationContext(), game.class);
                launchResult.putExtra("key", 1);
                startActivity(launchResult);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medium.startAnimation(myAnim);
                Intent launchResult = new Intent(getApplicationContext(), game.class);
                launchResult.putExtra("key", 2);
                startActivity(launchResult);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hard.startAnimation(myAnim);
                Intent launchResult = new Intent(getApplicationContext(), game.class);
                launchResult.putExtra("key", 3);
                startActivity(launchResult);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),MainMenu.class);
        startActivity(i);
        finish();
    }
}
