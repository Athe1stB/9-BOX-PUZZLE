import android.content.Context;
import android.content.res.AssetManager;

import java.util.*;
import java.io.*;

public class Files {

    //  map: 	 current_state -> next_state
    //
    //  moves: 	 current_state -> minimum possible moves to solve
    //
    //  pattern: all states
    //
    //	player:  name ->				   easy medium hard
    //				   	total_games_played	 0     0    0
    //				   	total_time			 0     0    0
    //				   	total_wins			 0     0    0
    //				   	total_moves			 0     0    0
    //				   	total_score			 0	   0    0

    String current_user;
    int difficulty[];
    LinkedHashMap<String, String> map;
    LinkedHashMap<String, Integer> moves;
    String[] pattern;
    LinkedHashMap<String, int[][]> player;
    public Files() {
        current_user = "";
        difficulty = new int[] {0, 706, 54802, 181440};
        map = new LinkedHashMap<String, String>();
        moves = new LinkedHashMap<String, Integer>();
        pattern = new String[181440];
        player = new LinkedHashMap<String, int[][]>();
    }
    public void performanceInit(Context context) throws Exception {

        // Run this just once at the beginning of the game

        // File pf = new File("performance.txt");
        File pf = new File(context.getFilesDir()+File.separator+"performance.txt");
        if(!pf.exists()) pf.createNewFile();
        BufferedReader br = new BufferedReader(new FileReader(pf));
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
    }
    public void performanceUpdate(String name, int move, int min_move, int time, int hard, int win, Context context) throws Exception {

		/*
		Run this code when:
			a game is completed
			solution is viewed
		*/

        int i, j, r = 5, c = 3, a[][] = new int[r][c];
        // File pf = new File("performance.txt");
        File pf = new File(context.getFilesDir()+File.separator+"performance.txt");
        if(!player.containsKey(name)) {
            for(i = 0; i < r; ++i) for(j = 0; j < c; ++j) a[i][j] = 0;
            player.put(name, a);
        }
        player.get(name)[0][hard-1] += 1;
        player.get(name)[1][hard-1] += time;
        player.get(name)[2][hard-1] += win;
        if(win == 1) {
            player.get(name)[3][hard-1] += move;
            player.get(name)[4][hard-1] += (int)(100.0*min_move/move);
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(pf, false));
        for(String key: player.keySet()) {
            bw.write(key + "\n");
            for(i = 0; i < r; ++i) {
                for(j = 0; j < c-1; ++j)
                    bw.write(player.get(key)[i][j] + ",");
                bw.write(player.get(key)[i][c-1] + "\n");
            }
        }
        bw.close();
    }
    public void dataInit(Context context) throws Exception {

        // Run this code just once at the beginning of game

        AssetManager am = context.getAssets();
        String input, filename;
        BufferedReader br;
        InputStream is;
        int i, j = 0;
        for(i = 1; i < 6; ++i) {
            filename = "data" + i + ".txt";
            is = am.open(filename);
            // is = new FileInputStream(new File(filename));
            br = new BufferedReader(new InputStreamReader(is));
            while((input = br.readLine()) != null) {
                map.put(input.substring(0, 9), input.substring(10, 19));
                moves.put(input.substring(0, 9), Integer.parseInt(input.substring(20)));
                pattern[j++] = input.substring(0, 9);
            }
        }
    }
    public void userInit(Context context) throws Exception {

		/*
		Run this code at the start of game
		if object.current_user == null, prompt for username
		else use current_user as the default user
		 */

        // File uf = new File("user.txt");
        File uf = new File(context.getFilesDir()+File.separator+"user.txt");
        if(!uf.exists()) uf.createNewFile();
        BufferedReader br = new BufferedReader(new FileReader(uf));
        current_user = br.readLine();
        br.close();
    }
    public void userUpdate(String name, Context context) throws Exception {

		/*
		Run this code:
			everytime new user is added
			user is changed
		 */

        current_user = name;
        // File uf = new File("user.txt");
        File uf = new File(context.getFilesDir()+File.separator+"user.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(uf, false));
        bw.write(name);
        bw.close();
    }
}