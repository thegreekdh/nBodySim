
import javax.swing.*;
//import java.awt.*;
import java.awt.*;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Iterator;

public class Main extends JPanel implements Runnable{


    Body[] bodies = new Body[10];
    int timeStep = 20;  // seconds
//    public void render() {
//        StdDraw.clear();
//        for (int i = 0; i < bodies.length; i++) {
//            StdDraw.setPenColor(bodies[i].color);
//            StdDraw.filledCircle(bodies[i].xPos.doubleValue(), bodies[i].yPos.doubleValue(), bodies[i].radius);
//        }
//        StdDraw.show();
//    }

    @Override
    public void paintComponent(Graphics g) {
       g.setColor(Color.BLACK);
       g.fillRect(0, 0, 2000, 1200);
       g.setColor(Color.WHITE);
       g.fillRect(998, 0, 4, 1200);
       g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
       g.drawString("Top view", 400, 50);
       g.drawString("Side view", 1400, 50);
        for (int i = 0; i < 5; i++) {




            //g.fillOval(500, 500,
            //        bodies[i].radius, bodies[i].radius);
            int temp1 = (int) (bodies[i].xPos / 500000000.0);
            int temp2 = -(int) (bodies[i].yPos / 500000000.0);
            temp1 += 500;
            temp2 += 600;
            temp1 -= (int) (bodies[i].radius / 2);
            temp2 -= (int) (bodies[i].radius / 2);

            g.setColor(bodies[i].color);
            g.fillOval(temp1, temp2,
                    bodies[i].radius, bodies[i].radius);

            temp1 += (int) (bodies[i].radius / 2);
            temp2 += (int) (bodies[i].radius / 2);
            Iterator<Double> xIt = bodies[i].oldXs.iterator();
            Iterator<Double> yIt = bodies[i].oldYs.iterator();

            while (xIt.hasNext()) {
                int temp3 = (int) (xIt.next() / 500000000.0);
                int temp4 = -(int) (yIt.next() / 500000000.0);
                temp3 += 500;
                temp4 += 600;
                g.drawLine(temp1, temp2,
                        temp3, temp4);
                temp1 = temp3;
                temp2 = temp4;
            }

        }

        for (int i = 0; i < 5; i++) {
            g.setColor(bodies[i].color);
            int temp1 = (int) (bodies[i].xPos / 500000000.0);
            int temp2 = (int) -(bodies[i].zPos / 500000000.0);
            temp1 += 1500;
            temp2 += 600;
            g.drawLine(temp1, 600, temp1, temp2);
            temp1 -= (int) (bodies[i].radius / 2);
            temp2 -= (int) (bodies[i].radius / 2);

            g.fillOval(temp1, temp2,
                    bodies[i].radius, bodies[i].radius);

        }
    }


    public void start() {
        Body sun = new Body(1.989e30,
                0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Color.YELLOW, 50);
        Body mercury = new Body(3.3011e23,
                4.79e10, 0.0, 0.0,
                0.0, 47360, 0.0, Color.GRAY, 10);
        Body venus = new Body(4.8675e24,
                1.082e11, 0.0, 1e10,
                0.0, 35020, 0.0, Color.WHITE, 20);
        Body earth = new Body(5.972e24,
                1.496e11, 0.0, 0.0,
                0.0, 29780, 0.0, Color.GREEN, 25);
        Body mars = new Body(6.4171e23,
                2.279e11, 0.0, 5e10,
                0.0, 24130, 0.0, Color.RED, 15);
        Body jupiter = new Body(1.8986e27,
                7.785e11, 0.0, 0.0,
                0.0, 13070, 0.0, Color.ORANGE, 40);
        Body saturn = new Body(5.6834e26,
                1.433e12, 0.0, 0.0,
                0.0, 9690, 0.0, Color.YELLOW, 35);
        Body uranus = new Body(8.6810e25,
                2.871e12, 0.0, 0.0,
                0.0, 6810, 0.0, Color.CYAN, 30);
        Body neptune = new Body(1.0243e26,
               4.495e12, 0.0, 0.0,
                0.0, 5430, 0.0, Color.BLUE, 30);
        Body pluto = new Body(1.303e22,
                5.913e12, 0.0, 0.0,
                0.0, 4740, 0.0, Color.DARK_GRAY, 10);
//        Body anotherSun = new Body(1.989e30,
//                1e11, 0, 0, 40000, Color.PINK, 50);
        //Body ceres = new Body(9.393e20,
        //        2.769e11, 0.0,
        //        0.0, 21000, Color.MAGENTA, 5);





        // generate some random planets for me
//        for (int i = 0; i < 100; i++) {
//            double mass = Math.random() * 1e30;
//            double xPos = Math.random() * 3e11;
//            double yPos = Math.random() * 3e11;
//            double xVel = Math.random() * 1e4;
//            double yVel = Math.random() * 1e4;
//            Color color = new Color((int) (Math.random() * 0x1000000));
//            int radius = (int) (Math.random() * 50);
//            bodies[i] = new Body(mass, xPos, yPos, xVel, yVel, color, radius);
//        }
        bodies[0] = sun;
        bodies[1] = mercury;
        bodies[2] = venus;
        bodies[3] = earth;
        bodies[4] = mars;
        bodies[5] = jupiter;
        bodies[6] = saturn;
        bodies[7] = uranus;
        bodies[8] = neptune;
        bodies[9] = pluto;
        //bodies[10] = ceres;

        int cntr = 0;
        //int dayCntr = 0;
        //setBackground(Color.BLACK);

        setDoubleBuffered(true);
        JFrame f = new JFrame();
        f.add(this);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(2000, 1200);
        f.setVisible(true);
        //new Thread(this).start();
        while (true) {
            for (int i = 0; i < bodies.length; i++) {
                bodies[i].calculateForce(bodies, timeStep);
            }
            for (int i = 0; i < bodies.length; i++) {
                bodies[i].update();
            }
            if (cntr % 500 == 0) {
//                //dayCntr++;
//                //System.out.println("Merc: " + dayCntr + ": " + mercury.xPos + ", " + mercury.yPos + ", "
//                //        + mercury.xVelocity + ", " + mercury.yVelocity);
                repaint();
//                cntr =0;
            }

            cntr++;
        }

    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
