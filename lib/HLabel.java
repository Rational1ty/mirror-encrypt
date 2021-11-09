package lib;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import javax.swing.JLabel;

// A JLabel with highlighting capabilities
public class HLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	public static final Font FONT = new Font("Arial", Font.BOLD, 20);

	protected String foreLabel;
	protected Color hlColor;
	protected int hlBegin, hlEnd;

	public HLabel() {
		this("");
	}

	public HLabel(String text) {
		this(text, "");
	}

	public HLabel(String text, String fl) {
		super(fl + text);
		foreLabel = fl;
		init();
	}

	public HLabel(String text, int horizontalAlignment) {
		this(text, "", horizontalAlignment);
	}

	public HLabel(String text, String fl, int horizontalAlignment) {
		super(fl + text, horizontalAlignment);
		foreLabel = fl;
		init();
	}

	protected void init() {
		hlColor = null;
		hlBegin = -1;
		hlEnd = -1;
		setFont(FONT);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setFont(getFont());

		if (hlColor != null && hlBegin != -1 && hlEnd != -1) {
			FontMetrics fm = g2d.getFontMetrics();

			int textWidth = fm.stringWidth(getText());
			int textBeforeWidth = fm.stringWidth(getText().substring(0, hlBegin));
			int width = fm.stringWidth(getText().substring(hlBegin, hlEnd));
			int height = fm.getAscent();
			int x = ((getWidth() - textWidth) / 2) + textBeforeWidth;
			int y = (getHeight() - height) / 2;

			g2d.setColor(hlColor);
			g2d.fillRect(x, y, width, height);
		}

		super.paintComponent(g);

		Toolkit.getDefaultToolkit().sync();
	}

	public void append(String text) {
		setText(getText() + text);
	}

	public void setHighlight(int begin, int end, Color c) {
		hlColor = c;
		hlBegin = begin;
		hlEnd = end;
		repaint();
	}

	public void removeHighlight() {
		hlColor = null;
		hlBegin = -1;
		hlEnd = -1;
		repaint();
	}

	public void setForeLabel(String fl) {
		foreLabel = fl;
	}

	public String getForeLabel() {
		return foreLabel;
	}
}