import java.util.Random;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.FileDialog;
import java.awt.Frame;


public class Maze {
    private int height;
    private int width;
    private Cell[][] maze;
    private boolean[][] visited;
    private Random rand;

    public Maze(int height, int width) {
        if (height % 2 == 0) {
            height++;
        }
        if (width % 2 == 0) {
            width++;
        }

        this.height = height;
        this.width = width;
        maze = new Cell[height][width];
        visited = new boolean[height][width];
        rand = new Random();

        // Start with EVERY cell as a wall
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                maze[row][col] = new Cell(true, row, col);
            }
        }

        this.generate();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                maze[row][col].refreshValue();
            }
        }



    }

    public int returnMazeHeight()
    {
        return this.height;
    }

    public int returnMazeWidth()
    {
        return this.width;
    }

    public Cell getCell(int row, int col) {
        return maze[row][col];
    }
    public void generate() {
        backtrack(1, 1);

        maze[0][1].setWall(false);
        maze[height - 1][width - 2].setWall(false);
    }

    private void backtrack(int row, int col) {
        visited[row][col] = true;
        maze[row][col].setWall(false);  

        int[][] directions = {
            {-2, 0},
            { 2, 0},
            { 0, -2},
            { 0,  2}
        };

        // Fisher-Yates shuffle to randomize directions
        for (int i = directions.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int[] temp = directions[i];
            directions[i] = directions[j];
            directions[j] = temp;
        }

        for (int[] dir : directions) {
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];

            if (nextRow > 0 && nextRow < height - 1
             && nextCol > 0 && nextCol < width - 1
             && !visited[nextRow][nextCol]) {
                
                int wallRow = row + dir[0] / 2;
                int wallCol = col + dir[1] / 2;
                maze[wallRow][wallCol].setWall(false);

                backtrack(nextRow, nextCol);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                sb.append(maze[row][col].returnValue());
            }
            sb.append('\n');
        }
        return sb.toString();
    }



    public void saveMazeImage() {
        try {

            // Native Windows save dialog
            FileDialog fd = new FileDialog(
                    (Frame) null,
                    "Save Maze Image",
                    FileDialog.SAVE);

            fd.setFile("maze.png");

            fd.setVisible(true);

            // User hit cancel
            if (fd.getFile() == null) {
                System.out.println("Save cancelled.");
                return;
            }

            String directory = fd.getDirectory();
            String filename = fd.getFile();

            if (!filename.toLowerCase().endsWith(".png")) {
                filename += ".png";
            }

            File outputFile = new File(directory + filename);

            int cellSize = 20;

            int imageWidth = width * cellSize;
            int imageHeight = height * cellSize;

            BufferedImage image =
                    new BufferedImage(
                            imageWidth,
                            imageHeight,
                            BufferedImage.TYPE_INT_RGB);

            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {

                    Color color;

                    if (maze[row][col].isWall()) {
                        color = Color.BLACK;
                    }
                    else {
                        color = Color.WHITE;
                    }

                    for (int y = row * cellSize; y < (row + 1) * cellSize; y++) {
                        for (int x = col * cellSize; x < (col + 1) * cellSize; x++) {
                            image.setRGB(x, y, color.getRGB());
                        }
                    }
                }
            }

            ImageIO.write(image, "png", outputFile);

            System.out.println("Saved to:");
            System.out.println(outputFile.getAbsolutePath());

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}