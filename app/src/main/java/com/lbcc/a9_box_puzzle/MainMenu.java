package com.lbcc.a9_box_puzzle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //defining animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

        //player name text field
        final EditText getname = findViewById(R.id.getname);
        if (MainActivity.object.current_user != null)
            getname.setHint("Hi " + MainActivity.object.current_user + " !");

        final TextView playButton = findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.startAnimation(myAnim);

                //unique name in leaderboard. so we convert to lowercase.
                name = getname.getText().toString().toLowerCase();

                //Before proceeding to start the game check if there is a current user playing (or a new user is trying to register by writing his name).
                //If both the cases not satisfying then its an error and we will show dialog box to enter name.
                if (MainActivity.object.current_user == null && name.length() == 0) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainMenu.this);
                    alertDialogBuilder.setTitle("Enter Username !");
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    if (name.length() != 0) {
                        MainActivity.object.userUpdate(name, getApplicationContext());
                    }
                    startActivity(new Intent(getApplicationContext(), setDifficulty.class));
                }
            }
        });

        final TextView infoButton = findViewById(R.id.info);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoButton.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(), info.class));
            }
        });

        final TextView howToPlayButton = findViewById(R.id.howtoplay);
        howToPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                howToPlayButton.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(), instructions.class));
            }
        });

        final TextView leaderBoardButton = findViewById(R.id.rules);
        leaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaderBoardButton.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(), performanceActivity.class));
            }
        });

        final TextView feedbackButton = findViewById(R.id.feedback);
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackButton.startAnimation(myAnim);
                String uritext = "mailto:lbccpvtgroup@gmail.com" + "?subject=" + Uri.encode("FEEDBACK REGARDING 9 BOX APP") + "&body=" + Uri.encode("FEEDBACK/suggestions/review");
                Uri uri = Uri.parse(uritext);
                Intent mail = new Intent(Intent.ACTION_SENDTO);
                mail.setData(uri);
                startActivity(Intent.createChooser(mail, "Send FEEDBACK:"));
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
                                homeIntent.addCategory(Intent.CATEGORY_HOME);
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeIntent);

//                                android.os.Process.killProcess(android.os.Process.myPid());
//                                System.exit(0);
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
