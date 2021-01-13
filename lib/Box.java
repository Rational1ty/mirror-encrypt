package lib;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Box {
    // Box constants
    public static final int SCL = 30;
    private static final int HALF = SCL / 2;

    public static final int HALF_LEFT = 0;
    public static final int HALF_TOP = 1;
    public static final int HALF_RIGHT = 2;
    public static final int HALF_BOTTOM = 3;

    public static final int[] LINED_VERT_HALF_TOP = {4, 5};
    public static final int[] LINED_VERT_HALF_BOTTOM = {6, 7};
    public static final int[] LINED_HORIZ_HALF_LEFT = {8, 9};
    public static final int[] LINED_HORIZ_HALF_RIGHT = {10, 11};
    public static final int[] LINED_NONE = {12, 13};

    public ArrayList<Line2D.Float> content;
    public int numParts;
    public char value;
    public Shape hlArea;
    public Color highlight;
    public int x, y;

    // Collection of lines for /, \, |, and -
    private Line2D.Float[] normals = new Line2D.Float[4];
    // Collection of lines that correspond to the half-step and lined style Box constants
    private Line2D.Float[] specials = new Line2D.Float[12];

    public Box(int x, int y) {
        this(x, y, ' ');
    }

    public Box(int x, int y, char val) {
        this.x = x;
        this.y = y;
        value = val;

        initNormals();
        initSpecials();
        
        numParts = val == ' ' ? 0 : 1;
        content = new ArrayList<Line2D.Float>(numParts);
        switch (val) {
            case '/':
                content.add(normals[0]);
                break;
            case '\\':
                content.add(normals[1]);
                break;
            case '|':
                content.add(normals[2]);
                break;
            case '-':
                content.add(normals[3]);
                break;
        }
        highlight = null;
        hlArea = null;
    }

    private void initNormals() {
        normals[0] = new Line2D.Float(x, y + SCL, x + SCL, y);                      // "/"
        normals[1] = new Line2D.Float(x, y, x + SCL, y + SCL);                      // "\"
        normals[2] = new Line2D.Float(x + HALF, y, x + HALF, y + SCL);    // "|"
        normals[3] = new Line2D.Float(x, y + HALF, x + SCL, y + HALF);    // "-"
    }

    private void initSpecials() {
        specials[0] = new Line2D.Float(x, y + HALF, x + HALF, y + HALF);         // HALF_LEFT
        specials[1] = new Line2D.Float(x + HALF, y, x + HALF, y + HALF);         // HALF_TOP
        specials[2] = new Line2D.Float(x + HALF, y + HALF, x + SCL, y + HALF);   // HALF_RIGHT
        specials[3] = new Line2D.Float(x + HALF, y + HALF, x + HALF, y + SCL);   // HALF_BOTTOM

        specials[4] = new Line2D.Float(x, y, x, y + HALF);                     // LINED_VERT_HALF_TOP left line
        specials[5] = new Line2D.Float(x + SCL, y, x + SCL, y + HALF);         // LINED_VERT_HALF_TOP right line

        specials[6] = new Line2D.Float(x, y + HALF, x, y + SCL);               // LINED_VERT_HALF_BOTTOM left line
        specials[7] = new Line2D.Float(x + SCL, y + HALF, x + SCL, y + SCL);   // LINED_VERT_HALF_BOTTOM right line

        specials[8] = new Line2D.Float(x, y, x + HALF, y);                     // LINED_HORIZ_HALF_LEFT top line
        specials[9] = new Line2D.Float(x, y + SCL, x + HALF, y + SCL);         // LINED_HORIZ_HALF_LEFT bottom line

        specials[10] = new Line2D.Float(x + HALF, y, x + SCL, y);              // LINED_HORIZ_HALF_RIGHT top line
        specials[11] = new Line2D.Float(x + HALF, y + SCL, x + SCL, y + SCL);  // LINED_HORIZ_HALF_RIGHT bottom line
    }

    // Adds the content from the corresponding Box special constant to this.content while leaving all of this box's other content intact
    public void addContent(int special) {
        if (special >= 0 && special <= 3) {
            content.add(specials[special]);
            numParts++;
        }
    }

    // Adds content to this.content to create the desired lined style
    // style is one of the Box lined constants
    public void setLinedStyle(int[] style) {
        if (style[0] < 4 || style[1] > 11) return;
        content.add(specials[style[0]]);
        content.add(specials[style[1]]);
    }

    // Removes all extra content from this box and resets its highlight
    // "Extra content" is any content that does not represent this box's value property
    public void revert() {
        content.clear();

        if (value == '/')
            content.add(normals[0]);
        if (value == '\\')
            content.add(normals[1]);
        if (value == '|')
            content.add(normals[2]);
        if (value == '-')
            content.add(normals[3]);

        numParts = 1;
        highlight = null;
        hlArea = null;
    }

    public void setHighlight(Color c, int area) {
        highlight = c;
        switch (area) {
            case HALF_LEFT:
                hlArea = new Rectangle2D.Float(x, y, HALF, SCL);
                break;
            case HALF_TOP:
                hlArea = new Rectangle2D.Float(x, y, SCL, HALF);
                break;
            case HALF_RIGHT:
                hlArea = new Rectangle2D.Float(x + HALF, y, SCL, SCL);
                break;
            case HALF_BOTTOM:
                hlArea = new Rectangle2D.Float(x, y + HALF, SCL, SCL);
                break;
        }
    }

    public boolean isLineNormal(Line2D.Float line) {
        for (Line2D.Float l : normals) {
            if (line.equals(l)) return true;
        }
        return false;
    }

    public boolean isLineSpecial(Line2D.Float line) {
        for (Line2D.Float l : specials) {
            if (line.equals(l)) return true;
        }
        return false;
    }

    public boolean isLineHalfStep(Line2D.Float line) {
        for (int i = 0; i <= 3; i++) {
            if (line.equals(specials[i])) return true;
        }
        return false;
    }

    public boolean isLineStyle(Line2D.Float line) {
        for (int i = 4; i < specials.length; i++) {
            if (line.equals(specials[i])) return true;
        }
        return false;
    }
}