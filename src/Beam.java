package src;

import java.io.File;
import java.io.IOException;
import lib.Box;
import java.util.Arrays;
import java.util.Scanner;

public final class Beam {
    public static final int BEAM_HORIZ = 0;
    public static final int BEAM_VERT = 1;
    public static final int BEAM_REFLECT_FORWARD = 2;
    public static final int BEAM_REFLECT_BACKWARD = 3;

    public int row, col;
    public char dir, prevDir;
    public int prevOp;
    public char[][] field;

    // ------------------------------------------------------------------- Constructors
    public Beam() {
        init();
    }

    public Beam(String f) {
        this(f.toCharArray());
    }

    public Beam(char[] f) {
        init();

        for (int i = 0; i < field.length; i++) {
            field[i] = Arrays.copyOfRange(f, i * 13, (i * 13) + 13);
        }
    }

    public Beam(char[][] f) {
        init();

        for (int i = 0; i < field.length; i++) {
            field[i] = Arrays.copyOf(f[i], f[i].length);
        }
    }
    
    public Beam(File f) throws IOException {
        init();

        Scanner in = new Scanner(f);
        for (int i = 0; i < field.length; i++) {
            field[i] = in.nextLine().toCharArray();
        }
        in.close();
    }

    private void init() {
        col = -1;
        row = -1;
        dir = 'N';
        prevDir = 'N';
        prevOp = -1;
        field = new char[13][13];
    }

    // ------------------------------------------------------------------- Public Methods
    
    // Traces a path from [start] through the beam's mirror field and returns the first character that it hits
    // Traces the entire path at once
    public char tracePath(char start) {
        initTrace(start);

        int count = 0;
        do {
            advance();
            if (row == -1 || row == 13 || col == -1 || col == 13) {
                return getChar();
            }
            if (field[row][col] == '\\' || field[row][col] == '/') {
                mirror(field[row][col]);
                prevOp = field[row][col] == '/' ? BEAM_REFLECT_FORWARD : BEAM_REFLECT_BACKWARD;
            } else {
                if (dir == 'L' || dir == 'R')
                    prevOp = BEAM_HORIZ;
                else
                    prevOp = BEAM_VERT;
            }
            count++;
        } while (count < 1000);
        return '?';
    }

    // Advances the beam 1 step, checks for border collision, and handles mirror impacts
    // Returns true if the beam impacts the edge, otherwise false
    public void traceStep() {
        advance();
        if (row == -1 || row == 13 || col == -1 || col == 13) {
            return;
        }
        if (field[row][col] == '\\' || field[row][col] == '/') {
            mirror(field[row][col]);
            prevOp = field[row][col] == '/' ? BEAM_REFLECT_FORWARD : BEAM_REFLECT_BACKWARD;
        } else {
            if (dir == 'L' || dir == 'R')
                prevOp = BEAM_HORIZ;
            else
                prevOp = BEAM_VERT;
        }
        return;
    }

    // Prepares the beam to either trace a path or trace steps
    // Sets this.row, this.col, and this.dir to the correct values for [start]
    // It is redundant to call this before calling tracePath
    public void initTrace(char start) {
        int[] pd = getPosDir(start);
        col = pd[0];
        row = pd[1];
        setDir(pd[2]);
    }

    // Returns the last whole-step that the beam performed (prevOp) as an array of two half-steps (Box constant pieces)
    // Intended for use with this.traceStep(), as using it with this.tracePath() would not result in the expected behavior
    // The frames in the returned array are in chronological order
    public int[] getAnimationFrames() {
        switch (prevOp) {
            case BEAM_HORIZ:
                if (prevDir == 'R')
                    return new int[]{Box.HALF_LEFT, Box.HALF_RIGHT};
                if (prevDir == 'L')
                    return new int[]{Box.HALF_RIGHT, Box.HALF_LEFT};
                break;
            case BEAM_VERT:
                if (prevDir == 'D')
                    return new int[]{Box.HALF_TOP, Box.HALF_BOTTOM};
                if (prevDir == 'U')
                    return new int[]{Box.HALF_BOTTOM, Box.HALF_TOP};
                break;
            case BEAM_REFLECT_FORWARD:
                if (prevDir == 'D')
                    return new int[]{Box.HALF_TOP, Box.HALF_LEFT};
                if (prevDir == 'U')
                    return new int[]{Box.HALF_BOTTOM, Box.HALF_RIGHT};
                if (prevDir == 'R')
                    return new int[]{Box.HALF_LEFT, Box.HALF_TOP};
                if (prevDir == 'L')
                    return new int[]{Box.HALF_RIGHT, Box.HALF_BOTTOM};
                break;
            case BEAM_REFLECT_BACKWARD:
                if (prevDir == 'D')
                    return new int[]{Box.HALF_TOP, Box.HALF_RIGHT};
                if (prevDir == 'U')
                    return new int[]{Box.HALF_BOTTOM, Box.HALF_LEFT};
                if (prevDir == 'R')
                    return new int[]{Box.HALF_LEFT, Box.HALF_BOTTOM};
                if (prevDir == 'L')
                    return new int[]{Box.HALF_RIGHT, Box.HALF_TOP};
                break;
        }
        return new int[]{-1, -1};
    }

    // ------------------------------------------------------------------- Private methods
    private void advance() {
        prevDir = dir;
        if (dir == 'D')
            row += 1;
        if (dir == 'U')
            row -= 1;
        if (dir == 'R')
            col += 1;
        if (dir == 'L')
            col -= 1;
    }

    // Sets the direction (this.dir) of the beam according to the type of mirror [m] and updates this.prevDir accordingly
    // This method should only be called if the beam is currently at [m]
    private void mirror(char m) {
        prevDir = dir;
        if (m == '\\') {
            if (dir == 'L' || dir == 'U') {
                dir = dir == 'L' ? 'U' : 'L';
            } else {
                dir = dir == 'R' ? 'D' : 'R';
            }
        }
        if (m == '/') {
            if (dir == 'L' || dir == 'D') {
                dir = dir == 'L' ? 'D' : 'L';
            } else {
                dir = dir == 'R' ? 'U' : 'R';
            }
        }
    }

    // Converts direction as int to direction as char and assigns it to this.dir
    // Intended for use with getPosDir, which returns an int for direction
    private void setDir(int d) {
        if (d == 1)
            dir = 'L';
        if (d == 2)
            dir = 'U';
        if (d == 3)
            dir = 'R';
        if (d == 4)
            dir = 'D';
    }

    // Gets the position of a certain character on the board as well as the initial direction of the beam from that character
    // Returns an array where index 0 and 1 are the column and row of the character, and index 2 is the direction
    // 1 is left, 2 is up, 3 is right, 4 is down (for direction)
    private int[] getPosDir(char c) {
        int[] r = new int[3];
        if (c <= 'Z') {
            if (c <= 'M') {
                r[0] = -1;
                r[1] = c - 65;
                r[2] = 3;
            } else {
                r[0] = c - 78;
                r[1] = 13;
                r[2] = 2;
            }
        } else {
            if (c <= 'm') {
                r[0] = c - 97;
                r[1] = -1;
                r[2] = 4;
            } else {
                r[0] = 13;
                r[1] = c - 110;
                r[2] = 1;
            }
        }
        return r;
    }

    // Returns the char at (col, row) or '?' if not on the border
    public char getChar() {
        if (col == -1) {
            return (char)(row + 65);
        }
        if (col == 13) {
            return (char)(row + 110);
        }
        if (row == -1) {
            return (char)(col + 97);
        }
        if (row == 13) {
            return (char)(col + 78);
        }
        return '?';
    }
}