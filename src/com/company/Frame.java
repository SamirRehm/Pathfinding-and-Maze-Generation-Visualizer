package com.company;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private final JPanel mainPanel;
    private final JPanel eastPanel;
    private final JButton bGenerate;
    private final JButton findPath;
    private final JButton findPathDFS;

    public Frame() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Grid g = new Grid();
        this.setResizable(false);
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.add(g, BorderLayout.WEST);
        this.add(mainPanel);

        this.eastPanel = new JPanel(new GridLayout(3, 0));

        this.bGenerate = new JButton("Generate Maze");
        bGenerate.addActionListener(e -> {
            g.simulate();
        });

        this.findPath = new JButton("BFS Pathfinding");
        findPath.addActionListener(e -> {
            Timer sim = g.breadth_first_search();
            sim.start();
        });

        this.findPathDFS = new JButton("DFS Pathfinding");
        findPathDFS.addActionListener(e -> {
            Timer sim = g.depth_first_search();
            sim.start();
        });
        eastPanel.add(bGenerate);
        eastPanel.add(findPath);
        eastPanel.add(findPathDFS);

        this.mainPanel.add(eastPanel, BorderLayout.EAST);

        this.setVisible(true);
        this.pack();
    }
}
