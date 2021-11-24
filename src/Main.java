import java.io.File;
import java.util.Scanner;

public class Main {
    private static final char[][] maze = new char[25][25];
    private static final int[][] dp = new int[25][25];
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
        for (int i = 0; i <= n+1; ++i)
            for (int j = 0; j <= m+1; ++j)
                maze[i][j] = '0';
        for (int i = 1; i <= n; ++i){
            maze[i] = ("X" + scanner.nextLine() + "X").toCharArray();
        }
        for (int i = 1; i <= n; ++i){
            for (int j = 1; j <= m; ++j) {
                System.out.printf("%c", maze[i][j]);
                if (maze[i][j] == '.') maze[i][j] = '0';
            }
            System.out.println();
        }
    }

    public static int solve(){
        for (int i = 1; i <= n; ++i){
            for (int j = 1; j <= m; ++j){
                if (maze[i][j] != 'X'){
                    dp[i][j] = maze[i][j] - '0' + Math.max(dp[i - 1][j], dp[i][j-1]);
                }
            }
        }
        int res = 0;
        for (int i = 1; i <= m; ++i)
            res = Math.max(res, dp[n][i]);
        for (int j = 1; j <= m; ++j)
            res = Math.max(res, dp[j][m]);
        return res;
    }

    public static void main(String[] args) {
        if (args.length != 1) return;
        read(args[0]);
        System.out.println(solve());
    }
}
