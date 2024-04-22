import java.util.*;

class Node {
    int x, y; // Position coordinates
    int g; // Cost from start
    int h; // Heuristic (estimated cost to goal)
    Node parent; // Parent node

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        g = Integer.MAX_VALUE;
        h = 0;
        parent = null;
    }

    public int f() {
        return g + h;
    }
}

class PuzzleMap {
    char[][] map; // Map representation
    int width, height; // Map dimensions
    Node start, finish; // Start and finish nodes

    public PuzzleMap(char[][] map) {
        this.map = map;
        this.height = map.length;
        this.width = map[0].length;
        findStartAndFinish();
    }

    public int heuristic(Node node) {
        return Math.abs(node.x - finish.x) + Math.abs(node.y - finish.y);
    }

    private void findStartAndFinish() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (map[y][x] == 'S') {
                    start = new Node(x, y);
                } else if (map[y][x] == 'F') {
                    finish = new Node(x, y);
                }
            }
        }
    }

    public List<Node> findNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        for (int i = 0; i < 4; i++) {
            int nx = node.x + dx[i];
            int ny = node.y + dy[i];

            if (nx >= 0 && nx < width && ny >= 0 && ny < height && map[ny][nx] != '0') {
                neighbors.add(new Node(nx, ny));
            }
        }
        return neighbors;
    }

    public int manhattanDistance(Node node1, Node node2) {
        return Math.abs(node1.x - node2.x) + Math.abs(node1.y - node2.y);
    }

    public List<Node> findPath() {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::f));
        Map<Node, Integer> gScores = new HashMap<>();
        openSet.add(start);
        start.g = 0;
        gScores.put(start, 0);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current == finish) {
                return reconstructPath(current);
            }

            for (Node neighbor : findNeighbors(current)) {
                int tentativeG = gScores.get(current) + 1; // Assuming each step has a cost of 1
                if (tentativeG < gScores.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    neighbor.parent = current;
                    neighbor.g = tentativeG;
                    neighbor.h = manhattanDistance(neighbor, finish); // Calculate heuristic
                    gScores.put(neighbor, tentativeG);
                    openSet.add(neighbor);
                }
            }
        }
        return null; // No path found
    }



    private List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }
}

public class Main {
    public static void main(String[] args) {
        char[][] map = {
                {'.', '.', '.', '.', '0', '.', '.', '.', 'S'},
                {'.', '.', '.', '.', '0', '.', '.', '.', '.'},
                {'0', '.', '.', '.', '.', '.', '0', '.', '0'},
                {'.', '.', '.', '.', '0', '.', '.', '.', '0'},
                {'.', 'F', '.', '.', '.', '.', '.', '.', '0'},
                {'.', '0', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '0'},
                {'.', '0', '.', '0', '.', '0', '.', '0', '0'},
                {'0', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '0', '0', '.', '.', '.', '.', '.', '0'}
        };

        PuzzleMap puzzleMap = new PuzzleMap(map);
        List<Node> path = puzzleMap.findPath();

        if (path != null) {
            for (Node node : path) {
                System.out.println("Move to (" + node.x + ", " + node.y + ")");
            }
        } else {
            System.out.println("No path found.");
        }
    }
}
