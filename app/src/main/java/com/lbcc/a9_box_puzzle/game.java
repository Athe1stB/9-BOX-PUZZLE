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

    Boolean isPopUpOpen; //check state of popup(open or close)
    Boolean isNewgameOrSolutionPressed; // to check whether newGameButton/solution pressed or not
    Button[][] b = new Button[3][3];//these buttons make up the screen
    int moves;
    int level; //easy,medium,hard (1,2,3 respectively)
    Files ob = MainActivity.object;
    String puzzle = "";
    int[][] currentPuzzle = new int[3][3]; //current puzzle
    PopupWindow popupWindow;
    //3 buttons in game interface
    Button gameNewGameButton, gameSolutionButton, gameResetButton;

    game() {
        isPopUpOpen = false;
        isNewgameOrSolutionPressed = false;
        moves = 0;
        level = 0;
        puzzle = "";
        gameNewGameButton = findViewById(R.id.newgame);
        gameSolutionButton = findViewById(R.id.seesolution);
        gameResetButton = findViewById(R.id.restart);
    }

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

        gameSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResults(); //looking solution means either you gave up or you have won and looking for the best solution. which updates results terminating your ranked gameplay.
                gameSolutionButton.startAnimation(myAnim);
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

        gameNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResults();
                gameNewGameButton.startAnimation(myAnim);
                isNewgameOrSolutionPressed = true;
                startActivity(new Intent(getApplicationContext(), setDifficulty.class));
                finish();
            }
        });

        gameResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameResetButton.startAnimation(myAnim);
                rand(currentPuzzle);
                isPopUpOpen = false;
                play();
            }
        });

        int buttonCounter = 0;
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

            //render all the buttons useless after winning
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    b[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }

            gameNewGameButton.setVisibility(View.GONE);
            gameResetButton.setVisibility(View.GONE);
            gameSolutionButton.setVisibility(View.GONE);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
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

    public static int[][] strToArr(String s) {
        int[][] a = new int[3][3];
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)
                a[i][j] = s.charAt(3 * i + j) - 48;
        return a;
    }

    //creates new puzzle from given difficulty level
    int[][] create(Context context, int level) {
        int moves, id;
        Random rd = new Random();

        // number of moves for the next puzzle
        if(level == 1)
            moves = rd.nextInt(4) + 6;        // 6 to 9 moves
        else if(level == 2)
            moves = rd.nextInt(10) + 10;      // 10 to 19 moves
        else if(level == 3)
            moves = rd.nextInt(12) + 20;      // 20 to 31 moves

        // random id with that number of moves
        id = rd.nextInt(ob.difficulty[moves] - ob.difficulty[moves - 1]) + ob.difficulty[moves - 1];

        String puzzle = ob.pattern[id];

        return strToArr(puzzle);
    }

    void aftersleep(View v) {
        showWinPopupWindow(v, 1);
    }

    ViewGroup rootclearDim;

    //flag =0 means popUpWindow to be shown when we paused the game.
    //flag =1 means popUpWindow to be shown when we have won.

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

        final Button puResumeButton = popupView.findViewById(R.id.popupresume);
        final Button puSolutionButton = popupView.findViewById(R.id.popupsolution);
        final Button puNewGameButton = popupView.findViewById(R.id.popupnewgame);

        if (flag == 0) {
            LinearLayout l1 = popupView.findViewById(R.id.t1);
            LinearLayout l2 = popupView.findViewById(R.id.t2);
            maint.setText("PAUSED");
            puSolutionButton.setVisibility(View.GONE);
            puNewGameButton.setVisibility(View.GONE);
            l1.setVisibility(View.GONE);
            l2.setVisibility(View.GONE);
        } else {
            puResumeButton.setVisibility(View.GONE);
        }

        puResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                puResumeButton.startAnimation(myAnim);
                // after clicking resume again bring back all the buttons of the game to visible , start chronometer, close pop up menu
                gameNewGameButton.setVisibility(View.VISIBLE);
                gameResetButton.setVisibility(View.VISIBLE);
                gameSolutionButton.setVisibility(View.VISIBLE);
                startChronometer();
                popupWindow.dismiss();
                clearDim(root);
                isPopUpOpen = false;
            }
        });

        puSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if solution button of popUpMenu clicked. update results. dismiss popUp window, reinitialise all params go to solution window
                updateResults();
                popupWindow.dismiss();
                moves = 0;
                puSolutionButton.startAnimation(myAnim);
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

        puNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResults();
                popupWindow.dismiss();
                clearDim(root);
                puNewGameButton.startAnimation(myAnim);
                //As an example, display the message
                Toast.makeText(view.getContext(), "NEW GAME", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), setDifficulty.class);
                startActivity(i);
                finish();
            }
        });
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
                gameNewGameButton.setVisibility(View.GONE);
                gameResetButton.setVisibility(View.GONE);
                gameSolutionButton.setVisibility(View.GONE);
                showWinPopupWindow(v, 0);
            } else {
                gameSolutionButton.setVisibility(View.VISIBLE);
                gameResetButton.setVisibility(View.VISIBLE);
                gameNewGameButton.setVisibility(View.VISIBLE);
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
            gameNewGameButton.setVisibility(View.VISIBLE);
            gameResetButton.setVisibility(View.VISIBLE);
            gameSolutionButton.setVisibility(View.VISIBLE);
            showWinPopupWindow(v, 0);
        }

        super.onPause();
    }

    void updateResults() {
        int win = (havewon()) ? 1 : 0;
        MainActivity.object.performanceUpdate(MainActivity.object.current_user, moves, ob.moves.get(puzzle), 0, level, win, getApplicationContext());
    }
}
