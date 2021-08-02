package com.lbcc.a9_box_puzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

public class solution extends AppCompatActivity {

    String recievedpuzzle;
    Files ob = MainActivity.object;
    int numberOfMoves=0;
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
      check();
    }

    void check(){
        String puzzle = recievedpuzzle; // puzzle corresponding to id
        recursion(puzzle);
        numberOfMoves=-1;

    }

    void recursion(final String puzzle) {
        if(!puzzle.equals((ob.map.get(puzzle))))
        {
            System.out.println("not equal");
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int p[][]= strToArr(puzzle);
                    rand(p);
                    numberOfMoves++;
                    TextView tt = findViewById(R.id.solutionmoveval);
                    String movescount = Integer.toString(numberOfMoves);
                    tt.setText(movescount);
                    recursion(ob.map.get(puzzle));
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
                    sb[i][j].setBackgroundResource(R.drawable.box_appearance);
                }
            }
        }
    }

    void setDColor() {
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                sb[i][j].setBackgroundResource(R.drawable.green_box_appearance);
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainMenu.class));
    }
}
