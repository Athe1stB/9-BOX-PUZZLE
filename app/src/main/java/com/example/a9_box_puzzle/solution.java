package com.example.a9_box_puzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Random;

public class solution extends AppCompatActivity {

    String recievedpuzzle;
    int numberOfMoves=0;
    LinkedHashMap<String, String> map = new LinkedHashMap<>(); //present,next state
    LinkedHashMap<String, Integer> moves = new LinkedHashMap<>(); //number of moves to solve
    Button sb[][] = new Button[3][3];
    private static final int[] BUTTON_IDS = {R.id.sb1, R.id.sb2, R.id.sb3, R.id.sb4, R.id.sb5, R.id.sb6, R.id.sb7, R.id.sb8, R.id.sb9,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        recievedpuzzle = getIntent().getExtras().getString("key");

        int idc=0;
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++)
            {
                sb[i][j]=(Button) findViewById(BUTTON_IDS[idc]); idc++;
            }
        }

        System.out.println(recievedpuzzle);

        int puxx[][] = strToArr(recievedpuzzle);
        rand(puxx);
      initialize(getApplicationContext());
      check();
    }

    void initialize(Context context) {
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
                    moves.put(input.substring(0, 9), Integer.parseInt(input.substring(20)));
                    pattern[i++] = input.substring(0, 9);
                }
            }

        }
        catch (IOException e)
        {
            Log.v("unhandled","catch");
        }
    }

    void check(){
        String puzzle = recievedpuzzle; // puzzle corresponding to id
        recursion(puzzle);
        numberOfMoves=-1;

//        while (!puzzle.equals(map.get(puzzle))) //checking whether final state is reached or not
//            {
//                //print(puzzle);
//                int p[][]= strToArr(puzzle);
//                rand(p);
//                puzzle = map.get(puzzle);
//
//            }

            //print(puzzle);

    }

    void recursion(final String puzzle) {
        if(!puzzle.equals((map.get(puzzle))))
        {
            System.out.println("not equal");
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    int p[][]= strToArr(puzzle);
                    rand(p);
                    numberOfMoves++;
                    TextView tt = findViewById(R.id.solutionmoveval);
                    String movescount = Integer.toString(numberOfMoves);
                    tt.setText(movescount);
                    recursion(map.get(puzzle));
                }
            }, 700);
        }
        else //final state(123456789)
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    int p[][]= strToArr(puzzle);
                    rand(p);
                    setDColor();
                    numberOfMoves++;
                    TextView tt = findViewById(R.id.solutionmoveval);
                    String movescount = Integer.toString(numberOfMoves);
                    tt.setText(movescount);
                    System.out.println("last");
                }
            }, 700);
        }

    }

    public static int[][] strToArr(String s) {
        int[][] a = new int[3][3];
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                a[i][j] = s.charAt(3*i+j)-48;
        return a;
    }

    void rand(int[][] puzzle) {

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                String s = Integer.toString(puzzle[i][j]);
                if(s.compareToIgnoreCase("9")==0)s=" ";
                sb[i][j].setText(s);
            }
        }
        setColor();
    } //implements puzzle to buttons

    void setColor() {
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                String s; s = sb[i][j].getText().toString();
                if(s.compareToIgnoreCase(" ")==0)
                {
                    sb[i][j].setBackgroundColor(getResources().getColor(R.color.white));
                }
                else
                {
                    sb[i][j].setBackgroundResource(R.drawable.box_apperance);
                }
            }
        }
    }

    void setDColor() {
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                sb[i][j].setBackgroundColor(getResources().getColor(R.color.green));
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
