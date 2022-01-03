import java.io.File;
import java.util.Scanner;

public class Brute {
    private static int[][] maze = null;
    private static int n, m, maxGold = Integer.MIN_VALUE;
    private static String finalPath = "";

    // read maze from file : return FALSE if read unsuccessfully, otherwise return TRUE
    public static boolean read(String filename) {
        File file = new File(filename); // attempt to open file
        if (!file.exists()) { // check if file exists/can be opened or not
            System.out.println("Error : cannot open file"); // print message if cant open file
            return false;
        }
        // init scanner
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (Exception e) {
            System.out.println("Error : cannot read file/file does not exists");
            return false; //exit function if there is no file to be read
        }

        try {
            n = scanner.nextInt(); // get the number of rows
            m = scanner.nextInt(); // get the number of columns
            scanner.nextLine(); //remove \n character
        } catch (Exception e){
            System.out.println("Error : cannot read number of rows or columns");
            return false;
        }
        maze = new int[n][m]; // create a matrix with n rows and m columns

        for (int i = 0; i < n; ++i) {
            String[] s;
            try {
                //reads the next line, and splits them into strings seperated by spaces
                s = scanner.nextLine().split("\\s+");
            } catch (Exception e){
                System.out.println("Error : cannot read line " + i);
                return false;
            }
            if (s.length != m) {
                System.out.println("Error : line " + (i+2) + " does not have " + m + " elements seperated by spaces.");
                return false;
            }
            for (int j = 0; j < m; ++j){
                if (s[j].equals(".")) maze[i][j] = 0; //initialize "." as 0
                else if (s[j].equals("X")) maze[i][j] = -1; //initialize "X" as -1
                else { //convert the amount of golds at this position from string to int
                    try {
                        maze[i][j] = Integer.parseInt(s[j]);
                    }
                    //if file contains wrongly formatted character (ex: @, abc, !...) then return false
                    catch (NumberFormatException e) {
                        System.out.println("Error : wrongly formatted character found in line " + (i+2) + ".");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void dfs(int i, int j, int goldCollected, StringBuilder currentPath) {
        // check if this position is valid to go to or not, if not then return
        if(i<0 || i>=n || j<0 || j>=m || maze[i][j] == -1) {
            return;
        }

        // get the amount of golds at this position [i][j]
        int currentGold = maze[i][j];

        // check if current collected golds is BIGGER than current maxGold
        // if true then update maxGold and finalPath
        // also, if current collected golds EQUALS maxGold but has SHORTER PATH, then update finalPath
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
        //check argument
        if (args.length != 1) return;
        //read file
        if (!read(args[0])) { //if file has problem
            System.out.println("Program ended due to reading file unsuccessfully.");
            return;
        }
        //solve problem only when file is read successfully
        System.out.println("Golds = " + solve());
        System.out.println("Steps = " + finalPath.length());
        System.out.println("Path: " + finalPath);
        System.out.println("Program finished.");
    }
}
