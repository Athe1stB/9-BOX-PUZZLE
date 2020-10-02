package com.example.a9_box_puzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class setDifficulty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_difficulty);

        Button easy,medium,hard;
        easy = findViewById(R.id.easybutton);
        medium = findViewById(R.id.mediumbutton);
        hard = findViewById(R.id.hardbutton);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchResult = new Intent(getApplicationContext(), game.class);
                launchResult.putExtra("key", 1);
                startActivity(launchResult);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchResult = new Intent(getApplicationContext(), game.class);
                launchResult.putExtra("key", 2);
                startActivity(launchResult);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
