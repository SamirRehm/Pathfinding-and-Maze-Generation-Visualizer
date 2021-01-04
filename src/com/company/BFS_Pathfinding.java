package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static com.company.Constants.COLS;
import static com.company.Constants.ROWS;

public class BFS_Pathfinding {

    // Store a grid reference
    private Grid grid;
    private java.util.Queue<Spot> bfs_queue = new LinkedList<>();
    private java.util.Queue<Spot> bfs_progress_queue = new LinkedList<>();
    private Timer bfsTimer;

    public enum Phase {
        SPREAD,
        BACKTRACK
    }

    public Phase algoPhase = Phase.SPREAD;
    private Spot[][] parents;
    private Spot backtrack = null;

    public BFS_Pathfinding(Grid g) {
        grid = g;
        parents = new Spot[Constants.ROWS][Constants.COLS];
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLS; ++j) {
                parents[i][j] = null;
            }
        }
    }

    public Timer startAlgo() {
        bfs_queue.add(grid.grid[0][0]);
        bfsTimer =
                new Timer(
                        60,
                        e -> {
                            if (algoPhase == Phase.SPREAD) {
                                bfs_progress_queue = new LinkedList<>();
                                while (!bfs_queue.isEmpty()) {
                                    Spot s = bfs_queue.poll();
                                    if (s.getCol() == COLS - 1 && s.getRow() == ROWS - 1) {
                                        this.algoPhase = Phase.BACKTRACK;
                                        backtrack = s;
                                    }
                                    s.color = Color.BLUE;
                                    java.util.List<Spot> neighbours =
                                            s.reachable_neighbours().stream()
                                                    .filter(x -> x.color == Color.WHITE)
                                                    .collect(Collectors.toList());
                                    if (neighbours.size() == 0) {
                                        s.color = Color.RED;
                                    }
                                    bfs_progress_queue.addAll(neighbours);
                                    neighbours.forEach(
                                            spot -> {
                                                parents[spot.getRow()][spot.getCol()] = s;
                                            });

                                    neighbours.forEach(x -> x.color = Color.ORANGE);
                                }
                                bfs_queue.addAll(bfs_progress_queue);
                            } else {
                                backtrack.color = Color.GREEN;
                                backtrack = parents[backtrack.getRow()][backtrack.getCol()];
                                if (backtrack == null) {
                                    bfsTimer.stop();
                                }
                            }
                            grid.revalidate();
                            grid.repaint();
                        });
        return bfsTimer;
    }
}
