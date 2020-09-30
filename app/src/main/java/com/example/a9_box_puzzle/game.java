package com.example.a9_box_puzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class game extends AppCompatActivity {

    Button b[][] = new Button[3][3];
    int ar[][] = new int[3][3];
    int moves=0;
    private static final int[] BUTTON_IDS = {R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6, R.id.b7, R.id.b8, R.id.b9,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        int idc=0;
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                b[i][j]=(Button) findViewById(BUTTON_IDS[idc]); idc++;
            }
        }
        rand();
        play();


    }

    void play() {
        if(!havewon() && !partialwon())//gamestate=false
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
            if(havewon()) {
                Toast msg = Toast.makeText(getApplicationContext(), "Hurray!! You WON!!", Toast.LENGTH_SHORT);
                msg.show();
            }
            else
            {
                Toast msg = Toast.makeText(getApplicationContext(), "Hurray!! You WON Partially!!", Toast.LENGTH_SHORT);
                msg.show();
            }

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
                s1=Integer.toString(c); s1 = " "+s1;
                if(s.compareToIgnoreCase(s1)!=0)
                {
                    return false;
                }
                c++;
            }
        }
        Log.v("winning status", "won !!!!!!!!!!!!!!!!!!!!!!!!!");
        return true;
    }

    boolean partialwon() {
        int c=1;
        for(int i=0; i<2; i++)
        {
            for(int j=0; j<3; j++)
            {
                if(c==9)break;
                String s,s1; s= b[i][j].getText().toString();
                s1=Integer.toString(c); s1 = " "+s1;
                if(s.compareToIgnoreCase(s1)!=0)
                {
                    return false;
                }
                c++;
            }
        }
        Log.v("winning status", "won !!!!!!!!!!!!!!!!!!!!!!!!!");
        return true;
    }

    void rand() {
        int arr[] = new int[8];
        int k = 0, flag = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                flag = 0;
                int x = (int) (Math.random() * 8);
                x = x + 1;
                if (k == 0) {
                    String str = new String(" ");
                    str = str + x;
                    b[i][j].setText(str);
                    ar[0][0] = x;
                    arr[k] = x;
                    k++;
                } else if (k <= 7) {

                    while (flag == 0) {
                        int chk = 0;
                        for (int a = 0; a < k; a++) {
                            if (arr[a] != x) {
                                chk++;
                            } else a = 10;
                        }
                        if (k == chk) {
                            String str = new String(" ");
                            str = str + x;
                            b[i][j].setText(str);
                            arr[k] = x;
                            ar[i][j] = x;
                            k++;
                            flag = 1;
                        } else {
                            x = (int) (Math.random() * 8);
                            x = x + 1;
                        }
                    }
                }
                ar[2][2] = 9;
            }
        }
        setColor();
    }



}
