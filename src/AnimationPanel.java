package src;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import lib.Box;
import lib.HLabel;

public final class AnimationPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    // Components
    private DrawingPanel dpanel;
    public HLabel mlabel, rlabel;

    private Beam beam;
    private String message;
    public boolean done;

    // For animation
    private int index, counter;
    private int[] frames;
    public boolean tracing;

    public AnimationPanel(String message, Beam beam, String op) {
        setBackground(Color.decode("#ededed"));
        setLayout(new BorderLayout());

        // Initializing JLabels, setting their position in layout, and adding thiem to this panel
        mlabel = new HLabel(message, "Input: ", SwingConstants.CENTER);
        rlabel = new HLabel("", "Output: ", SwingConstants.CENTER);

        mlabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rlabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(mlabel, BorderLayout.NORTH);
        add(rlabel, BorderLayout.SOUTH);

        dpanel = new DrawingPanel(beam);
        add(dpanel, BorderLayout.CENTER);

        this.beam = beam;
        this.message = message;

        done = false;
        index = 0;
        counter = 0;
        frames = new int[2];
        tracing = false;
    }

    public void update() {
        if (index >= message.length()) {
            done = true;
            return;
        }

        // If the current character is not A-Z or a-z
        if (!String.valueOf(message.charAt(index)).matches("[A-Z|a-z]")) {
            mlabel.removeHighlight();
            rlabel.append(message.charAt(index) + "");
            index++;
            return;
        }

        // Initializing a trace
        if (!tracing) {
            startTrace();
            // Quickly paint the first segment of the laser and skip the rest of this animation cycle
            // This is to prevent "jumping" off of the edge when the beam starts tracing a path
            dpanel.repaint();
            return;
        }

        // Only advances the beam every other animation cycle
        // This is necessary because the beam advances in whole-steps, while the drawing is done in half-steps, so the drawing must happen 2x as often
        // It also gets and stores the next two frames so that the second one will be ready to draw on the next animation cycle
        if (counter % 2 == 0) {
            beam.traceStep();
            frames = beam.getAnimationFrames();
        }

        // Adds the current frame to the box at the beam's current position, to be drawn by dpanel.repaint()
        dpanel.grid[beam.row + 2][beam.col + 2].addContent(frames[counter % 2]);
        counter++;

        // If the beam impacts the edge
        if (beam.row == -1 || beam.row == 13 || beam.col == -1 || beam.col == 13) {
            beamImpact();
        }

        dpanel.repaint();
    }

    private void startTrace() {
        dpanel.clearBeam();

        rlabel.removeHighlight();

        beam.initTrace(message.charAt(index));

        // Add the first segment of the laser to the spot on the edge where the beam is starting from
        dpanel.grid[beam.row + 2][beam.col + 2].addContent(
            beam.dir == 'U' ? Box.HALF_TOP :
            beam.dir == 'D' ? Box.HALF_BOTTOM :
            beam.dir == 'L' ? Box.HALF_LEFT :
            beam.dir == 'R' ? Box.HALF_RIGHT : -1
        );
        tracing = true;

        // Highlight the character that the beam is starting from in TRACE_COLOR
        Color color1 = (Color) MirrorConstants.get("trace_color");
        switch (beam.dir) {
            case 'U':
                dpanel.grid[beam.row + 3][beam.col + 2].setHighlight(color1, Box.HALF_TOP);
                dpanel.grid[beam.row + 2][beam.col + 2].setHighlight(color1, Box.HALF_BOTTOM);
                break;
            case 'D':
                dpanel.grid[beam.row + 1][beam.col + 2].setHighlight(color1, Box.HALF_BOTTOM);
                dpanel.grid[beam.row + 2][beam.col + 2].setHighlight(color1, Box.HALF_TOP);
                break;
            case 'L':
                dpanel.grid[beam.row + 2][beam.col + 3].setHighlight(color1, Box.HALF_LEFT);
                dpanel.grid[beam.row + 2][beam.col + 2].setHighlight(color1, Box.HALF_RIGHT);
                break;
            case 'R':
                dpanel.grid[beam.row + 2][beam.col + 1].setHighlight(color1, Box.HALF_RIGHT);
                dpanel.grid[beam.row + 2][beam.col + 2].setHighlight(color1, Box.HALF_LEFT);
                break;
        }

        mlabel.setHighlight(
            index + mlabel.getForeLabel().length(),
            index + mlabel.getForeLabel().length() + 1,
            color1
        );
    }

    private void beamImpact() {
        // Add the final segment of the laser to the spot where the beam impacted
        dpanel.grid[beam.row + 2][beam.col + 2].addContent(
            beam.dir == 'U' ? Box.HALF_BOTTOM :
            beam.dir == 'D' ? Box.HALF_TOP :
            beam.dir == 'L' ? Box.HALF_RIGHT :
            beam.dir == 'R' ? Box.HALF_LEFT : -1
        );

        // Highlight the character that the beam impacted in SUCCESS_COLOR
        Color color2 = (Color) MirrorConstants.get("success_color");
        switch (beam.dir) {
            case 'U':
                dpanel.grid[beam.row + 1][beam.col + 2].setHighlight(color2, Box.HALF_BOTTOM);
                dpanel.grid[beam.row + 2][beam.col + 2].setHighlight(color2, Box.HALF_TOP);
                break;
            case 'D':
                dpanel.grid[beam.row + 3][beam.col + 2].setHighlight(color2, Box.HALF_TOP);
                dpanel.grid[beam.row + 2][beam.col + 2].setHighlight(color2, Box.HALF_BOTTOM);
                break;
            case 'L':
                dpanel.grid[beam.row + 2][beam.col + 1].setHighlight(color2, Box.HALF_RIGHT);
                dpanel.grid[beam.row + 2][beam.col + 2].setHighlight(color2, Box.HALF_LEFT);
                break;
            case 'R':
                dpanel.grid[beam.row + 2][beam.col + 3].setHighlight(color2, Box.HALF_LEFT);
                dpanel.grid[beam.row + 2][beam.col + 2].setHighlight(color2, Box.HALF_RIGHT);
                break;
        }

        // Update the result message/label with the correct character and highlight
        rlabel.append(beam.getChar() + "");
        rlabel.setHighlight(
            index + rlabel.getForeLabel().length(),
            index + rlabel.getForeLabel().length() + 1,
            color2
        );

        // Reset animation vars
        tracing = false;
        index++;
        counter = 0;
    }
}