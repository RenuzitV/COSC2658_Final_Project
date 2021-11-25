import java.io.File;
import java.util.Scanner;

public class Brute {
    private static char[][] maze = null;
    private static int n, m, maxGold = Integer.MIN_VALUE;
    private static int currentCoin = 0;
    private static String str = "";
    public static void read(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (Exception e) {
            System.out.println(e);
        }
        assert scanner != null;
        n = scanner.nextInt();
        m = scanner.nextInt();
        maze = new char[n][m];
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < m; j++) {
                maze[i][j] = line.charAt(j);
            }
        }

    }
    public static boolean dfs(int i, int j, int goldCollected) {
        if(i<0 || i>=n || j<0 || j>=m || maze[i][j] == 'X') {
            return false;
        }
        int currentCoin;
        if(maze[i][j] == '.')
            currentCoin = 0;
        else
            currentCoin = maze[i][j] - '0';
        maze[i][j] = 'X';
        maxGold = Math.max(maxGold, goldCollected + currentCoin);
        dfs(i+1,j,goldCollected + currentCoin);
        dfs(i,j+1,goldCollected + currentCoin);
        maze[i][j] = (char) (currentCoin + '0');
        return false;
    }

    public static int solve() {
        for( int i = 0; i < n; i++) {
            for( int j = 0; j < m; j++) {
                if( maze[i][j] != 'X') {
                    dfs(i,j,0);
                }
            }
        }
        return maxGold;

    }


    public static void main(String[] args) {
        if (args.length != 1) return;
        read(args[0]);
        System.out.println(solve());
        System.out.println(str);
    }
}