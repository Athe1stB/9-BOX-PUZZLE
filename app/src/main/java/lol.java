import android.content.Context;

import java.io.*;
import java.util.*;

class lol {
    void func(Context context) throws Exception {
        // Use below map for player statistics
        // Run below code only once, every time app is opened
        // mapping is as follows:
        //
        //	player name -> totalGamesPlayed, totalWins, totalMoves, easy, medium, hard
        //
        //	player name ->					   easy medium hard
        //				   total_games_played	 0     0    0
        //				   total_time			 0     0    0
        //				   total_wins			 0     0    0
        //				   total_moves			 0     0    0
        //				   total_score			 0	   0    0

        // File pf = new File("performance.txt");
        File pf = new File(context.getFilesDir()+File.separator+"performance.txt");
        if(!pf.exists()) pf.createNewFile();
        BufferedReader br = new BufferedReader(new FileReader(pf));
        LinkedHashMap<String, int[][]> player = new LinkedHashMap<String, int[][]>();
        String input1, input2[];
        int i, j, r = 5, c = 3, a[][] = new int[r][c], n;
        while((input1 = br.readLine()) != null) {
            for(i = 0; i < r; ++i) {
                input2 = br.readLine().split(",");
                for(j = 0; j < c; ++j)
                    a[i][j] = Integer.parseInt(input2[j]);
            }
            player.put(input1, a);
        }
        br.close();

        // example data for current game
        int current_move = 69;
        int current_min_move = 30;
        int current_time = 69;
        int current_hard = 3;
        int current_victory_status = 1; // 0 for lose, 1 for win
        String current_name = "Athe1stB";

        // after a level is completed run this

        if(!player.containsKey(current_name)) {
            for(i = 0; i < r; ++i) for(j = 0; j < c; ++j) a[i][j] = 0;
            player.put(current_name, a);
        }
        player.get(current_name)[0][current_hard-1]++;
        player.get(current_name)[1][current_hard-1] += current_time;
        if(current_victory_status == 1) {
            player.get(current_name)[2][current_hard-1]++;
            player.get(current_name)[3][current_hard-1] += current_move;
            player.get(current_name)[4][current_hard-1] += (int)(100.0*current_min_move/current_move);
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(pf, false));
        n = player.size();
        for(String name: player.keySet()) {
            bw.write(name + "\n");
            for(i = 0; i < r; ++i) {
                for(j = 0; j < c-1; ++j)
                    bw.write(a[i][j] + ",");
                bw.write(a[i][c-1] + "\n");
            }
        }
        bw.close();
    }
}

/*
8 6 7
2 5 4
3   1

6 4 7
8 5
3 2 1
*/