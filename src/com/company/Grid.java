package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.stream.Stream;

import static com.company.Constants.*;
import static com.company.Utilities.check_bounds;
import static com.company.Utilities.generate_int_between;

public class Grid extends JPanel {

    public Spot[][] grid;
    private Timer simulationTimer;
    private java.util.Queue<Utilities.Quadruple> maze_alg_queue = new LinkedList<>();

    public Grid() {
        setPreferredSize(new Dimension(Constants.WIDTH+16, Constants.WIDTH + 39));
        initGrid();
    }

    public void initGrid() {
        int cell_width = Constants.WIDTH / ROWS;
        grid = new Spot[ROWS][COLS];
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLS; ++j) {
                grid[i][j] = new Spot(i, j, cell_width, ROWS);
            }
        }

        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLS; ++j) {
                Spot s = grid[i][j];
                if (check_bounds(i - 1, j)) {
                    s.neighbours[0] = (grid[i - 1][j]);
                }
                if (check_bounds(i, j - 1)) {
                    s.neighbours[3] = (grid[i][j - 1]);
                }
                if (check_bounds(i + 1, j)) {
                    s.neighbours[2] = (grid[i + 1][j]);
                }
                if (check_bounds(i, j + 1)) {
                    s.neighbours[1] = (grid[i][j + 1]);
                }
            }
        }
        drawBorders();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // cast to get 2D drawing methods
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, // antialiasing look nicer
                RenderingHints.VALUE_ANTIALIAS_ON);
        draw(g2);
    }

    public void draw(Graphics2D g2) {
        Stream.of(grid).forEach(row -> Stream.of(row).forEach(spot -> spot.draw(g2)));
    }

    public void simulate() {
        initGrid();
        maze_alg_queue.add(new Utilities.Quadruple(0, ROWS, 0, COLS));
        simulationTimer =
                new Timer(
                        1,
                        e -> {
                            if (!maze_alg_queue.isEmpty()) {
                                Utilities.Quadruple args = maze_alg_queue.poll();
                                generate_maze_recursive_division(
                                        args.a, args.b, args.c, args.d);
                                revalidate();
                                repaint();
                            } else {
                               simulationTimer.stop();
                            }
                        });
        simulationTimer.start();
    }

    public Timer breadth_first_search() {
        BFS_Pathfinding bfs_pathfinding = new BFS_Pathfinding(this);
        return bfs_pathfinding.startAlgo();
    }

    public Timer depth_first_search() {
        DFS_Pathfinding bfs_pathfinding = new DFS_Pathfinding(this);
        return bfs_pathfinding.startAlgo();
    }

    public void drawBorders() {
        for (int i = 0; i < ROWS; ++i) {
            add_wall(i, 0, 's');
            add_wall(i, COLS, 's');
        }
        for (int i = 0; i < COLS; ++i) {
            add_wall(0, i, 'e');
            add_wall(ROWS, i, 'e');
        }
    }

    public void generate_maze_recursive_division(int minRow, int maxRow, int minCol, int maxCol) {
        int horizontalRow = generate_int_between(minRow, maxRow);
        for (int i = minCol; i < maxCol; ++i) {
            add_wall(horizontalRow, i, 'e');
        }

        int verticalCol = generate_int_between(minCol, maxCol);
        for (int i = minRow; i < maxRow; ++i) {
            add_wall(i, verticalCol, 's');
        }

        int wallToLeaveIntact = generate_int_between(0, 5);
        if (wallToLeaveIntact != 1) {
            int spaceToRemove = generate_int_between(minCol - 1, verticalCol);
            remove_wall(horizontalRow, spaceToRemove, 'e');
        }
        if (wallToLeaveIntact != 2) {
            int spaceToRemove = generate_int_between(verticalCol - 1, maxCol);
            remove_wall(horizontalRow, spaceToRemove, 'e');
        }
        if (wallToLeaveIntact != 3) {
            int spaceToRemove = generate_int_between(minRow - 1, horizontalRow);
            remove_wall(spaceToRemove, verticalCol, 's');
        }
        if (wallToLeaveIntact != 4) {
            int spaceToRemove = generate_int_between(horizontalRow - 1, maxRow);
            remove_wall(spaceToRemove, verticalCol, 's');
        }

        if (horizontalRow - minRow > 1 && verticalCol - minCol > 1) {
            maze_alg_queue.add(new Utilities.Quadruple(minRow, horizontalRow, minCol, verticalCol));
        }
        if (maxRow - horizontalRow > 1 && verticalCol - minCol > 1) {
            maze_alg_queue.add(new Utilities.Quadruple(horizontalRow, maxRow, minCol, verticalCol));
        }
        if (horizontalRow - minRow > 1 && maxCol - verticalCol > 1) {
            maze_alg_queue.add(new Utilities.Quadruple(minRow, horizontalRow, verticalCol, maxCol));
        }
        if (maxRow - horizontalRow > 1 && maxCol - verticalCol > 1) {
            maze_alg_queue.add(new Utilities.Quadruple(horizontalRow, maxRow, verticalCol, maxCol));
        }
    }

    public void add_wall(int row, int col, char orientation) {
        if (orientation == 'n') {
            if (check_bounds(row - 1, col - 1)) {
                Spot s = grid[row - 1][col - 1];
                s.add_wall('e');
            }
            if (check_bounds(row - 1, col)) {
                Spot s2 = grid[row - 1][col];
                s2.add_wall('w');
            }
        } else if (orientation == 's') {
            if (check_bounds(row, col - 1)) {
                Spot s = grid[row][col - 1];
                s.add_wall('e');
            }
            if (check_bounds(row, col)) {
                Spot s2 = grid[row][col];
                s2.add_wall('w');
            }
        } else if (orientation == 'e') {
            if (check_bounds(row, col)) {
                Spot s = grid[row][col];
                s.add_wall('n');
            }
            if (check_bounds(row - 1, col)) {
                Spot s2 = grid[row - 1][col];
                s2.add_wall('s');
            }
        } else if (orientation == 'w') {
            if (check_bounds(row, col - 1)) {
                Spot s = grid[row][col - 1];
                s.add_wall('n');
            }
            if (check_bounds(row - 1, col - 1)) {
                Spot s2 = grid[row - 1][col - 1];
                s2.add_wall('s');
            }
        }
    }

    public void remove_wall(int row, int col, char orientation) {
        if (orientation == 'n') {
            if (check_bounds(row - 1, col - 1)) {
                Spot s = grid[row - 1][col - 1];
                s.remove_wall('e');
            }
            if (check_bounds(row - 1, col)) {
                Spot s2 = grid[row - 1][col];
                s2.remove_wall('w');
            }
        } else if (orientation == 's') {
            if (check_bounds(row, col - 1)) {
                Spot s = grid[row][col - 1];
                s.remove_wall('e');
            }
            if (check_bounds(row, col)) {
                Spot s2 = grid[row][col];
                s2.remove_wall('w');
            }
        } else if (orientation == 'e') {
            if (check_bounds(row, col)) {
                Spot s = grid[row][col];
                s.remove_wall('n');
            }
            if (check_bounds(row - 1, col)) {
                Spot s2 = grid[row - 1][col];
                s2.remove_wall('s');
            }
        } else if (orientation == 'w') {
            if (check_bounds(row, col - 1)) {
                Spot s = grid[row][col - 1];
                s.remove_wall('n');
            }
            if (check_bounds(row - 1, col - 1)) {
                Spot s2 = grid[row - 1][col - 1];
                s2.remove_wall('s');
            }
        }
    }
}
