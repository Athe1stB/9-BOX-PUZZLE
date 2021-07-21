package com.lbcc.a9_box_puzzle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;

public class game extends AppCompatActivity {

    Boolean isPopUpOpen = false; //check state of popup(open or close)
    Boolean isNewgameOrSolutionPressed = false; // to check whether newGameButton/solution pressed or not
    Button[][] b = new Button[3][3];//these buttons make up the screen
    int moves = 0;
    int level = 0;//easy,medium,hard (1,2,3 respectively)
    Files ob = MainActivity.object;
    String puzzle = "";
    int[][] currentPuzzle = new int[3][3]; //current puzzle
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

        stopTime = 0;
        chronometer = (Chronometer) findViewById(R.id.timer);
        startChronometer();

        //to fetch difficulty level from sent intent(from setdifficulty.class)
        Bundle extras = getIntent().getExtras();
        level = extras.getInt("key");
        
        final Button seeSolutionButton = findViewById(R.id.seesolution);
        seeSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResults(); //looking solution means either you gave up or you have won and looking for the best solution. which updates results terminating your ranked gameplay.
                seeSolutionButton.startAnimation(myAnim);
                isNewgameOrSolutionPressed = true;
                moves = 0;
                pauseChronometer();
                Intent sendtosol = new Intent(getApplicationContext(), solution.class);
                String puzzle = ""; //retrieving the current puzzle
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        puzzle = puzzle + Integer.toString(currentPuzzle[i][j]);
                    }
                }
                System.out.println(puzzle);
                sendtosol.putExtra("key", puzzle);
                startActivity(sendtosol);
                finish();
            }
        });

        Button newGameButton = findViewById(R.id.newgame);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResults();
                newGameButton.startAnimation(myAnim);
                isNewgameOrSolutionPressed = true;
                startActivity(new Intent(getApplicationContext(), setDifficulty.class));
                finish();
            }
        });

        int buttonCounter=0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                b[i][j] = (Button) findViewById(BUTTON_IDS[buttonCounter]);
                buttonCounter++;
            }
        }

        //create puzzle with the desired level
        int[][] getpuzzle = create(level);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                currentPuzzle[i][j] = getpuzzle[i][j];
            }
        } //current puzzle

        Button restart = findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart.startAnimation(myAnim);
                rand(currentPuzzle);
                isPopUpOpen = false;
                play();
            }
        });


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                puzzle = puzzle + Integer.toString(currentPuzzle[i][j]);
            }
        }
        System.out.println(puzzle);

        TextView bestval = findViewById(R.id.bestval);
        String temp = " " + ob.moves.get(puzzle);
        bestval.setText(temp);

        rand(getpuzzle);
        play();
    }

    void play() {
        if (!havewon())//gamestate=false
        {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    final int chi = i, chj = j;
                    b[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isvalidmove(chi, chj)) {
                                play();
                            }
                        }
                    });
                }
            }
        } else {
            setDColor();
            pauseChronometer();
            Toast msg = Toast.makeText(getApplicationContext(), "Hurray!! You WON!!", Toast.LENGTH_SHORT);
            msg.show();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    b[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            } //seize grid;

            Button newGameButton, restart, seesol;
            newGameButton = findViewById(R.id.newgame);
            restart = findViewById(R.id.restart);
            seesol = findViewById(R.id.seesolution);
            newGameButton.setVisibility(View.GONE);
            restart.setVisibility(View.GONE);
            seesol.setVisibility(View.GONE);

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
        int[] iposs = {0, -1, 0, 1};
        int[] jposs = {-1, 0, 1, 0};
        for (int k = 0; k < 4; k++) {
            int X = x + iposs[k];
            int Y = y + jposs[k];

            if (X < 3 && X >= 0 && Y >= 0 && Y < 3) {
                String s = b[X][Y].getText().toString();
                if (s.compareTo(" ") == 0) {
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
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String s;
                s = b[i][j].getText().toString();
                if (s.compareToIgnoreCase(" ") == 0) {
                    b[i][j].setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    b[i][j].setBackgroundResource(R.drawable.box_appearance);
                }
            }
        }
    }

    void setDColor() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                b[i][j].setBackgroundResource(R.drawable.green_box_appearance);
            }
        }
    }

    boolean havewon() {
        int c = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (c == 9) break;
                String s, s1;
                s = b[i][j].getText().toString();
                s1 = Integer.toString(c);
                if (s.compareToIgnoreCase(s1) != 0) {
                    return false;
                }
                c++;
            }
        }

        //after winning perform the tasks below
        return true;
    }

    //implements puzzle to buttons
    void rand(int[][] puzzle) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String s = Integer.toString(puzzle[i][j]);
                if (s.compareToIgnoreCase("9") == 0) s = " ";
                b[i][j].setText(s);
            }
        }
        setColor();
    }

    public static void print(String s) {
        for (int i = 0; i < 9; ++i) {
            if (s.charAt(i) == '9') System.out.print("  ");
            else System.out.print(s.charAt(i) + " ");
            if (i % 3 == 2) System.out.println();
        }
        System.out.println();
    }

    public static int[][] strToArr(String s) {
        int[][] a = new int[3][3];
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)
                a[i][j] = s.charAt(3 * i + j) - 48;
        return a;
    }

    //creates new puzzle from given difficulty level
    int[][] create(int level) {

        int id=0; // puzzle id (initial state)

        Random rd = new Random();
        while (id == 0) {
            id = rd.nextInt(ob.difficulty[level] - ob.difficulty[level - 1]) + ob.difficulty[level - 1];
        }
        String puzzle = ob.pattern[id];

        return strToArr(puzzle);
    }

    void aftersleep(View v) {
        showWinPopupWindow(v, 1);
    }

    ViewGroup rootclearDim;

    public void showWinPopupWindow(final View view, int flag) {
        isPopUpOpen = true;
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
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
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
        String s = moves + " Moves";
        movestext.setText(s);

        Chronometer timetext = popupView.findViewById(R.id.popuptime);
        setCurrentTime(timetext, stopTime);

        final Button resumebutton = popupView.findViewById(R.id.popupresume);
        final Button solbutton = popupView.findViewById(R.id.popupsolution);
        final Button newGameButtonbutton = popupView.findViewById(R.id.popupnewgame);
        if (flag == 0) {
            LinearLayout l1 = popupView.findViewById(R.id.t1);
            LinearLayout l2 = popupView.findViewById(R.id.t2);
            maint.setText("PAUSED");
            solbutton.setVisibility(View.GONE);
            newGameButtonbutton.setVisibility(View.GONE);
            l1.setVisibility(View.GONE);
            l2.setVisibility(View.GONE);
        } else {
            resumebutton.setVisibility(View.GONE);
        }

        resumebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumebutton.startAnimation(myAnim);
                Button newGameButton, restart, seesol;
                newGameButton = findViewById(R.id.newgame);
                restart = findViewById(R.id.restart);
                seesol = findViewById(R.id.seesolution);
                newGameButton.setVisibility(View.VISIBLE);
                restart.setVisibility(View.VISIBLE);
                seesol.setVisibility(View.VISIBLE);
                startChronometer();
                popupWindow.dismiss();
                clearDim(root);
                isPopUpOpen = false;
            }
        });

        final Button seesolution = popupView.findViewById(R.id.popupsolution);
        seesolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResults();
                popupWindow.dismiss();
                moves = 0;
                seesolution.startAnimation(myAnim);
                pauseChronometer();
                Intent sendtosol = new Intent(getApplicationContext(), solution.class);
                String puzzle = "";
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        puzzle = puzzle + Integer.toString(currentPuzzle[i][j]);
                    }
                }
                System.out.println(puzzle);
                sendtosol.putExtra("key", puzzle);
                startActivity(sendtosol);
                finish();
            }
        });
/*
        exitame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResults();
                exitame.startAnimation(myAnim);
                popupWindow.dismiss();
                startActivity(new Intent(getApplicationContext(),MainMenu.class));
                finish();
            }
        });*/

        final Button newGameButton = popupView.findViewById(R.id.popupnewgame);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResults();
                popupWindow.dismiss();
                clearDim(root);
                newGameButton.startAnimation(myAnim);
                //As an example, display the message
                Toast.makeText(view.getContext(), "NEW GAME", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), setDifficulty.class);
                startActivity(i);
                finish();
            }
        });

        //Handler for clicking on the inactive zone of the window
        /*restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopUpOpen=false;
                Button newGameButton,restart,seesol;
                newGameButton = findViewById(R.id.newgame);
                restart = findViewById(R.id.restart);
                seesol= findViewById(R.id.seesolution);
                newGameButton.setVisibility(View.VISIBLE); restart.setVisibility(View.VISIBLE); seesol.setVisibility(View.VISIBLE);
                restartButton.startAnimation(myAnim);
                clearDim(root);
                rand(currentPuzzle);
                popupWindow.dismiss();
                startChronometer();
                play();
            }
        });*/

    }

    public static void applyDim(@NonNull ViewGroup parent, float dimAmount) {
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
        if (!havewon()) {
            if (!isPopUpOpen) {
                pauseChronometer();
                Button newGameButton, restart, seesol;
                newGameButton = findViewById(R.id.newgame);
                restart = findViewById(R.id.restart);
                seesol = findViewById(R.id.seesolution);
                newGameButton.setVisibility(View.GONE);
                restart.setVisibility(View.GONE);
                seesol.setVisibility(View.GONE);
                showWinPopupWindow(v, 0);
            } else {
                Button newGameButton, restart, seesol;
                newGameButton = findViewById(R.id.newgame);
                restart = findViewById(R.id.restart);
                seesol = findViewById(R.id.seesolution);
                newGameButton.setVisibility(View.VISIBLE);
                restart.setVisibility(View.VISIBLE);
                seesol.setVisibility(View.VISIBLE);
                clearDim(rootclearDim);
                popupWindow.dismiss();
                isPopUpOpen = false;
                startChronometer();
            }
        }
    }

    void pauseChronometer() {
        stopTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();
    }

    void startChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
        chronometer.start();
    }

    public void setCurrentTime(Chronometer c, long time) {
        stopTime = time;
        c.setBase(SystemClock.elapsedRealtime() + time);
    }

    @Override
    protected void onPause() {
        pauseChronometer();
        ScrollView v = findViewById(R.id.gamescrollview);
        if (!isPopUpOpen && !isNewgameOrSolutionPressed) {
            Button newGameButton, restart, seesol;
            newGameButton = findViewById(R.id.newgame);
            restart = findViewById(R.id.restart);
            seesol = findViewById(R.id.seesolution);
            newGameButton.setVisibility(View.VISIBLE);
            restart.setVisibility(View.VISIBLE);
            seesol.setVisibility(View.VISIBLE);
            showWinPopupWindow(v, 0);
        }

        super.onPause();
    }

    void updateResults() {
        int win = (havewon())?1:0;
        MainActivity.object.performanceUpdate(MainActivity.object.current_user, moves, ob.moves.get(puzzle), 0, level, win, getApplicationContext());
    }
}
