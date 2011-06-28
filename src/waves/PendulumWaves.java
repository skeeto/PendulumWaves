package waves;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;
import javax.swing.JApplet;
import javax.swing.JComponent;

public class PendulumWaves extends JApplet {
    private static final long serialVersionUID = 2143262286361769613l;
    public static final String TITLE = "Pendulum Waves";

    public static void main(String[] args) {
        /* Fix for poor OpenJDK performance. */
        System.setProperty("sun.java2d.pmoffscreen", "false");

        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new Display().start());
        frame.pack();
        frame.setVisible(true);
    }

    private Display display = new Display();

    @Override
    public void init() {
        add(display);
    }

    @Override
    public void start() {
        display.start();
    }

    @Override
    public void stop() {
        display.stop();
    }

    private static class Display extends JComponent implements Runnable {
        private static final long serialVersionUID = 1874470188451568945l;
        public static final int WIDTH = 800;
        public static final int HEIGHT = 400;
        public static final double YSCALE = 2.25;
        public static final int COUNT = 15;
        public static final int MIN_RATE = 51;
        public static final double SEP = (WIDTH * YSCALE / HEIGHT) / COUNT;
        public static final int SLEEP = 20;
        public static final Shape BALL
            = new Ellipse2D.Double(SEP / -4, SEP / -4, SEP / 2, SEP / 2);

        private final double start;
        private boolean running;

        public Display() {
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            start = System.currentTimeMillis() / 1000.0d;
            new Thread(this).start();
        }

        public Display start() {
            running = true;
            return this;
        }

        public void stop() {
            running = false;
        }

        @Override
        public void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics;
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.translate(0, HEIGHT / 2);
            g.scale(HEIGHT / YSCALE, HEIGHT / YSCALE);
            g.translate(SEP / 2, 0);

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                               RenderingHints.VALUE_STROKE_PURE);
            g.setRenderingHint(RenderingHints.KEY_RENDERING,
                               RenderingHints.VALUE_RENDER_QUALITY);

            double now = System.currentTimeMillis() / 1000.0d;
            AffineTransform at = new AffineTransform();
            g.setColor(Color.WHITE);
            for (int i = 0; i < COUNT; i++) {
                double rate = (i + MIN_RATE) / 60.0;
                double p = Math.sin((now - start) * rate * Math.PI * 2);
                at.setToTranslation(i * SEP, p);
                g.fill(at.createTransformedShape(BALL));
            }
        }

        @Override
        public void run() {
            while (true) {
                if (running) repaint();
                try {
                    Thread.sleep(SLEEP);
                } catch (Exception e) {
                }
            }
        }
    }
}
