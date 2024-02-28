import java.awt.*;
import java.awt.geom.*;

public class SolarSystemAnimation extends Frame implements Runnable {
    private int windowHeight;
    private int sunRadius = 55;
    private int planetRadius = 25;
    private int orbitRadius = 200;
    private int centerX = 200;
    private int centerY = 85;
    private double rotationAngle = 0;
    private double orbitAngle = 0;
    private Thread animator;

    SolarSystemAnimation(int height) {
        // Enable closing of the window
        addWindowListener(new MyFinishWindow());
        windowHeight = height;
    }

    public void start() {
        if (animator == null) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void stop() {
        animator = null;
    }

    public void run() {
        long startTime, sleepTime;
        long fps = 10;
        long targetTime = 1000 / fps;

        while (Thread.currentThread() == animator) {
            startTime = System.currentTimeMillis();

            // Update rotation angle
            rotationAngle += Math.PI / 365;

            // Update orbit angle (change to negative to rotate clockwise)
            orbitAngle -= Math.PI / 120;

            // Repaint the window
            repaint();

            // Adjust frame rate
            sleepTime = targetTime - (System.currentTimeMillis() - startTime);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Clear the window
        g2d.clearRect(0, 0, getWidth(), getHeight());

        // Translate to center of window
        g2d.translate(getWidth() / 2, getHeight() / 2);

        // Draw sun (matahari)
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(-sunRadius / 2, centerY - sunRadius / 2, sunRadius, sunRadius);

        // Draw orbit
        g2d.setColor(Color.GRAY);
        g2d.drawOval(-orbitRadius, -orbitRadius, 2 * orbitRadius, 2 * orbitRadius);

        // Draw x-axis
        g2d.setColor(Color.BLACK);
        g2d.drawLine(-orbitRadius, 0, orbitRadius, 0);

        // Draw y-axis
        g2d.drawLine(0, -orbitRadius, 0, orbitRadius);

        // Draw x-axis labels
        for (int i = -orbitRadius; i <= orbitRadius; i += 40) {
            if (i % 40 == 0) {
                g2d.drawString(Integer.toString(i), i, 15);
            }
        }

        // Draw y-axis labels
        for (int i = -orbitRadius; i <= orbitRadius; i += 40) {
            if (i % 40 == 0) {
                g2d.drawString(Integer.toString(-i), -20, i);
            }
        }

        // Calculate planet position
        double planetAngle = orbitAngle;
        int planetX = (int) (orbitRadius * Math.cos(planetAngle));
        int planetY = (int) (orbitRadius * Math.sin(planetAngle));

        // Calculate distance of planet from sun
        double distanceFromSun = Math.sqrt(Math.pow(centerX - planetX, 2) + Math.pow(centerY - planetY, 2));

        // Normalize distance from sun to range [0, 1]
        double normalizedDistance = distanceFromSun / orbitRadius;

        // Calculate brightness (inverse of distance)
        float brightness = 1.0f - (float) normalizedDistance;

        // Ensure brightness is within valid range [0, 1]
        brightness = Math.max(0.0f, Math.min(1.0f, brightness));

        // Set color based on brightness
        Color planetColor;
        if (planetY >= 0) {
            // Berwarna biru muda ketika berada di bawah
            planetColor = new Color(173, 216, 230); // Light Blue
        } else {
            // Gunakan warna abu-abu dengan kecerahan yang dihitung
            int colorValue = (int) (255 * brightness);
            planetColor = new Color(colorValue, colorValue, colorValue);
        }

        // Draw planet
        g2d.setColor(planetColor);
        g2d.fillOval(planetX - planetRadius / 2, planetY - planetRadius / 2, planetRadius, planetRadius);
    }

    public static void main(String[] argv) {
        int height = 400;
        SolarSystemAnimation f = new SolarSystemAnimation(height);
        f.setTitle("Solar System Animation");
        f.setSize(400, height);
        f.setVisible(true);
        f.start();
    }
}
