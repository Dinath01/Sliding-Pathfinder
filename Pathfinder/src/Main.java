import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    // Arrays representing directional movement: right, down, left, up
    private static final int[] dx = {0, 1, 0, -1};
    private static final int[] dy = {1, 0, -1, 0};

    public static void main(String[] args) {
        char[][] map;
        Positions start = null;
        Positions finish = null;
        ArrayList<Positions> rocks = new ArrayList<>();

        // Read input map from file
        try {
            Scanner scanner = new Scanner(new File("input.txt"));
            int rows = scanner.nextInt();
            int cols = scanner.nextInt();
            map = new char[rows][cols];
            scanner.nextLine(); // Consume newline

            // Loop through each row and column to populate the map
            for (int i = 0; i < rows; i++) {
                String line = scanner.nextLine();
                for (int j = 0; j < cols; j++) {
                    map[i][j] = line.charAt(j);
                    // Check for start, finish, and rocks positions
                    if (map[i][j] == 'S') {
                        start = new Positions(i, j);
                    } else if (map[i][j] == 'F') {
                        finish = new Positions(i, j);
                    } else if (map[i][j] == '0') {
                        rocks.add(new Positions(i, j));
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found.");
            return;
        }
    }
}