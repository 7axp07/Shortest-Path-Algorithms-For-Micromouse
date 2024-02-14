package com.company;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {

    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private static final int CANVAS_WIDTH = WIDTH * 30;
    private static final int CANVAS_HEIGHT = HEIGHT * 30;

    private static final int[][] MAZE_3 = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
            {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1},
            {1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1},
            {1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1},
            {1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1},
            {1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1},
            {1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    static int[][] maze = new int[HEIGHT][WIDTH];

    private static final Color EMPTY_COLOR = Color.WHITE;
    private static final Color WALL_COLOR = Color.BLACK;
    private static final Color PATH_COLOR_BF = Color.BLUE;
    private static final Color PATH_COLOR_DIJKSTRA = Color.RED;
    private static final Color SHORTEST_PATH_COLOR = Color.YELLOW;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        maze = MAZE_3;

        drawMaze(gc, maze);

         int[][] distancesBF = bellmanFord();
          drawPath(gc, distancesBF, PATH_COLOR_BF);

       // int[][] distancesDijkstra = dijkstra();
       // drawPath(gc, distancesDijkstra, PATH_COLOR_DIJKSTRA);



        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Maze Solving");
        primaryStage.show();

      //  System.out.println(Arrays.deepToString(distancesDijkstra));

    }

    private void drawMaze(GraphicsContext gc, int[][] maze) {
        gc.setFill(EMPTY_COLOR);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        for (int r = 0; r < HEIGHT; r++) {
            for (int c = 0; c < WIDTH; c++) {
                if (maze[r][c] == 1) {
                    gc.setFill(WALL_COLOR);
                    gc.fillRect(c * 30, r * 30, 30, 30);
                }
            }
        }
    }

    // Bellman Ford
    private int[][] bellmanFord() {
        int[][] distances = new int[HEIGHT][WIDTH];
        for (int[] row : distances) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        distances[1][1] = 0;

        for (int i = 0; i < WIDTH * HEIGHT - 1; i++) {
            for (int r = 1; r < HEIGHT; r++) {
                for (int c = 1; c < WIDTH; c++) {

                    if (maze[r][c] == 0) {
                        //System.out.println("empty" + r +" "+ c);
                        if (r > 0 && distances[r-1][c] != Integer.MAX_VALUE && distances[r][c] + 1 < distances[r - 1][c]) {
                            distances[r][c] = distances[r-1][c] + 1;
                        }
                        else if (c > 0 && distances[r][c-1] != Integer.MAX_VALUE && distances[r][c] + 1 < distances[r][c - 1]) {
                            distances[r][c] = distances[r][c-1] + 1;
                        }
                        else if (r < HEIGHT-1 && distances[r+1][c] != Integer.MAX_VALUE && distances[r][c] + 1 < distances[r + 1][c]) {
                            distances[r][c] = distances[r+1][c] + 1;
                        }
                        else if (c < WIDTH-1 && distances[r][c+1] != Integer.MAX_VALUE && distances[r][c] + 1 < distances[r][c + 1]) {
                            distances[r][c] = distances[r][c+1] + 1;
                        }
                    }
                }
            }
        }

        /*for (int rr = 0; rr < HEIGHT; rr++) {
            for (int cc = 0; cc < WIDTH; cc++) {
                System.out.print(distances[rr][cc] + "  ");
            }
            System.out.println();
        }*/
        return distances;
    }

    // Dijkstra
    private int[][] dijkstra() {
        int[][] distances = new int[HEIGHT][WIDTH];
        for (int[] row : distances) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        distances[1][1] = 0;

        boolean[][] visited = new boolean[HEIGHT][WIDTH];

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{1, 1});

        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0];
            int c = cell[1];

            if (visited[r][c]) {
                continue;
            }
            visited[r][c] = true;

            for (int[] dir : directions) {
                int newRow = r + dir[0];
                int newCol = c + dir[1];
                if (newRow >= 0 && newRow < HEIGHT && newCol >= 0 && newCol < WIDTH && maze[newRow][newCol] == 2){
                    return distances;
                }
                if (newRow >= 0 && newRow < HEIGHT && newCol >= 0 && newCol < WIDTH && maze[newRow][newCol] == 0) {
                    int newDist = distances[r][c] + 1;
                    if (newDist < distances[newRow][newCol]) {
                        distances[newRow][newCol] = newDist;
                        queue.add(new int[]{newRow, newCol});
                    }
                }
            }
        }

        return distances;
    }

    private void drawPath(GraphicsContext gc, int[][] distances, Color pathColor) {
        /*for (int rr = 0; rr < HEIGHT; rr++) {
            for (int cc = 0; cc < WIDTH; cc++) {
                System.out.print(distances[rr][cc] + "  ");
            }
            System.out.println();
        }*/
        gc.setFill(pathColor);
        for (int r = 0; r < HEIGHT; r++) {
            for (int c = 0; c < WIDTH; c++) {
                if (distances[r][c] != Integer.MAX_VALUE) {
                    gc.fillRect(c * 30, r * 30, 30, 30);
                }
            }
        }
        gc.setFill(SHORTEST_PATH_COLOR);
        List<int[]> path = shortestPath(distances, 18, 1 );
        for (int i = 0; i < path.size(); i++) {
            gc.fillRect(path.get(i)[1] * 30, path.get(i)[0] * 30, 30, 30);
        }
    }

    private List<int[]> shortestPath(int[][] distances, int r, int c){
        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        List<int[]> coordinates = new ArrayList<>();
        coordinates.add(new int[]{r, c});

        while (true) {
            if (r == 1 && c == 1){
                break;
            }
            int minDistance = Integer.MAX_VALUE;
            int minR = 0;
            int minC = 0;

            for (int[] dir : directions) {
                int newRow = r + dir[0];
                int newCol = c + dir[1];
                if (newRow >= 0 && newRow < HEIGHT && newCol >= 0 && newCol < WIDTH && maze[newRow][newCol] == 0) {
                    if (minDistance > distances[newRow][newCol]){
                        minC = newCol;
                        minR = newRow;
                        minDistance = distances[newRow][newCol];
                    }
                }
            }

            r = minR;
            c = minC;
            coordinates.add(new int[]{r, c});

//            System.out.println(r + " " + c + " " + minDistance);
//            for (int i = 0; i < coordinates.size(); i++) {
//
//                System.out.println(Arrays.toString(coordinates.get(i)));
//            }
        }

        return coordinates;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
