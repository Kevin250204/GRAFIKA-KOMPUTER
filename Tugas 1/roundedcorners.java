import java.awt.*;
import java.awt.geom.*;

public class roundedcorners extends Frame {
    private int windowHeight;

    roundedcorners(int height) {
        addWindowListener(new MyFinishWindow());
        windowHeight = height;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform yUp = new AffineTransform();
        yUp.setToScale(1, -1);
        AffineTransform translate = new AffineTransform();
        translate.setToTranslation(120, windowHeight - 130);
        yUp.preConcatenate(translate);
        g2d.transform(yUp);
        g2d.setStroke(new BasicStroke(3.0f));
        
        // Generate and draw a rounded rectangle with a radius of 20
        RoundRectangle2D.Double roundedRect = new RoundRectangle2D.Double(-40, -20, 80, 40, 10, 10);
        g2d.draw(roundedRect);

        g2d.setStroke(new BasicStroke(1.0f));
        drawSimpleCoordinateSystem(80, 80, g2d);
    }

    public static void drawSimpleCoordinateSystem(int xmax, int ymax, Graphics2D g2d) {
        int xOffset = -100;
        int yOffset = -100;
        int step = 20;
        String s;
        Font fo = g2d.getFont();
        int fontSize = 13;
        Font fontCoordSys = new Font("serif", Font.PLAIN, fontSize);
        AffineTransform flip = new AffineTransform();
        flip.setToScale(1, -1);
        AffineTransform lift = new AffineTransform();
        lift.setToTranslation(0, fontSize);
        flip.preConcatenate(lift);
        Font fontUpsideDown = fontCoordSys.deriveFont(flip);
        g2d.setFont(fontUpsideDown);
        
        g2d.drawLine(xOffset, 0, xmax, 0);
        for (int i = xOffset + step; i <= xmax; i += step) {
            g2d.drawLine(i, -2, i, 2);
            if (i != 0) {
                g2d.drawString(String.valueOf(i), i - 7, -30);
            }
        }
        
        g2d.drawLine(0, yOffset, 0, ymax);
        for (int i = yOffset + step; i <= ymax; i += step) {
            g2d.drawLine(-2, i, 2, i);
            if (Math.abs(i) > 99) {
                s = "";
            } else {
                s = Math.abs(i) > 9 ? "  " : "    ";
            }
            if (i >= 0) {
                s = s + " ";
            }
            if (i != 0) {
                g2d.drawString(s + String.valueOf(i), -25, i - 20);
            }
        }
        
        g2d.setFont(fo);
    }

    public static void main(String[] argv) {
        int height = 260;
        roundedcorners f = new roundedcorners(height);
        f.setTitle("Example: rounded corners");
        f.setSize(230, height);
        f.setVisible(true);
    }
}
