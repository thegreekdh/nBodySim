
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
       g.fillRect(0, 0, 2200, 1200);
       g.setColor(Color.WHITE);
       g.fillRect(1098, 0, 4, 1200);
       g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
       g.drawString("Top view", 450, 50);
       g.drawString("Side view", 1550, 50);
        for (int i = 0; i < 5; i++) {




            //g.fillOval(500, 500,
            //        bodies[i].radius, bodies[i].radius);
            int temp1 = (int) (bodies[i].xPos / 500000000.0);
            int temp2 = -(int) (bodies[i].yPos / 500000000.0);
            temp1 += 550;
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
                temp3 += 550;
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
            temp1 += 1650;
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
                -1.343760924609516E+06, -6.047191362649841E+04, 3.179594186041649E+04,
                2.841473940679482E-03, -1.525973169332030E-02, 5.700220793954363E-05, Color.YELLOW, 50);
        Body mercury = new Body(3.3011e23,
                1.711125709127513E+07, -6.479770815640976E+07, -6.951318790816076E+06,
                3.708392700753278E+01, 1.581866756195965E+01, -2.107248961476617E+00, Color.GRAY, 10);
        Body venus = new Body(4.8675e24,
                6.858873399242932E+07, 8.231814517972817E+07, -2.872485832412515E+06,
                -2.680372526403628E+01, 2.249962496572153E+01, 1.855975221710441E+00, Color.WHITE, 20);
        Body earth = new Body(5.972e24,
                -1.374578929623432E+08, 5.828629402214251E+07, 2.938774468573555E+04,
                -1.220909157713714E+01, -2.751588919426942E+01, 4.882913994546101E-04, Color.GREEN, 25);
        Body mars = new Body(6.4171e23,
                -1.017378283695528E+08, 2.209920802322008E+08, 7.127262757147059E+06,
                -2.114100953927668E+01, -7.974960381357101E+00, 3.518835870921877E-01, Color.RED, 15);
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
        f.setSize(2200, 1200);
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
