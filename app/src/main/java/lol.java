import java.io.File;
import java.util.*;

public class lol {
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
    public static void main(String args[]) throws Exception {
        int i = 0, j, size = 181440, move, id;
        int[] difficulty = new int[] {7279, 108809, 65352};
        Map<String, String> map = new LinkedHashMap<>();
        Map<String, Integer> moves = new LinkedHashMap<>();
        String[] pattern = new String[size];
        Random rd = new Random();
        String current, previous;
        File f = new File("data.txt");
        Scanner sc = new Scanner(f);
        while(sc.hasNext()) {
            current = sc.next();
            previous = sc.next();
            move = sc.nextInt();
            map.put(current, previous);
            moves.put(current, move);
            pattern[i++] = current;
        }
        // difficulty[0] Easy, difficulty[1] Medium, difficulty[2] Hard
        id = rd.nextInt(difficulty[1]);
        current = pattern[id];
        while(!current.equals(map.get(current))) {
            print(current);
            current = map.get(current);
        }
        print(current);
        System.out.println("Minmum Moves: " + moves.get(pattern[id]));
    }
}