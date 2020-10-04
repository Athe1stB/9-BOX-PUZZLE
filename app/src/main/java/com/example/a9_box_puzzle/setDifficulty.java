package com.example.a9_box_puzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

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
                easy.setAnimation(myAnim);
                Intent launchResult = new Intent(getApplicationContext(), game.class);
                launchResult.putExtra("key", 1);
                startActivity(launchResult);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medium.setAnimation(myAnim);
                Intent launchResult = new Intent(getApplicationContext(), game.class);
                launchResult.putExtra("key", 2);
                startActivity(launchResult);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hard.setAnimation(myAnim);
                Intent launchResult = new Intent(getApplicationContext(), game.class);
                launchResult.putExtra("key", 3);
                startActivity(launchResult);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(setDifficulty.this, "Back Button is being Pressed!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
}
