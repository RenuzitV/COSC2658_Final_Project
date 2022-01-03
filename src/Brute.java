import java.io.File;
import java.util.Scanner;

public class Brute {
    private static String[][] maze = null;
    private static int n, m, maxGold = Integer.MIN_VALUE;
    private static String finalPath = "";

    // read maze from file
    public static void read(String filename) {
        File file = new File(filename); // attempt to open file
        if (!file.exists()) { // check if file exists/can be opened or not
            System.out.println("Error : cannot open file"); // print message if cant open file
            return;
        }
        // init scanner
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (Exception e) {
            System.out.println("Error : cannot read file/file does not exists");
            return; //exit function if there is no file to be read
        }

        try {
            n = scanner.nextInt(); // get the number of rows
            m = scanner.nextInt(); // get the number of columns
            scanner.nextLine(); //remove \n character
        } catch (Exception e){
            System.out.println("Error : cannot read number of rows or columns");
            return;
        }
        maze = new String[n][m]; // create a matrix with n rows and m columns

        for (int i = 0; i < n; ++i) {
            String[] s;
            try {
                //reads the next line, and splits them into strings seperated by spaces
                s = scanner.nextLine().split("\\s+");
            } catch (Exception e){
                System.out.println("Error : cannot read line " + i);
                return;
            }
            if (s.length != m) {
                System.out.println("Error : line " + i + " does not have " + m + " elements seperated by spaces.");
                return;
            }
            for (int j = 0; j < m; ++j){
                if (s[j].equals(".")) maze[i][j] = "0"; //initialize "." as "0" for convenience
                else maze[i][j] = s[j];
            }
        }
    }

    public static void dfs(int i, int j, int goldCollected, StringBuilder currentPath) {
        // check if this position is valid to go to or not, if not then return
        if(i<0 || i>=n || j<0 || j>=m || maze[i][j].equals("X")) {
            return;
        }

        int currentGold = 0;
        try {
            // convert value of gold in this position from string to integer to do later calculation
            currentGold = Integer.parseInt(maze[i][j]);
        }
        catch (NumberFormatException ignored) {}

        // check if current collected golds is bigger than current maxGold
        // if true then update maxGold and finalPath
        // also, if it equals maxGold but has shorter path, then update finalPath
        if (maxGold < (goldCollected + currentGold) || (maxGold == (goldCollected+currentGold) && (finalPath.length() > currentPath.length()))) {
            maxGold = goldCollected + currentGold;
            finalPath = currentPath.toString();
        }

        // go down, same column
        currentPath.append("D");
        dfs(i+1,j,goldCollected + currentGold, currentPath);
        currentPath.setLength(currentPath.length()-1); //remove previous append

        // go right, same row
        currentPath.append("R");
        dfs(i,j+1,goldCollected + currentGold, currentPath);
        currentPath.setLength(currentPath.length()-1); //remove previous append
    }

    public static int solve() {
        dfs(0,0,0, new StringBuilder());
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
