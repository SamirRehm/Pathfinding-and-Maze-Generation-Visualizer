package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Spot {
    public final int row;
    public final int col;
    public final int x;
    public final int y;
    public Color color;
    public Spot[] neighbours;
    public final int width;
    public final int total_rows;
    public boolean[] walls;
    public java.awt.Rectangle[] wallRects;
    java.awt.Rectangle rectangle;


    public Spot(final int row, final int col, final int width, final int total_rows) {
        this.row = row;
        this.col = col;
        this.width = width;
        this.x = col * width;
        this.y = row * width;
        color = Color.WHITE;
        neighbours = new Spot[4];
        for (int i = 0; i < 4; ++i) {
            this.neighbours[i] = null;
        }
        this.total_rows = total_rows;
        this.walls = new boolean[4];
        for (int i = 0; i < 4; ++i) {
            this.walls[i] = false;
        }
        this.wallRects = new Rectangle[4];
        for (int i = 0; i < 4; ++i) {
            this.wallRects[i] = null;
        }
        this.rectangle = new Rectangle(this.x, this.y, width, width);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public Spot[] getNeighbours() {
        return neighbours;
    }

    public int getWidth() {
        return width;
    }

    public int getTotal_rows() {
        return total_rows;
    }

    public boolean[] getWalls() {
        return walls;
    }

    public void add_wall(char orientation) {
        if (orientation == 'e') {
            walls[1] = true;
            wallRects[1] = new Rectangle(this.x + this.width - 1, this.y, 1, this.width);
        } else if (orientation == 'w') {
            walls[3] = true;
            wallRects[3] = new Rectangle(this.x, this.y, 1, this.width);
        } else if (orientation == 'n') {
            walls[0] = true;
            wallRects[0] = new Rectangle(this.x, this.y, this.width, 1);
        } else if (orientation == 's') {
            walls[2] = true;
            wallRects[2] = new Rectangle(this.x, this.y + this.width - 1, this.width, 1);
        }
    }

    public void remove_wall(char orientation) {
        if (orientation == 'e') {
            walls[1] = false;
            wallRects[1] = null;
        } else if (orientation == 'w') {
            walls[3] = false;
            wallRects[3] = null;
        } else if (orientation == 'n') {
            walls[0] = false;
            wallRects[0] = null;
        } else if (orientation == 's') {
            walls[2] = false;
            wallRects[2] = null;
        }
    }

    public void draw(Graphics2D g2) {
        Color original = g2.getColor();
        g2.setColor(color);
        g2.draw(rectangle);
        g2.fill(rectangle);
        g2.setColor(Color.BLACK);
        for (Rectangle rect : wallRects) {
            if (rect != null) {
                g2.draw(rect);
                g2.fill(rect);
            }
        }
        g2.setColor(original);
    }

    public List<Spot> reachable_neighbours() {
        List<Spot> reachable_neighbours = new ArrayList<>();
        for(int i = 0; i < 4; ++i) {
            if(neighbours[i] != null && walls[i] == false) {
                reachable_neighbours.add(neighbours[i]);
            }
        }
        return reachable_neighbours;
    }

    public List<Spot> reachable_neighbours_dfs() {
        List<Spot> neighbours = reachable_neighbours();
        neighbours.sort((o1, o2) -> {
            if(o1.getRow() != o2.getRow()) {
                return Integer.compare(o1.getRow(), o2.getRow());
            }
            if(o1.getCol() != o2.getCol()) {
                return Integer.compare(o1.getCol(), o2.getCol());
            }
            return 0;
        });
        return neighbours;
    }
}
