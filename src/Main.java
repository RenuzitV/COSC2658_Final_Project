import javafx.util.Pair;
import java.io.File;
import java.util.Scanner;

public class Main {
    private static final char[][] maze = new char[25][25];
    //a pair consists of a key (coin collected) and a value (length of path)
    private static final Pair<Integer, Integer>[][] dp = new Pair[25][25];
    //this pair contains the parent location that leads to this location being the most optimal path
    private static final Pair<Integer, Integer>[][] parent = new Pair[25][25];
    private static int n, m; // n: rows; m: columns

    // read maze from file
    public static void read(String filename){
        File file = new File(filename);
        // check if file exists
        if (!file.exists()){
            return;
        }
        // init scanner
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (Exception e){
            System.out.println("file not found or cannot be read");
            return; //exit function if there is no file to be read
        }

        n = scanner.nextInt(); // get the number of rows
        m = scanner.nextInt(); // get the number of columns
        scanner.nextLine(); //remove \n character

        for (int i = 1; i <= n; ++i){
            maze[i] = (" " + scanner.nextLine() + " ").toCharArray(); //read maze in, and offsets the maze by to start at (1, 1) and ends at (n, m)
        }

        //initialize dynamic programming matrix
        //we want the bounds to be (-1, 0) so the program does not seek solution from outside of the box
        for (int i = 0; i <= n+1; ++i){
            for (int j = 0; j <= m+1; ++j) {
                dp[i][j] = new Pair<>(-500, 0); //anything that is not the root is initially unreachable, and therefore has -500 weighting
                if (maze[i][j] == '.') maze[i][j] = '0'; //initialize '.' as '0' for convenience
            }
        }
        //the idea of -500 is to make sure whatever optimal answer we can get from "unreachable nodes" (nodes that cannot
        //be traversed from (1, 1)) is always worse than if we did not move at all.
        //the most we can get from such an answer is to move 25 moves down and 24 moves right, with 9 points each move, which is 449, and we round it up to 500 for simplicity.
        dp[1][1] = new Pair<>(0, 0); //set root as our only reachable node
    }

    //function to see if this pair of result is better than another
    //a pair of result is better if its coins collected is the largest, and its path length the shortest
    private static boolean better(Pair<Integer, Integer> a, Pair<Integer, Integer> b){
        return a.getKey() > b.getKey() || (a.getKey().equals(b.getKey()) && a.getValue() < b.getValue());
    }

    //adds 2 pairs together, (a, b) + (c, d) = (a+c, b+d)
    private static Pair<Integer, Integer> add(Pair<Integer, Integer> a, Pair<Integer, Integer> b){
        return new Pair<>(a.getKey()+b.getKey(), a.getValue() + b.getValue());
    }

    public static void solve(){
        //initialize v as the end location of the best path, and res as its coin collected and length of path
        Pair<Integer, Integer> v = new Pair<>(1, 1);
        Pair<Integer, Integer> res = new Pair<>(0, 0);
        //traverse from top left to bottom right
        for (int i = 1; i <= n; ++i){
            for (int j = 1; j <= m; ++j){
                //if the current node is not 'X'
                if (maze[i][j] != 'X'){
                    //make a pair of (coins, length of path for this node)
                    Pair<Integer, Integer> temp = add(dp[i - 1][j], new Pair<>(maze[i][j] - '0', 1));
                    //if the result at this node is either reachable i.e. (0, 0) vs (-1, 0) or just better i.e. (12, 4) vs (15,6) then we update the current node
                    if (better(temp, dp[i][j])) {
                        dp[i][j] = temp; //update result
                        parent[i][j] = new Pair<>(i - 1, j); //update parent node
                    }

                    //same thing as above, but from the left
                    temp = add(dp[i][j-1], new Pair<>(maze[i][j] - '0', 1));
                    if (better(temp, dp[i][j])){
                        dp[i][j] = temp;
                        parent[i][j] = new Pair<>(i, j - 1);
                    }

                    //if current result is the best so far, update both v and res.
                    if (better(dp[i][j], res)){
                        res = dp[i][j];
                        v = new Pair<>(i, j);
                    }
                }
            }
        }
        //string for path result
        StringBuilder temp = new StringBuilder();
        //traverse from end location up its parents to get to the root, which is always (1, 1)
        while (v.getValue() > 1 || v.getKey() > 1){
            Pair<Integer, Integer> u = parent[v.getKey()][v.getValue()]; //get the parent of v, the location from which we traverse to v
            if (u.getValue() < v.getValue()){
                temp.append("R"); //if we went right (parent is to the left), append R
            }
            else temp.append("D"); //if we went down (parent is above), append D
            v = u; //set current coordinate to its parent, and repeat
        }
        //print result and path
        System.out.println(res.getKey() + " " + temp.reverse());
    }

    public static void main(String[] args) {
        if (args.length != 1) return;
        read(args[0]);
        solve();
    }
}