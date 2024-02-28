import java.awt.*;
import java.awt.geom.*;
import java.util.Date;

public class beatingheart extends Frame {

    public beatingheart() {
        addWindowListener(new MyFinishWindow());
    }

    public static void clearWindow(Graphics2D g) {
        g.setPaint(Color.white);
        g.fill(new Rectangle(0, 0, 600, 400));
        g.setPaint(Color.red);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Define the path along which the heart will move (a simple line)
        Path2D.Double path = new Path2D.Double();
        path.moveTo(50, 200);
        path.lineTo(550, 200);

        // Create a heart shape
        Shape heart = createHeart(100, new Point2D.Double(0, 0));

        // Number of steps for the heart animation
        int steps = 200;

        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps; // Interpolation parameter

            // Get the point on the path based on interpolation parameter
            Point2D.Double point = interpolatePath(path, t);

            // Translate the heart shape to the current position on the path
            AffineTransform transform = AffineTransform.getTranslateInstance(point.getX(), point.getY());

            // Apply a scaling transform to make the heart appear to beat
            double scale = Math.abs(Math.sin(t * Math.PI * 2)); // Sine function to create a pulsating effect
            transform.scale(scale, scale);

            // Clear the window
            clearWindow(g2d);

            // Draw the heart
            if (i % 20 < 10) { // Blink effect
                g2d.fill(transform.createTransformedShape(heart));
            }

            // Wait a short time before drawing the next frame
            sustain(50);
        }
    }

    // Method to create a heart shape
    private Shape createHeart(double size, Point2D.Double center) {
        Path2D.Double path = new Path2D.Double();

        path.moveTo(center.getX(), center.getY() - size);
        path.curveTo(center.getX() + 2 * size, center.getY() - size,
                center.getX() + 2 * size, center.getY() + size / 2,
                center.getX(), center.getY() + 2 * size);
        path.curveTo(center.getX() - 2 * size, center.getY() + size / 2,
                center.getX() - 2 * size, center.getY() - size,
                center.getX(), center.getY() - size);

        return path;
    }

    private Point2D.Double interpolatePath(Path2D.Double path, double t) {
        Path2D.Double clonePath = (Path2D.Double) path.clone();
        PathIterator pathIterator = clonePath.getPathIterator(null);
        double[] coords = new double[6];
        double[] prevCoords = new double[2];
        double length = 0;
        double targetLength = pathIterator.currentSegment(coords);

        while (!pathIterator.isDone()) {
            int type = pathIterator.currentSegment(coords);
            if (type == PathIterator.SEG_MOVETO) {
                prevCoords[0] = coords[0];
                prevCoords[1] = coords[1];
            } else if (type == PathIterator.SEG_LINETO) {
                double segmentLength = Math.hypot(coords[0] - prevCoords[0], coords[1] - prevCoords[1]);
                length += segmentLength;
                if (length >= targetLength * t) {
                    double ratio = (length - targetLength * t) / segmentLength;
                    double x = prevCoords[0] + ratio * (coords[0] - prevCoords[0]);
                    double y = prevCoords[1] + ratio * (coords[1] - prevCoords[1]);
                    return new Point2D.Double(x, y);
                }
                prevCoords[0] = coords[0];
                prevCoords[1] = coords[1];
            }
            pathIterator.next();
        }
        return new Point2D.Double(prevCoords[0], prevCoords[1]);
    }


    // Method for a delay
    public static void sustain(long t) {
        long finish = (new Date()).getTime() + t;
        while ((new Date()).getTime() < finish) {
        }
    }

    public static void main(String[] argv) {
        beatingheart f = new beatingheart();
        f.setTitle("Beating Heart Animation");
        f.setSize(600, 400);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }
}