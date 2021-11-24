import javafx.util.Pair;

import java.io.File;
import java.util.Scanner;

public class Main {
    private static final char[][] maze = new char[25][25];
    private static final Pair<Integer, Integer>[][] dp = new Pair[25][25];
    private static final Pair<Integer, Integer>[][] parent = new Pair[25][25];
    private static int n, m;
    public static void read(String filename){
        File file = new File(filename);
        if (!file.exists()){
            return;
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (Exception e){
            System.out.println(e);
        }
        assert scanner != null;
        n = scanner.nextInt();
        m = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= n; ++i){
            maze[i] = ("X" + scanner.nextLine() + "X").toCharArray();
        }
        for (int i = 0; i <= n+1; ++i)
            dp[i][0] = new Pair<>(-1, 0);
        for (int i = 0; i <= m+1; ++i)
            dp[0][i] = new Pair<>(-1, 0);


        for (int i = 1; i <= n; ++i){
            for (int j = 1; j <= m; ++j) {
                dp[i][j] = new Pair<>(-1, 0);
//                System.out.printf("%c", maze[i][j]);
                if (maze[i][j] == '.') maze[i][j] = '0';
            }
//            System.out.println();
        }
        dp[1][1] = new Pair<>(0, 0);
    }

    private static boolean greater(Pair<Integer, Integer> a, Pair<Integer, Integer> b){
        return a.getKey() > b.getKey() || (a.getKey().equals(b.getKey()) && a.getValue() > b.getValue());
    }

    private static Pair<Integer, Integer> add(Pair<Integer, Integer> a, Pair<Integer, Integer> b){
        return new Pair<>(a.getKey()+b.getKey(), a.getValue() + b.getValue());
    }

    public static String solve(){
        Pair<Integer, Integer> v = new Pair<>(1, 1);
        Pair<Integer, Integer> res = new Pair<>(0, 0);
        for (int i = 1; i <= n; ++i){
            for (int j = 1; j <= m; ++j){
                if (maze[i][j] != 'X'){
                    Pair<Integer, Integer> temp = add(dp[i - 1][j], new Pair<>(maze[i][j] - '0', -1));
                    if (maze[i-1][j] != 'X' && greater(temp, dp[i][j])) {
                        dp[i][j] = temp;
                        parent[i][j] = new Pair<>(i - 1, j);
                    }
                    temp = add(dp[i][j-1], new Pair<>(maze[i][j] - '0', -1));
                    if (maze[i][j-1] != 'X' && greater(temp, dp[i][j])){
                        dp[i][j] = temp;
                        parent[i][j] = new Pair<>(i, j - 1);
                    }
                    if (greater(dp[i][j], res)){
                        res = dp[i][j];
                        v = new Pair<>(i, j);
                    }
                }
            }
        }
        StringBuilder ans = new StringBuilder(res.getKey() + " ");
        StringBuilder temp = new StringBuilder();
        while (v.getValue() > 1 || v.getKey() > 1){
            Pair<Integer, Integer> u = parent[v.getKey()][v.getValue()];
            if (u.getValue() < v.getValue()){
                temp.append("R");
            }
            else temp.append("D");
            v = u;
        }
        return ans.toString() + temp.reverse();
    }

    public static void main(String[] args) {
        if (args.length != 1) return;
        read(args[0]);
        System.out.println(solve());
    }
}
