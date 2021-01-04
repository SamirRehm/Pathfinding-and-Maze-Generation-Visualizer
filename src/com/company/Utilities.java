package com.company;

import static com.company.Constants.COLS;
import static com.company.Constants.ROWS;

public class Utilities {

    public static boolean check_bounds(int row, int col) {
        return row >= 0 && col >= 0 && row < ROWS && col < COLS;
    }

    public static int generate_int_between(int low, int high) {
        int r = (int)(Math.random() * (high - (low+1))) + (low+1);
        return r;
    }

    public static class Quadruple {
        int a;
        int b;
        int c;
        int d;

        public Quadruple(int a, int b, int c, int d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }
    }
}
