//import android.content.res.AssetManager;
//
//import java.io.*;
//import java.util.*;
//
//class lol {
//    public static void print(String s) {
//        for(int i = 0; i < 9; ++i) {
//            if(s.charAt(i) == '9') System.out.print("  ");
//            else System.out.print(s.charAt(i) + " ");
//            if(i%3==2) System.out.println();
//        }
//        System.out.println();
//    }
//    public static int[][] strToArr(String s) {
//        int[][] a = new int[3][3];
//        for(int i = 0; i < 3; ++i)
//            for(int j = 0; j < 3; ++j)
//                a[i][j] = s.charAt(3*i+j)-48;
//        return a;
//    }
//    public static void main(String args[]) throws Exception {
//        int i = 0, j, size = 181440, move, id, hard;
//        int[] difficulty = new int[] {0, 7279, 116088, 181440};
//        LinkedHashMap<String, String> map = new LinkedHashMap<>();
//        LinkedHashMap<String, Integer> moves = new LinkedHashMap<>();
//        String[] pattern = new String[size];
//        Random rd = new Random();
//        String input, puzzle, filename;
//        AssetManager am = context.getAssets();
//        InputStream is;
//        BufferedReader br;
//        for(j = 1; j < 6; ++j) {
//            filename = "data" + j + ".txt";
//            is = am.open(filename);
//            br = new BufferedReader(new InputStreamReader(is));
//            while((input = br.readLine()) != null) {
//                map.put(input.substring(0, 9), input.substring(10, 19));
//                moves.put(input.substring(0, 9), Integer.parseInt(input.substring(20)));
//                pattern[i++] = input.substring(0, 9);
//            }
//        }
//
//        // hard = 1 Easy, 2 Medium, 3 Hard
//
//        hard = 3;
//        id = rd.nextInt(difficulty[hard] - difficulty[hard-1]) + difficulty[hard-1];
//        puzzle = pattern[id];
//        while(!puzzle.equals(map.get(puzzle))) {
//            print(puzzle);
//            puzzle = map.get(puzzle);
//        }
//        print(puzzle);
//        System.out.println("Minimum Moves: " + moves.get(pattern[id]));
//    }
//}
//
///*
//8 6 7
//2 5 4
//3   1
//
//6 4 7
//8 5
//3 2 1
// */
//int arr[] = new int[8];
//        int k = 0, flag = 0;
//
//        for (int i = 0; i < 3; i++) {
//        for (int j = 0; j < 3; j++) {
//        flag = 0;
//        int x = (int) (Math.random() * 8);
//        x = x + 1;
//        if (k == 0) {
//        String str = new String(" ");
//        str = str + x;
//        b[i][j].setText(str);
//        ar[0][0] = x;
//        arr[k] = x;
//        k++;
//        } else if (k <= 7) {
//
//        while (flag == 0) {
//        int chk = 0;
//        for (int a = 0; a < k; a++) {
//        if (arr[a] != x) {
//        chk++;
//        } else a = 10;
//        }
//        if (k == chk) {
//        String str = new String(" ");
//        str = str + x;
//        b[i][j].setText(str);
//        arr[k] = x;
//        ar[i][j] = x;
//        k++;
//        flag = 1;
//        } else {
//        x = (int) (Math.random() * 8);
//        x = x + 1;
//        }
//        }
//        }
//        ar[2][2] = 9;
//        }
//        }
//        setColor();
//        }
