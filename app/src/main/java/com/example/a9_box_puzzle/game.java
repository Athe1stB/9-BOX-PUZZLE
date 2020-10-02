package com.example.a9_box_puzzle;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

public class game extends AppCompatActivity {

    Button b[][] = new Button[3][3];
    int ar[][] = new int[3][3];
    int moves=0;
    int currentpuzzle[][] = new int[3][3]; //current puzzle
    LinkedHashMap<String, String> map = new LinkedHashMap<>(); //present,next state
    LinkedHashMap<String, Integer> movesmap = new LinkedHashMap<>(); //number of moves to solve


    private static final int[] BUTTON_IDS = {R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6, R.id.b7, R.id.b8, R.id.b9,};
    Chronometer chronometer;
    long stopTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        stopTime=0;
        chronometer = (Chronometer)findViewById(R.id.timer);
        chronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
        chronometer.start();

        Bundle extras = getIntent().getExtras();
        int level = extras.getInt("key");

        int idc=0;

        Button sol = findViewById(R.id.seesolution);
        sol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();stopTime=0; moves=0;
                findViewById(R.id.moveval).setVisibility(View.GONE);
                chronometer.setVisibility(View.GONE);
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
            stopTime = chronometer.getBase() - SystemClock.elapsedRealtime();
            chronometer.stop();
            Toast msg = Toast.makeText(getApplicationContext(), "Hurray!! You WON!!", Toast.LENGTH_SHORT);
            msg.show();
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

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                ScrollView v = findViewById(R.id.gamescrollview);
                aftersleep(v);
            }
        }, 5000);

        Log.v("winning status", "won !!!!!!!!!!!!!!!!!!!!!!!!!");
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

        int temp[][] = new int[3][3];
        try {
            int i = 0, j, size = 181440, id, hard;
            int[] difficulty = new int[]{0, 7279, 116088, 181440}; //checks difficulty
            String[] pattern = new String[size];
            Random rd = new Random();
            String input, puzzle, filename;
            AssetManager am = context.getAssets();
            InputStream is;
            BufferedReader br;

            for (j = 1; j < 6; ++j) {
                filename = "data" + j + ".txt";
                is = am.open(filename);
                br = new BufferedReader(new InputStreamReader(is));
                while ((input = br.readLine()) != null) {
                    map.put(input.substring(0, 9), input.substring(10, 19));
                    movesmap.put(input.substring(0, 9), Integer.parseInt(input.substring(20)));
                    pattern[i++] = input.substring(0, 9);
                }
            }

            // hard = 1 Easy, 2 Medium, 3 Hard

            hard = level;
            id = rd.nextInt(difficulty[hard] - difficulty[hard - 1]) + difficulty[hard - 1];
            //id = puzzle identity (initial state)
            puzzle = pattern[id];

            int arr[][] = strToArr(puzzle);

            return arr;
        }
        catch (IOException e)
        {
            Log.v("catch","catch");
        }
        return temp;
    } //generate puzzle

    void aftersleep(View v) {
       showPopupWindow(v);
   }

    public void showPopupWindow(final View view ) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = false;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.setOutsideTouchable(false);
        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextView test2 = popupView.findViewById(R.id.popupmoves);
        String s = moves+" Moves";
        test2.setText(s);

        long temptime = SystemClock.elapsedRealtime() - chronometer.getBase();
        TextView time = popupView.findViewById(R.id.popuptime);
        String s1 = Long.toString(temptime)+" Sec";
        time.setText(s1);

        Button newgame = popupView.findViewById(R.id.popupnewgame);
        newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //As an example, display the message
                Toast.makeText(view.getContext(), "NEW GAME", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),setDifficulty.class);
                startActivity(i);
            }
        });

        //Handler for clicking on the inactive zone of the window
        Button restartButton = popupView.findViewById(R.id.popuprestart);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rand(currentpuzzle);
                popupWindow.dismiss();
                moves=0; stopTime=0;
                chronometer = (Chronometer)findViewById(R.id.timer);
                chronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
                chronometer.start();
                TextView tttt = findViewById(R.id.moveval);
                tttt.setText("0");
                play();
            }
        });

    }

    

//    void check(Context context){
//        try {
//            int i = 0, j, size = 181440, id, hard;
//            int[] difficulty = new int[]{0, 7279, 116088, 181440}; //checks difficulty
//            LinkedHashMap<String, String> map = new LinkedHashMap<>(); //present,next state
//            LinkedHashMap<String, Integer> moves = new LinkedHashMap<>(); //number of moves to solve
//            String[] pattern = new String[size];
//            Random rd = new Random();
//            String input, puzzle, filename;
//            AssetManager am = context.getAssets();
//            InputStream is;
//            BufferedReader br;
//
//            for (j = 1; j < 6; ++j) {
//                filename = "data" + j + ".txt";
//                is = am.open(filename);
//                br = new BufferedReader(new InputStreamReader(is));
//                while ((input = br.readLine()) != null) {
//                    map.put(input.substring(0, 9), input.substring(10, 19));
//                    moves.put(input.substring(0, 9), Integer.parseInt(input.substring(20)));
//                    pattern[i++] = input.substring(0, 9);
//                }
//            }
//
//            // hard = 1 Easy, 2 Medium, 3 Hard
//
//            hard = 3;
//            id = rd.nextInt(difficulty[hard] - difficulty[hard - 1]) + difficulty[hard - 1];
//            //id = puzzle identity (initial state)
//            puzzle = pattern[id]; // puzzle corresponding to id
//
//            while (!puzzle.equals(map.get(puzzle))) //checking whether final state is reached or not
//            {
//                print(puzzle);
//                puzzle = map.get(puzzle);
//            }
//            print(puzzle);
//            System.out.println("Minimum Moves: " + moves.get(pattern[id]));
//        }
//        catch(IOException e)
//        {
//            Log.v("catch","catch");
//        }
//    }

}
