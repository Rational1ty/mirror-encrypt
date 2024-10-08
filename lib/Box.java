package lib;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

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
	private final List<Line2D.Float> normals;

	// Collection of lines that correspond to the half-step and lined style Box constants
	private final List<Line2D.Float> specials;

	public Box(int x, int y) {
		this(x, y, ' ');
	}

	public Box(int x, int y, char val) {
		this.x = x;
		this.y = y;
		value = val;

		normals = getNormals(x, y);
		specials = getSpecials(x, y);
		
		numParts = val == ' ' ? 0 : 1;
		content = new ArrayList<Line2D.Float>(numParts);

		content.add(normals.get(
			switch (val) {
				case '/' -> 0;
				case '\\' -> 1;
				case '|' -> 2;
				case '-' -> 3;
				default -> 4;
			}
		));
		
		highlight = null;
		hlArea = null;
	}

	private List<Line2D.Float> getNormals(int x, int y) {
		return List.of(
			new Line2D.Float(x, y + SCL, x + SCL, y),			// "/"
			new Line2D.Float(x, y, x + SCL, y + SCL),			// "\"
			new Line2D.Float(x + HALF, y, x + HALF, y + SCL),	// "|"
			new Line2D.Float(x, y + HALF, x + SCL, y + HALF),	// "-"
			new Line2D.Float(x, y, x, y)
		);
	}

	private List<Line2D.Float> getSpecials(int x, int y) {
		return List.of(
			new Line2D.Float(x, y + HALF, x + HALF, y + HALF),			// HALF_LEFT
			new Line2D.Float(x + HALF, y, x + HALF, y + HALF),			// HALF_TOP
			new Line2D.Float(x + HALF, y + HALF, x + SCL, y + HALF),	// HALF_RIGHT
			new Line2D.Float(x + HALF, y + HALF, x + HALF, y + SCL),	// HALF_BOTTOM

			new Line2D.Float(x, y, x, y + HALF),				// LINED_VERT_HALF_TOP left line
			new Line2D.Float(x + SCL, y, x + SCL, y + HALF),	// LINED_VERT_HALF_TOP right line

			new Line2D.Float(x, y + HALF, x, y + SCL),				// LINED_VERT_HALF_BOTTOM left line
			new Line2D.Float(x + SCL, y + HALF, x + SCL, y + SCL),	// LINED_VERT_HALF_BOTTOM right line

			new Line2D.Float(x, y, x + HALF, y),				// LINED_HORIZ_HALF_LEFT top line
			new Line2D.Float(x, y + SCL, x + HALF, y + SCL),	// LINED_HORIZ_HALF_LEFT bottom line

			new Line2D.Float(x + HALF, y, x + SCL, y),				// LINED_HORIZ_HALF_RIGHT top line
			new Line2D.Float(x + HALF, y + SCL, x + SCL, y + SCL)	// LINED_HORIZ_HALF_RIGHT bottom line
		);
	}

	// Adds the content from the corresponding Box special constant to this.content while leaving all of this box's other content intact
	public void addContent(int special) {
		if (special >= 0 && special <= 3) {
			content.add(specials.get(special));
			numParts++;
		}
	}

	// Adds content to this.content to create the desired lined style
	// style is one of the Box lined constants
	public void setLinedStyle(int[] style) {
		if (style[0] < 4 || style[1] > 11) return;
		
		content.add(specials.get(style[0]));
		content.add(specials.get(style[1]));
	}

	// Removes all extra content from this box and resets its highlight
	// "Extra content" is any content that does not represent this box's value property
	public void revert() {
		content.clear();

		content.add(normals.get(
			switch (value) {
				case '/' -> 0;
				case '\\' -> 1;
				case '|' -> 2;
				case '-' -> 3;
				default -> 4;
			}
		));

		numParts = 1;
		highlight = null;
		hlArea = null;
	}

	public void setHighlight(Color c, int area) {
		highlight = c;
		hlArea = switch (area) {
			case HALF_LEFT -> new Rectangle2D.Float(x, y, HALF, SCL);
			case HALF_TOP -> new Rectangle2D.Float(x, y, SCL, HALF);
			case HALF_RIGHT -> new Rectangle2D.Float(x + HALF, y, SCL, SCL);
			case HALF_BOTTOM -> new Rectangle2D.Float(x, y + HALF, SCL, SCL);
			default -> null;
		};
	}

	public boolean isLineNormal(Line2D.Float line) {
		return normals.stream().anyMatch(line::equals);
	}

	public boolean isLineSpecial(Line2D.Float line) {
		return specials.stream().anyMatch(line::equals);
	}

	public boolean isLineHalfStep(Line2D.Float line) {
		for (int i = 0; i <= 3; i++) {
			if (line.equals(specials.get(i))) return true;
		}
		return false;
	}

	public boolean isLineStyle(Line2D.Float line) {
		for (int i = 4; i < specials.size(); i++) {
			if (line.equals(specials.get(i))) return true;
		}
		return false;
	}
}