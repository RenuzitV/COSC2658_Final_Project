import java.io.File;
import java.util.Scanner;

public class Brute {
    private static char[][] maze = null;
    private static int n, m, maxGold = Integer.MIN_VALUE;
    private static String finalPath = "";
    private static int[][] cor = new int[50][50];

    // read maze from file
    public static void read(String filename) {
        File file = new File(filename); // attempt to open file
        if (!file.exists()) { // check if file exists/can be opened or not
            System.out.println("error opening file"); // print message if cant open file
            return;
        }
        // init scanner
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (Exception e) {
            System.out.println(e);
        }
        assert scanner != null; // assume scanner not null. if assumption not true, system will throw an error
        n = scanner.nextInt(); // get the number of rows
        m = scanner.nextInt(); // get the number of columns
        maze = new char[n][m]; // create a matrix with n rows and m columns

        // fill value to matrix
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < m; j++) {
                maze[i][j] = line.charAt(j);
            }
        }
    }

    public static void dfs(int i, int j, int goldCollected, StringBuilder currentPath, String direction) {
        // check if this position is valid to go to or not, if not then return
        if(i<0 || i>=n || j<0 || j>=m || maze[i][j] == 'X') {
            return;
        }

        // if can go then update current path
        currentPath.append(direction);

        int currentCoin; // to get the number of golds at this location
        if(maze[i][j] == '.') // if land then there's no gold
            currentCoin = 0;
        else
            currentCoin = maze[i][j] - '0';  // if gold then assign value (int) to current gold

        // check if current collected golds is bigger than current maxGold
        // if true then update maxGold and finalPath
        // also, if it equals maxGold but has shorter path, then update finalPath
        if (maxGold < (goldCollected + currentCoin) || (maxGold == (goldCollected+currentCoin) && (finalPath.length() > currentPath.length()))) {
            maxGold = goldCollected + currentCoin;
            finalPath = currentPath.toString();
        }

        // go down, same column
        dfs(i+1,j,goldCollected + currentCoin, currentPath,"D");
        // go right, same row
        dfs(i,j+1,goldCollected + currentCoin, currentPath,"R");

        // remove the last element in currentPath when return
        if (currentPath.length() > 0)
            currentPath.deleteCharAt(currentPath.length()-1);
    }

    public static int solve() {
        dfs(0,0,0, new StringBuilder(),"");
        return maxGold;
    }


    public static void main(String[] args) {
        if (args.length != 1) return;
        read(args[0]);
        System.out.println("Golds = " + solve());
        System.out.println("Steps = " + finalPath.length());
        System.out.println("Path: " + finalPath);
    }
}
