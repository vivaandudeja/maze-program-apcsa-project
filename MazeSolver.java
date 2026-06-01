import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class MazeSolver {
    private int startX;
    private int startY;

    private final String path;
    private final String wall;
    private final String start;
    private final String end;

    private final String[][] input;
    private final String solvedPath;

    private final int[][] isGood;


    private boolean solved;



    public MazeSolver(String[][] maze,
                      String path,
                      String wall,
                      String start,
                      String end,
                      String solvedPath) {

        this.path = path;
        this.wall = wall;
        this.start = start;
        this.end = end;
        this.solvedPath = solvedPath;

        int rows = maze.length;
        int cols = maze[0].length;

        this.input = new String[rows][cols];
        this.isGood = new int[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                this.input[row][col] = maze[row][col];

                if (maze[row][col].equals(path)) {
                    isGood[row][col] = 1;
                }
                else {
                    isGood[row][col] = 0;
                }

                if (maze[row][col].equals(start)) {
                    startX = row;
                    startY = col;
                }
            }
        }

        // Solve and store result
        solved = solve(startX, startY);

        if (!solved) {
            System.out.println("This maze has no solution :(");
        }
    }

    public boolean solve(int r, int c) {

        // Out of bounds
        if (r < 0 || r >= input.length ||
            c < 0 || c >= input[r].length) {

            return false;
        }

        // Found the end
        if (input[r][c].equals(end)) {
            return true;
        }

        // Wall or visited
        if (isGood[r][c] != 1 &&
            !input[r][c].equals(start)) {

            return false;
        }

        // Mark current location as possible solution path
        if (!input[r][c].equals(start)) {
            isGood[r][c] = 2;
        }

        int[][] directions = {
            {-1, 0}, 
            {1, 0},  
            {0, -1}, 
            {0, 1}   
        };

        for (int[] dir : directions) {

            int nextR = r + dir[0];
            int nextC = c + dir[1];

            if (solve(nextR, nextC)) {
                return true;
            }
        }

        // Backtrack: dead end
        if (!input[r][c].equals(start)) {
            isGood[r][c] = 3;
        }

        return false;
    }

    public String returnValue(int row, int col) {

        if (input[row][col].equals(start)) {
            return start;
        }

        else if (input[row][col].equals(end)) {
            return end;
        }

        else if (input[row][col].equals(wall)) {
            return wall;
        }

        else if (isGood[row][col] == 2) {
            return solvedPath;
        }

        else if (input[row][col].equals(path)
                || isGood[row][col] == 3) {

            return path;
        }

        return "?";
    }

    @Override
    public String toString() {

        if (!solved) {
            return "This maze has no solution :(";
        }

        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < input.length; row++) {

            for (int col = 0; col < input[row].length; col++) {
                sb.append(returnValue(row, col));
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    public void saveSolvedMazeImage() {

        if (!solved) {
            System.out.println("Cannot save: This maze has no solution :(");
            return;
        }

        try {

            FileDialog fd = new FileDialog(
                    (Frame) null,
                    "Save Solved Maze",
                    FileDialog.SAVE);

            fd.setFile("solvedMaze.png");

            fd.setVisible(true);

            if (fd.getFile() == null) {
                System.out.println("Save cancelled.");
                return;
            }

            String directory = fd.getDirectory();
            String filename = fd.getFile();

            if (!filename.toLowerCase().endsWith(".png")) {
                filename += ".png";
            }

            File outputFile =
                    new File(directory + filename);

            int cellSize = 20;

            int rows = input.length;
            int cols = input[0].length;

            BufferedImage image =
                    new BufferedImage(
                            cols * cellSize,
                            rows * cellSize,
                            BufferedImage.TYPE_INT_RGB);

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    Color color;

                    if (input[row][col].equals(start)) {
                        color = Color.BLUE;
                    }

                    else if (input[row][col].equals(end)) {
                        color = Color.RED;
                    }

                    else if (input[row][col].equals(wall)) {
                        color = Color.BLACK;
                    }

                    else if (isGood[row][col] == 2) {
                        color = Color.GREEN;
                    }

                    else {
                        color = Color.WHITE;
                    }

                    for (int y = row * cellSize;
                         y < (row + 1) * cellSize;
                         y++) {

                        for (int x = col * cellSize;
                             x < (col + 1) * cellSize;
                             x++) {

                            image.setRGB(
                                    x,
                                    y,
                                    color.getRGB());
                        }
                    }
                }
            }

            ImageIO.write(
                    image,
                    "png",
                    outputFile);

            System.out.println(
                    "Solved maze saved to:");

            System.out.println(
                    outputFile.getAbsolutePath());

        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}