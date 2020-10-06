package com.example.a9_box_puzzle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

        final EditText getname = findViewById(R.id.getname);
        if(MainActivity.object.current_user!=null)
        getname.setHint("Hi "+ MainActivity.object.current_user+" !");

        final TextView t1 = findViewById(R.id.play);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.startAnimation(myAnim);
                name = getname.getText().toString().toLowerCase();
                System.out.println(MainActivity.object.current_user);
                if(MainActivity.object.current_user==null && name.length()==0) {
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
                }
                else {
                    if (name.length() != 0) {
                        MainActivity.object.userUpdate(name, getApplicationContext());
                    }
                    Intent i = new Intent(getApplicationContext(), setDifficulty.class);
                    startActivity(i);
                }
            }
        });

        final TextView t2 = findViewById(R.id.info);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t2.startAnimation(myAnim);
                Intent i = new Intent(getApplicationContext(),info.class);
                startActivity(i);
            }
        });
        final TextView t3 = findViewById(R.id.howtoplay);
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t3.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(),instructions.class));
            }
        });

        final TextView t4  = findViewById(R.id.rules);
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t4.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(), performanceActivity.class));
            }
        });

        final TextView t5 = findViewById(R.id.feedback);
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t5.startAnimation(myAnim);
                String uritext = "mailto:lbccpvtgroup@gmail.com"+"?subject="+ Uri.encode("FEEDBACK REGARDING 9 BOX APP")+"&body="+Uri.encode("FEEDBACK/suggestions/review");
                Uri uri = Uri.parse(uritext);
                Intent mail = new Intent(Intent.ACTION_SENDTO);
                mail.setData(uri);
                startActivity(Intent.createChooser(mail,"Send FEEDBACK:"));
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
