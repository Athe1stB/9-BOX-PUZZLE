package com.lbcc.a9_box_puzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class performanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic);
        func();

    }
    //                                       0     1    2
    //	player:  name ->				   easy medium hard
    //			0	   	total_games_played	 0     0    0
    //			1	   	total_time			 0     0    0
    //			2	   	total_wins			 0     0    0
    //			3	   	total_moves			 0     0    0
    //			4	   	total_score			 0	   0    0
    //String name,int EasyWins,int EasyMoves,int EasyScore,int EasyGamesPlayed,int MediumWins,int MediumMoves,int MediumScore,int MediumGamesPlayed,int HardWins,int HardMoves,int HardScore, int HardGamesPlayed)
    //
    void func() {
        ArrayList<performanceDetails> temp = new ArrayList<>();
        for (String key : MainActivity.object.player.keySet()) {
            int EasyWins, EasyMoves, EasyScore, EasyGamesPlayed, MediumWins, MediumMoves, MediumScore, MediumGamesPlayed, HardWins, HardMoves, HardScore,  HardGamesPlayed;

            EasyWins= MainActivity.object.player.get(key)[2][0];
            EasyGamesPlayed= MainActivity.object.player.get(key)[0][0];
            EasyMoves= MainActivity.object.player.get(key)[3][0];
            EasyScore= MainActivity.object.player.get(key)[4][0];


            MediumWins= MainActivity.object.player.get(key)[2][1];
            MediumGamesPlayed= MainActivity.object.player.get(key)[0][1];
            MediumMoves= MainActivity.object.player.get(key)[3][1];
            MediumScore= MainActivity.object.player.get(key)[4][1];


            HardWins= MainActivity.object.player.get(key)[2][2];
            HardGamesPlayed= MainActivity.object.player.get(key)[0][2];
            HardMoves= MainActivity.object.player.get(key)[3][2];
            HardScore= MainActivity.object.player.get(key)[4][2];

            temp.add(new performanceDetails(key.toLowerCase(),EasyWins, EasyMoves, EasyScore, EasyGamesPlayed, MediumWins, MediumMoves, MediumScore, MediumGamesPlayed, HardWins, HardMoves, HardScore,  HardGamesPlayed));
        }

        ListView listview = (ListView) findViewById(R.id.list);
        final performanceAdapter cAdapter = new performanceAdapter(this,temp);
        listview.setAdapter(cAdapter);
    }
}
