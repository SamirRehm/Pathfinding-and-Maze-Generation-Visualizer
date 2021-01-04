package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Stack;
import java.util.stream.Collectors;

import static com.company.Constants.COLS;
import static com.company.Constants.ROWS;

public class DFS_Pathfinding {
    private Stack<Spot> stack = new Stack<>();
    // Store a grid reference
    private Grid grid;

    private Spot[][] parents;
    private Timer dfsTimer;
    public BFS_Pathfinding.Phase algoPhase = BFS_Pathfinding.Phase.SPREAD;
    private Spot backtrack = null;

    public DFS_Pathfinding(Grid g) {
        grid = g;
        parents = new Spot[Constants.ROWS][Constants.COLS];
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLS; ++j) {
                parents[i][j] = null;
            }
        }
    }

    public Timer startAlgo() {
        stack.push(grid.grid[0][0]);
        dfsTimer =
                new Timer(
                        30,
                        e -> {
                            if (algoPhase == BFS_Pathfinding.Phase.SPREAD) {
                                if (!stack.isEmpty()) {
                                    Spot s = stack.pop();
                                    if (s.getCol() == COLS - 1 && s.getRow() == ROWS - 1) {
                                        this.algoPhase = BFS_Pathfinding.Phase.BACKTRACK;
                                        backtrack = s;
                                    }
                                    if (s.color == Color.GREEN) {
                                        s.color = Color.RED;
                                    } else {
                                        s.color = Color.GREEN;
                                        stack.push(s);
                                        s.reachable_neighbours_dfs().stream()
                                                .filter(x -> x.color == Color.WHITE)
                                                .forEachOrdered(
                                                        x -> {
                                                            parents[x.getRow()][x.getCol()] = s;
                                                            stack.push(x);
                                                        });
                                    }
                                }
                            } else {
                                backtrack.color = Color.GREEN;
                                backtrack = parents[backtrack.getRow()][backtrack.getCol()];
                                if (backtrack == null) {
                                    dfsTimer.stop();
                                }
                            }
                            grid.revalidate();
                            grid.repaint();
                        });
        return dfsTimer;
    }
}
