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

        // Perform BFS to find shortest path
        ArrayList<Positions> shortestPath = pathFinder(map, start, finish, rocks);

        // Output the path
        if (shortestPath.isEmpty()) {
            System.out.println("No path found.");
        } else {
            System.out.println("Shortest path:");
            // Output each step in the shortest path
            for (int i = 0; i < shortestPath.size(); i++) {
                Positions current = shortestPath.get(i);
                System.out.println((i + 1) + ". Move to (" + (current.x + 1) + "," + (current.y + 1) + ")");
            }
        }
    }

    // Method to find the shortest path using BFS
    private static ArrayList<Positions> pathFinder(char[][] map, Positions start, Positions finish, ArrayList<Positions> rocks) {
        Queue<Positions> queue = new LinkedList<>();
        queue.add(start);
        boolean[][] visited = new boolean[map.length][map[0].length];
        Positions[][] parent = new Positions[map.length][map[0].length];

        // BFS algorithm
        while (!queue.isEmpty()) {
            Positions current = queue.poll();
            // Check if reached the finish position
            if (current.x == finish.x && current.y == finish.y) {
                return pathway(parent, start, finish); // Reconstruct path
            }
            // Explore adjacent positions
            for (int i = 0; i < 4; i++) {
                int nx = current.x + dx[i];
                int ny = current.y + dy[i];
                // Check if the new position is valid and not visited
                if (checker(map, nx, ny, rocks) && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    queue.add(new Positions(nx, ny));
                    parent[nx][ny] = current; // Store parent for path reconstruction
                }
            }
        }
        // If no path found
        return new ArrayList<>();
    }

    // Method to check if a position is valid
    private static boolean checker(char[][] map, int x, int y, ArrayList<Positions> rocks) {
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) {
            return false; // Out of bounds
        }
        if (map[x][y] == '0') {
            return false; // Rock
        }
        return true;
    }

    // Method to reconstruct the shortest path
    private static ArrayList<Positions> pathway(Positions[][] parent, Positions start, Positions finish) {
        ArrayList<Positions> path = new ArrayList<>();
        Positions current = finish;
        // Reconstruct path by following parent pointers
        while (current != start) {
            path.add(0, current); // Add to the beginning of the list
            current = parent[current.x][current.y];
        }
        path.add(0, start);
        return path;
    }
}

