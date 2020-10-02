package com.example.a9_box_puzzle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int c=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView t1 = findViewById(R.id.play);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),setDifficulty.class);
                startActivity(i);
            }
        });

        final TextView t2 = findViewById(R.id.info);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),info.class);
                startActivity(i);
            }
        });
        final TextView t3 = findViewById(R.id.credits);
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),afterwin.class);
                startActivity(i);
            }
        });

        TextView t4  = findViewById(R.id.rules);
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpClass popUpClass = new PopUpClass();
                popUpClass.showPopupWindow(v);
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                homeIntent.addCategory( Intent.CATEGORY_HOME );
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeIntent);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
