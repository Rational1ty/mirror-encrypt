package src;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import lib.Box;
import lib.HLabel;

public final class DrawingPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public Box[][] grid;
	private Color beamColor = (Color) MirrorConstants.get("beam_color");
	
	public DrawingPanel(Beam beam) {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());

		int paddingX = Math.round((Master.WIDTH - (Box.SCL * 17)) / 2f) - 20;
		int paddingY = 25;
		setBounds(paddingX, paddingY, getWidth() - paddingX, getHeight() - paddingY);

		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		grid = new Box[17][17];
		
		// Fills in the field (middle part) of the grid with boxes, with values from beam.field
		for (int row = 2; row < grid.length - 2; row++) {
			for (int col = 2; col < grid[row].length - 2; col++) {
				// Creates a new box at the proper x and y coordinates, with the value of the corresponding mirror
				grid[row][col] = new Box(
					getX() + (col * Box.SCL),
					getY() + (row * Box.SCL),
					beam.field[row - 2][col - 2]
				);
			}
		}

		initBorderEdge();
		setEmptyBoxes();
		addCorners();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------- INITIALIZING METHODS

	// Adds boxes to the grid for the border characters and edges
	private void initBorderEdge() {
		// Iterates over the left/right edges of the border and sets them accordingly
		for (int i = 2; i < grid.length - 2; i++) {
			grid[i][0] = new Box(getX(), getY() + (i * Box.SCL), (char) (i + 63));
			grid[i][1] = new Box(getX() + Box.SCL, getY() + (i * Box.SCL), '|');
			grid[i][grid[i].length - 2] = new Box(getX() + ((grid[i].length - 2) * Box.SCL), getY() + (i * Box.SCL), '|');
			grid[i][grid[i].length - 1] = new Box(getX() + ((grid[i].length - 1) * Box.SCL), getY() + (i * Box.SCL), (char) (i + 108));
		}
		
		// Iterates over the top/bottom edges of the border and sets them accordingly
		for (int i = 2; i < grid[0].length - 2; i++) {
			grid[0][i] = new Box(getX() + (i * Box.SCL), getY(), (char) (i + 95));
			grid[1][i] = new Box(getX() + (i * Box.SCL), getY() + Box.SCL, '-');
			grid[grid.length - 2][i] = new Box(getX() + (i * Box.SCL), getY() + ((grid.length - 2) * Box.SCL), '-');
			grid[grid.length - 1][i] = new Box(getX() + (i * Box.SCL), getY() + ((grid.length - 1) * Box.SCL), (char) (i + 76));
		}

		addEdgeStyleLines();
		addCharStyleLines();
	}

	private void addEdgeStyleLines() {
		// Adds a horizontal lined style to the left/right edge boxes
		for (int i = 2; i < grid.length - 2; i++) {
			grid[i][1].setLinedStyle(Box.LINED_HORIZ_HALF_LEFT);
			grid[i][grid[i].length - 2].setLinedStyle(Box.LINED_HORIZ_HALF_RIGHT);
		}
		// Adds a vertical lined style to the top/bottom edge boxes
		for (int i = 2; i < grid[0].length - 2; i++) {
			grid[1][i].setLinedStyle(Box.LINED_VERT_HALF_TOP);
			grid[grid.length - 2][i].setLinedStyle(Box.LINED_VERT_HALF_BOTTOM);
		}
	}

	private void addCharStyleLines() {
		// Adds a horizontal lined style to the left/right border character boxes
		for (int i = 2; i < grid.length - 2; i++) {
			grid[i][0].setLinedStyle(Box.LINED_HORIZ_HALF_RIGHT);
			grid[i][grid[i].length - 1].setLinedStyle(Box.LINED_HORIZ_HALF_LEFT);
		}
		// Adds a vertical lined style to the top/bottom border character boxes
		for (int i = 2; i < grid[0].length - 2; i++) {
			grid[0][i].setLinedStyle(Box.LINED_VERT_HALF_BOTTOM);
			grid[grid.length - 1][i].setLinedStyle(Box.LINED_VERT_HALF_TOP);
		}
	}

	// Adds corner pieces
	private void addCorners() {
		// Top left
		grid[1][1].addContent(Box.HALF_RIGHT);
		grid[1][1].addContent(Box.HALF_BOTTOM);
		
		// Top right
		grid[1][grid[1].length - 2].addContent(Box.HALF_LEFT);
		grid[1][grid[1].length - 2].addContent(Box.HALF_BOTTOM);

		// Bottom left
		grid[grid.length - 2][1].addContent(Box.HALF_RIGHT);
		grid[grid.length - 2][1].addContent(Box.HALF_TOP);

		// Bottom right
		grid[grid.length - 2][grid[grid.length - 2].length - 2].addContent(Box.HALF_LEFT);
		grid[grid.length - 2][grid[grid.length - 2].length - 2].addContent(Box.HALF_TOP);
	}

	// Fills in the corners of the grid (empty spaces) with empty boxes
	private void setEmptyBoxes() {
		// Top left
		for (int y = 0; y < 2; y++)
			for (int x = 0; x < 2; x++)
				grid[y][x] = new Box(getX() + (x * Box.SCL), getY() + (y * Box.SCL));

		// Top right
		for (int y = 0; y < 2; y++)
			for (int x = grid[y].length - 2; x < grid[y].length; x++)
				grid[y][x] = new Box(getX() + (x * Box.SCL), getY() + (y * Box.SCL));

		// Bottom left
		for (int y = grid.length - 2; y < grid.length; y++)
			for (int x = 0; x < 2; x++)
				grid[y][x] = new Box(getX() + (x * Box.SCL), getY() + (y * Box.SCL));
				
		// Bottom right
		for (int y = grid.length - 2; y < grid.length; y++)
			for (int x = grid[y].length - 2; x < grid[y].length; x++)
				grid[y][x] = new Box(getX() + (x * Box.SCL), getY() + (y * Box.SCL));
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------- DRAWING

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		g2d.setColor(Color.BLACK);
		g2d.setFont(HLabel.FONT.deriveFont(Box.SCL * 0.6f));

		drawHighlightedBoxes(g2d);

		g2d.setColor(Color.BLACK);

		// Drawing border
		drawBorderTB(g2d);
		drawBorderLR(g2d);

		// Drawing corners
		grid[1][1].content.forEach(g2d::draw);
		grid[1][grid[1].length - 2].content.forEach(g2d::draw);
		grid[grid.length - 2][1].content.forEach(g2d::draw);
		grid[grid.length - 2][grid[grid.length - 2].length - 2].content.forEach(g2d::draw);

		// Drawing beam and mirrors
		drawBeam(g2d);
		drawMirrors(g2d);

		Toolkit.getDefaultToolkit().sync();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------- DRAWING METHODS

	private void drawHighlightedBoxes(Graphics2D g2d) {
		for (var row : grid) {
			for (var b : row) {
				if (b.highlight == null) continue;

				g2d.setColor(b.highlight);
				g2d.fill(b.hlArea);
			}
		}
	}

	// Draws top and bottom border (characters and edge lines)
	private void drawBorderTB(Graphics2D g2d) {
		// Top characters
		for (int x = 2; x < grid[0].length - 2; x++) {
			Box b = grid[0][x];
			g2d.drawString(b.value + "", b.x + Math.round(Box.SCL / 4), b.y + Math.round(Box.SCL * 1.2));
			b.content.forEach(g2d::draw);
		}

		// Top edge
		for (int x = 2; x < grid[1].length - 2; x++) {
			Box b = grid[1][x];

			for (var c : b.content) {
				if (b.isLineHalfStep(c)) {
					g2d.setColor(beamColor);
					g2d.draw(c);
					g2d.setColor(Color.BLACK);
				} else {
					g2d.draw(c);
				}
			}
		}

		// Bottom edge
		for (int x = 2; x < grid[grid.length - 2].length - 2; x++) {
			Box b = grid[grid[grid.length - 2].length - 2][x];

			for (var c : b.content) {
				if (b.isLineHalfStep(c)) {
					g2d.setColor(beamColor);
					g2d.draw(c);
					g2d.setColor(Color.BLACK);
				} else {
					g2d.draw(c);
				}
			}
		}

		// Bottom characters
		for (int x = 2; x < grid[grid.length - 1].length - 2; x++) {
			Box b = grid[grid.length - 1][x];
			g2d.drawString(
				b.value + "",
				b.x + Math.round(Box.SCL / 4),
				b.y - Math.round(Box.SCL / 1.35) + Box.SCL
			);
			b.content.forEach(g2d::draw);
		}
	}

	// Draws left and right border (characters and edge lines)
	private void drawBorderLR(Graphics2D g2d) {
		// Left characters
		for (int y = 2; y < grid.length - 2; y++) {
			Box b = grid[y][0];
			g2d.drawString(
				b.value + "",
				b.x + Math.round(Box.SCL / 1.35),
				b.y + Math.round(Box.SCL / 1.35)
			);
			b.content.forEach(g2d::draw);
		}

		// Left edge
		for (int y = 2; y < grid.length - 2; y++) {
			Box b = grid[y][1];

			for (var c : b.content) {
				if (b.isLineHalfStep(c)) {
					g2d.setColor(beamColor);
					g2d.draw(c);
					g2d.setColor(Color.BLACK);
				} else {
					g2d.draw(c);
				}
			}
		}

		// Right edge
		for (int y = 2; y < grid.length - 2; y++) {
			Box b = grid[y][grid.length - 2];
			
			for (var c : b.content) {
				if (b.isLineHalfStep(c)) {
					g2d.setColor(beamColor);
					g2d.draw(c);
					g2d.setColor(Color.BLACK);
				} else {
					g2d.draw(c);
				}
			}
		}

		// Right characters
		for (int y = 2; y < grid.length - 2; y++) {
			Box b = grid[y][grid[y].length - 1];
			g2d.drawString(
				b.value + "",
				b.x - Math.round(Box.SCL / 4),
				b.y + Math.round(Box.SCL / 1.4)
			);
			b.content.forEach(g2d::draw);
		}
	}

	private void drawBeam(Graphics2D g2d) {
		g2d.setColor(beamColor);
		for (int y = 2; y < grid.length - 2; y++) {
			for (int x = 2; x < grid[y].length - 2; x++) {
				Box b = grid[y][x];

				if (b.numParts <= 1 && b.value != ' ') continue;

				for (int i = b.value == ' ' ? 0 : 1; i < b.content.size(); i++) {
					g2d.draw(b.content.get(i));
				}
			}
		}
	}

	private void drawMirrors(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);

		for (int y = 2; y < grid.length - 2; y++) {
			for (int x = 2; x < grid[y].length - 2; x++) {
				Box b = grid[y][x];

				if (b.numParts == 0 || b.value == ' ') continue;

				g2d.draw(b.content.get(0));
			}
		}
	}

	// Removes the content that makes up the beam from the respective boxes
	public void clearBeam() {
		for (var row : grid) {
			for (var b : row) {
				if (b == null) continue;
				b.revert();
			}
		}

		addCorners();
		addEdgeStyleLines();
		addCharStyleLines();
	}
}