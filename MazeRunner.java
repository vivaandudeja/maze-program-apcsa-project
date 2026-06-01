import java.util.*;

public class MazeRunner {

    public static void main(String[] args) {
        System.out.println("Welcome to my Maze Program!");
        program();
    }

    public static int terminal(Scanner scanner) {

        System.out.println("\nPlease enter what you would like to do:");
        System.out.println("1. Generate a maze");
        System.out.println("2. Solve a maze");
        System.out.println("3. Exit");

        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.nextLine();
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        return choice;
    }

    public static void program() {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            try {

                int choice = terminal(scanner);

                // Exit
                if (choice == 3) {
                    System.out.println("Goodbye!");
                    break;
                }

                // Generate Maze
                if (choice == 1) {

                    System.out.print(
                        "Please enter the width of the maze: ");

                    int width = scanner.nextInt();

                    System.out.print(
                        "Please enter the height of the maze: ");

                    int height = scanner.nextInt();

                    scanner.nextLine();

                    if (width <= 0 || height <= 0) {
                        throw new IllegalArgumentException(
                            "Width and height must be greater than 0."
                        );
                    }

                    System.out.println(
                        "\n[Generating a " +
                        width +
                        "x" +
                        height +
                        " maze...]"
                    );

                    Maze maze =
                            new Maze(height,width);

                    System.out.println(maze);

                    maze.saveMazeImage();
                }

                // Solve Maze
                else if (choice == 2) {

                    System.out.print(
                        "Please enter the start character: ");
                    String start =
                            scanner.nextLine();

                    System.out.print(
                        "Please enter the wall character: ");
                    String wall =
                            scanner.nextLine();

                    System.out.print(
                        "Please enter the path character: ");
                    String path =
                            scanner.nextLine();

                    System.out.print(
                        "Please enter the end character: ");
                    String end =
                            scanner.nextLine();

                    System.out.print(
                        "Please enter the solved path character: ");
                    String solvedPath =
                            scanner.nextLine();

                    // Ensure single-character symbols
                    if(start.length()!=1 ||
                       wall.length()!=1 ||
                       path.length()!=1 ||
                       end.length()!=1 ||
                       solvedPath.length()!=1) {

                        throw new IllegalArgumentException(
                            "Maze symbols must be one character only."
                        );
                    }

                    System.out.println(
                        "\nPlease enter the maze "
                        + "(Type 'END' on a new line when finished):"
                    );

                    ArrayList<String> mazeLines =
                            new ArrayList<>();

                    while (scanner.hasNextLine()) {

                        String line =
                                scanner.nextLine();

                        if(line.equalsIgnoreCase("END")) {
                            break;
                        }

                        mazeLines.add(line);
                    }

                    // Empty maze
                    if (mazeLines.isEmpty()) {

                        throw new IllegalArgumentException(
                            "Maze cannot be empty."
                        );
                    }

                    int rows =
                            mazeLines.size();

                    int cols =
                            mazeLines.get(0).length();

                    // Equal row lengths
                    for(String line : mazeLines) {

                        if(line.length()!=cols) {

                            throw new IllegalArgumentException(
                                "All maze rows must have equal lengths."
                            );
                        }
                    }

                    String[][] maze2D =
                            new String[rows][cols];

                    for(int i=0;i<rows;i++) {

                        String[] characters =
                                mazeLines.get(i)
                                .split("");

                        for(int j=0;j<cols;j++) {

                            maze2D[i][j] =
                                    characters[j];
                        }
                    }

                    MazeSolver solver =
                            new MazeSolver(
                                    maze2D,
                                    path,
                                    wall,
                                    start,
                                    end,
                                    solvedPath
                            );

                    System.out.println(
                        "\n--- Solved Maze ---");

                    System.out.println(
                        solver);

                    solver.saveSolvedMazeImage();
                }

                else {

                    System.out.println(
                        "Invalid choice."
                    );
                }

            }

            catch(Exception e) {

                System.out.println(
                    "\nError: "
                    + e.getMessage());

                System.out.println(
                    "Please try again."
                );
            }
        }

        scanner.close();
    }
}