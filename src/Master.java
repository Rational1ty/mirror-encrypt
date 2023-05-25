package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import lib.HLabel;

public final class Master extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	public static int WIDTH;
	public boolean done;

	private AnimationPanel apanel;
	private Timer timer;
	private int delay;

	public Master(String message, Beam beam, String op, int delay) {
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBackground(Color.decode("#ededed"));

		setTitle(op.toUpperCase().charAt(0) + op.substring(1, op.length()) + " message");
		setIconImage(
			new ImageIcon(System.getProperty("java.class.path") + "/../assets/internal/laser-reflect.jpg")
				.getImage()
		);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		WIDTH = setSize(message);
		done = false;
		
		getContentPane().setLayout(new BorderLayout());

		apanel = new AnimationPanel(message, beam, op);
		add(apanel, SwingConstants.CENTER);
		pack();

		setResizable(false);
		setFocusable(true);
		setLocationRelativeTo(null);
		setVisible(true);
		toFront();
		repaint();

		timer = new Timer(delay, this);
		timer.start();

		this.delay = delay;
	}

	// Determines the width of the window
	private int setSize(String message) {
		FontMetrics fm = getFontMetrics(HLabel.FONT);
		int width = fm.stringWidth(message + "Output: ") + 50;
		
		if (width < 700) {
			width = 700;
		}

		setPreferredSize(new Dimension(width, 700));

		return width;
	}

	public void actionPerformed(ActionEvent e) {
		if (!apanel.tracing) {
			sleep(delay * 4);
		}

		apanel.update();

		if (apanel.done) {
			timer.stop();

			sleep(1000);

			done = true;

			dispose();
		}
	}

	static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ex) {
			// do nothing
		}
	}
}
