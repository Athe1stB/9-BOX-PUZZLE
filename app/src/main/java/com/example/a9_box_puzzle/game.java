package com.example.a9_box_puzzle;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

public class game extends AppCompatActivity {

    Boolean popupopen=false; //check state of popup(open or close)
    Boolean newameandsol=false; // to check whether newgame/solution pressed or not
    Button b[][] = new Button[3][3];
    int ar[][] = new int[3][3];
    int moves=0,paused=0,level=0;
    Files ob = MainActivity.object;
    String puzzle="";
    int currentpuzzle[][] = new int[3][3]; //current puzzle
    PopupWindow popupWindow;


    private static final int[] BUTTON_IDS = {R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6, R.id.b7, R.id.b8, R.id.b9,};
    Chronometer chronometer;
    long stopTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

        stopTime=0;
        chronometer = (Chronometer)findViewById(R.id.timer);
        startChronometer();

        Bundle extras = getIntent().getExtras();
        level = extras.getInt("key");

        int idc=0;

        final Button sol = findViewById(R.id.seesolution);
        sol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResults();
                newameandsol=true;
                moves=0;
                sol.setAnimation(myAnim);
                pauseChronometer();
                Intent sendtosol = new Intent(getApplicationContext(),solution.class);
                String puzzle="";
                for(int i=0; i<3; i++) {
                    for(int j=0; j<3; j++)
                    {
                        puzzle = puzzle+ Integer.toString(currentpuzzle[i][j]);
                    }
                }
                System.out.println(puzzle);
                sendtosol.putExtra("key",puzzle);
                startActivity(sendtosol);
                finish();
            }
        });

        Button newgame = findViewById(R.id.newgame);
        newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResults();
                newameandsol=true;
                startActivity(new Intent(getApplicationContext(),setDifficulty.class));
                finish();
            }
        });

        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++)
            {
                b[i][j]=(Button) findViewById(BUTTON_IDS[idc]); idc++;
            }
        }

        //check(getApplicationContext());
        int getpuzzle[][] = create(getApplicationContext(),level);

        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++)
            {
                currentpuzzle[i][j]=getpuzzle[i][j];
            }
        } //current puzzle

        Button restart = findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rand(currentpuzzle);
                popupopen=false;
                play();
            }
        });


        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++)
            {
                puzzle = puzzle+ Integer.toString(currentpuzzle[i][j]);
            }
        }
        System.out.println(puzzle);

        TextView bestval = findViewById(R.id.bestval);
        String temp = " "+ ob.moves.get(puzzle);
        bestval.setText(temp);

        rand(getpuzzle);
        play();
    }

    void play() {
        if(!havewon())//gamestate=false
        {
            for(int i=0; i<3; i++)
            {
                for(int j=0; j<3; j++)
                {
                    final int chi=i,chj=j;
                    b[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           if(isvalidmove(chi,chj))
                           {
                               play();
                           }
                        }
                    });
                }
            }
        }
        else
        {
            setDColor();
            pauseChronometer();
            Toast msg = Toast.makeText(getApplicationContext(), "Hurray!! You WON!!", Toast.LENGTH_SHORT);
            msg.show();
            for(int i=0; i<3; i++) {
                for(int j=0; j<3; j++)
                {
                    b[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            } //seize grid;

            Button newgame,restart,seesol;
            newgame = findViewById(R.id.newgame);
            restart = findViewById(R.id.restart);
            seesol= findViewById(R.id.seesolution);
            newgame.setVisibility(View.GONE); restart.setVisibility(View.GONE); seesol.setVisibility(View.GONE);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    ScrollView v = findViewById(R.id.gamescrollview);
                    aftersleep(v);
                }
            }, 1000);

            Log.v("winning status", "won !!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    boolean isvalidmove(int x, int y) {
        int iposs[] = {0,-1,0,1};
        int jposs[] = {-1,0,1,0};
        for(int k=0; k<4; k++)
        {
            int X = x+iposs[k];
            int Y = y+jposs[k];

            if(X<3 && X>=0 && Y>=0 && Y<3)
            {
                String s = b[X][Y].getText().toString();
                if(s.compareTo(" ")==0)
                {
                    b[X][Y].setText(b[x][y].getText().toString());
                    b[x][y].setText(" ");
                    setColor();
                    moves++;
                    TextView movebox = findViewById(R.id.moveval);
                    movebox.setText(Integer.toString(moves));
                    return true;
                }
            }
        }
        return false;
    }

    void setColor() {
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                String s; s = b[i][j].getText().toString();
                if(s.compareToIgnoreCase(" ")==0)
                {
                    b[i][j].setBackgroundColor(getResources().getColor(R.color.white));
                }
                else
                {
                    b[i][j].setBackgroundResource(R.drawable.box_apperance);
                }
            }
        }
    }

    void setDColor() {
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                 b[i][j].setBackgroundColor(getResources().getColor(R.color.green));
            }
        }
    }

    boolean havewon() {
        int c=1;
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                if(c==9)break;
                String s,s1; s= b[i][j].getText().toString();
                s1=Integer.toString(c);
                if(s.compareToIgnoreCase(s1)!=0)
                {
                    return false;
                }
                c++;
            }
        }

        //after winning perform the tasks below
        return true;
    }

    void rand(int[][] puzzle) {

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                String s = Integer.toString(puzzle[i][j]);
                if(s.compareToIgnoreCase("9")==0)s=" ";
                b[i][j].setText(s);
            }
        }
        setColor();
    } //implements puzzle to buttons

    public static void print(String s) {
        for(int i = 0; i < 9; ++i) {
            if(s.charAt(i) == '9') System.out.print("  ");
            else System.out.print(s.charAt(i) + " ");
            if(i%3==2) System.out.println();
        }
        System.out.println();
    }

    public static int[][] strToArr(String s) {
        int[][] a = new int[3][3];
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                a[i][j] = s.charAt(3*i+j)-48;
        return a;
    }

    int[][] create(Context context, int level) {

            int i = 0, j, size = 181440, id=0 , hard;

            Random rd = new Random();

            // hard = 1 Easy, 2 Medium, 3 Hard

            hard = level;
            while(id==0) {
                id = rd.nextInt(ob.difficulty[hard] - ob.difficulty[hard - 1]) + ob.difficulty[hard - 1];
            }
           //id = puzzle identity (initial state)
            String puzzle = ob.pattern[id];

            int arr[][] = strToArr(puzzle);

            return arr;

    } //generate puzzle

    void aftersleep(View v) {
       showWinPopupWindow(v,1);
   }

    ViewGroup rootclearDim;

    public void showWinPopupWindow(final View view,int flag) {
        popupopen=true;
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup, null);
        final ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
        rootclearDim = root;
        applyDim(root, 0.87f);

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = false;

        //Create a window with our parameters
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.setOutsideTouchable(false);
        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextView maint = popupView.findViewById(R.id.popupmaintext);

        TextView movestext = popupView.findViewById(R.id.popupmoves);
        String s = moves+" Moves";
        movestext.setText(s);

        Chronometer timetext = popupView.findViewById(R.id.popuptime);
        setCurrentTime(timetext,stopTime);

        final Button resumebutton = popupView.findViewById(R.id.popupresume);
        final Button restartButton = popupView.findViewById(R.id.popuprestart);

        if(flag==0) {
            LinearLayout l1 = popupView.findViewById(R.id.t1);
            LinearLayout l2 = popupView.findViewById(R.id.t2);
            maint.setText("PAUSED");
            l1.setVisibility(View.GONE); l2.setVisibility(View.GONE);
        }
        else {
            resumebutton.setVisibility(View.GONE);
            restartButton.setVisibility(View.GONE);
        }

        resumebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button newgame,restart,seesol;
                newgame = findViewById(R.id.newgame);
                restart = findViewById(R.id.restart);
                seesol= findViewById(R.id.seesolution);
                newgame.setVisibility(View.VISIBLE); restart.setVisibility(View.VISIBLE); seesol.setVisibility(View.VISIBLE);
                resumebutton.setAnimation(myAnim);
                startChronometer();
                popupWindow.dismiss();
                clearDim(root);
                popupopen=false;
            }
        });

        final Button seesolution = popupView.findViewById(R.id.popupsolution);
        seesolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResults();
                popupWindow.dismiss();
                moves=0;
                seesolution.setAnimation(myAnim);
                pauseChronometer();
                Intent sendtosol = new Intent(getApplicationContext(),solution.class);
                String puzzle="";
                for(int i=0; i<3; i++) {
                    for(int j=0; j<3; j++)
                    {
                        puzzle = puzzle+ Integer.toString(currentpuzzle[i][j]);
                    }
                }
                System.out.println(puzzle);
                sendtosol.putExtra("key",puzzle);
                startActivity(sendtosol);
                finish();
            }
        });

        final Button exitame = popupView.findViewById(R.id.popupexitgame);
        exitame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResults();
                exitame.setAnimation(myAnim);
                popupWindow.dismiss();
                startActivity(new Intent(getApplicationContext(),MainMenu.class));
                finish();
            }
        });

        final Button newgame = popupView.findViewById(R.id.popupnewgame);
        newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResults();
                popupWindow.dismiss();
                clearDim(root);
                newgame.setAnimation(myAnim);
                //As an example, display the message
                Toast.makeText(view.getContext(), "NEW GAME", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),setDifficulty.class);
                startActivity(i);
                finish();
            }
        });

        //Handler for clicking on the inactive zone of the window
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupopen=false;
                Button newgame,restart,seesol;
                newgame = findViewById(R.id.newgame);
                restart = findViewById(R.id.restart);
                seesol= findViewById(R.id.seesolution);
                newgame.setVisibility(View.VISIBLE); restart.setVisibility(View.VISIBLE); seesol.setVisibility(View.VISIBLE);
                restartButton.setAnimation(myAnim);
                clearDim(root);
                restartButton.setAnimation(myAnim);
                rand(currentpuzzle);
                popupWindow.dismiss();
                startChronometer();
                play();
            }
        });

    }

    public static void applyDim(@NonNull ViewGroup parent, float dimAmount){
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }

    @Override
    public void onBackPressed() {
        ScrollView v = findViewById(R.id.gamescrollview);
        if(!havewon()) {
            if (!popupopen) {
                pauseChronometer();
                Button newgame,restart,seesol;
                newgame = findViewById(R.id.newgame);
                restart = findViewById(R.id.restart);
                seesol= findViewById(R.id.seesolution);
                newgame.setVisibility(View.GONE); restart.setVisibility(View.GONE); seesol.setVisibility(View.GONE);
                showWinPopupWindow(v, 0);
            } else {
                Button newgame,restart,seesol;
                newgame = findViewById(R.id.newgame);
                restart = findViewById(R.id.restart);
                seesol= findViewById(R.id.seesolution);
                newgame.setVisibility(View.VISIBLE); restart.setVisibility(View.VISIBLE); seesol.setVisibility(View.VISIBLE);
                clearDim(rootclearDim);
                popupWindow.dismiss();
                popupopen = false;
                startChronometer();
            }
        }
    }

    void pauseChronometer() {stopTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();}

    void resetChronometer() {chronometer.setBase(SystemClock.elapsedRealtime());
        stopTime = 0;}

    void startChronometer() {chronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
        chronometer.start();}

    public void setCurrentTime(Chronometer c , long time) {
        stopTime = time;
        c.setBase(SystemClock.elapsedRealtime() + time);
    }

    @Override
    protected void onPause() {
        pauseChronometer();
        ScrollView v = findViewById(R.id.gamescrollview);
        if(!popupopen && !newameandsol)
        {
            Button newgame,restart,seesol;
            newgame = findViewById(R.id.newgame);
            restart = findViewById(R.id.restart);
            seesol= findViewById(R.id.seesolution);
            newgame.setVisibility(View.VISIBLE); restart.setVisibility(View.VISIBLE); seesol.setVisibility(View.VISIBLE);
            showWinPopupWindow(v,0);
        }

        super.onPause();
    }

    void updateResults()
    {
        int win; if(havewon())win=1; else win=0;
        MainActivity.object.performanceUpdate(MainActivity.object.current_user,moves, ob.moves.get(puzzle),0,level,win,getApplicationContext());
    }
}
