
import javax.swing.*;
//import java.awt.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Iterator;

public class Main extends JPanel implements Runnable{


    Body[] bodies = new Body[12];
    int bodiesToSim = 12;
    int xOffset = 550;
    int yOffset = 600;
    int yOffsetSideView = yOffset;
    int midPoint = 1100;
    boolean followingMode = false;
    double scale = 500000000.0;
    int bodyToFollow = 0;
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
        g.fillRect(midPoint - 2, 0, 4, 1200);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g.drawString("Top view", 450, 50);
        g.drawString("Side view", 1550, 50);
        for (int i = 0; i < bodiesToSim; i++) {




            //g.fillOval(500, 500,
            //        bodies[i].radius, bodies[i].radius);
            int temp1 = (int) (bodies[i].xPos / scale);
            int temp2 = -(int) (bodies[i].yPos / scale);
            temp1 += xOffset;
            temp2 += yOffset;
            temp1 -= (int) (bodies[i].radius / 2);
            temp2 -= (int) (bodies[i].radius / 2);

            g.setColor(bodies[i].color);
            if (temp1 < 0 || temp1 > 1050 || temp2 < 0 || temp2 > 1050)
                continue;
            g.fillOval(temp1, temp2,
                    bodies[i].radius, bodies[i].radius);

            temp1 += (int) (bodies[i].radius / 2);
            temp2 += (int) (bodies[i].radius / 2);
            Iterator<Double> xIt = bodies[i].oldXs.iterator();
            Iterator<Double> yIt = bodies[i].oldYs.iterator();

            while (xIt.hasNext()) {
                int temp3 = (int) (xIt.next() / scale);
                int temp4 = -(int) (yIt.next() / scale);
                temp3 += xOffset;
                temp4 += yOffset;
                if (temp1 < 0 || temp1 > 1050 || temp2 < 0 || temp2 > 1050)
                    break;
                g.drawLine(temp1, temp2,
                        temp3, temp4);
                temp1 = temp3;
                temp2 = temp4;
            }

        }

        for (int i = 0; i < bodiesToSim; i++) {
            g.setColor(bodies[i].color);
            int temp1 = (int) (bodies[i].xPos / scale);
            int temp2 = (int) -(bodies[i].zPos / scale);
            temp1 += xOffset + midPoint;
            temp2 += yOffsetSideView;
            if (temp1 < 1150 || temp1 > 2200 || temp2 < 0 || temp2 > 1050)
                continue;
            g.drawLine(temp1, yOffsetSideView, temp1, temp2);
            temp1 -= (int) (bodies[i].radius / 2);
            temp2 -= (int) (bodies[i].radius / 2);

            g.fillOval(temp1, temp2,
                    bodies[i].radius, bodies[i].radius);

        }
    }

    // ephemeris data from https://ssd.jpl.nasa.gov/horizons.cgi feb 26th 2023
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
                7.055924787247471E+08, 2.204514214682221E+08, -1.670061914610689E+07,
                -4.044486567644700E+00, 1.308417710370551E+01, 3.615302501804774E-02, Color.ORANGE, 40);
        Body saturn = new Body(5.6834e26,
                1.240447749705632E+09, -7.845212423386828E+08, -3.574725983464175E+07,
                4.621403504557823E+00, 8.145608399404992E+00, -3.257302331113943E-01, Color.YELLOW, 35);
        Body uranus = new Body(8.6810e25,
                1.974191936542250E+09, 2.178993208448020E+09, -1.748319367804456E+07,
                -5.096844721068565E+00, 4.255135540231182E+00, 8.161828707303309E-02, Color.CYAN, 30);
        Body neptune = new Body(1.0243e26,
                4.453144242684819E+09, -4.138905088272247E+08, -9.410400143731076E+07,
                4.673996655521420E-01, 5.443818432396268E+00, -1.235090747211511E-01, Color.BLUE, 30);
        Body pluto = new Body(1.303e22,
                2.442548562633492E+09, -4.577248595625036E+09, -2.167356603368096E+08,
                4.948830971912519E+00, 1.383320622318901E+00, -1.588267256009569E+00, Color.DARK_GRAY, 10);
//        Body anotherSun = new Body(1.989e30,
//                1e11, 0, 0, 40000, Color.PINK, 50);
        Body ceres = new Body(9.393e20,
                -3.760270381504925E+08, 3.792730059730145E+07, 7.025117451664409E+07,
                -2.469971454729070E+00, -1.914948577918830E+01, -1.494377118401031E-01, Color.PINK, 10);
        Body moon = new Body(7.34767309e22,
                -1.372154672737438E+08, 5.858916861173135E+07, 3.878218492670730E+04,
                -1.295101330709591E+01, -2.683730152708864E+01, 4.882913994546101E-04, Color.WHITE, 5);





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
        bodies[4] = moon;
        bodies[5] = mars;
        bodies[6] = ceres;
        bodies[7] = jupiter;
        bodies[8] = saturn;
        bodies[9] = uranus;
        bodies[10] = neptune;
        bodies[11] = pluto;


        int cntr = 0;
        //int dayCntr = 0;
        //setBackground(Color.BLACK);

        setDoubleBuffered(true);
        JFrame f = new JFrame();
        f.add(this);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(2200, 1200);
        f.setVisible(true);

        InputMap im = getInputMap(WHEN_FOCUSED);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("UP"), "up");
        im.put(KeyStroke.getKeyStroke("DOWN"), "down");
        im.put(KeyStroke.getKeyStroke("LEFT"), "left");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        im.put(KeyStroke.getKeyStroke("Z"), "zoomIn");
        im.put(KeyStroke.getKeyStroke("X"), "zoomOut");
        im.put(KeyStroke.getKeyStroke("F"), "speedUp");
        im.put(KeyStroke.getKeyStroke("S"), "speedDown");
        im.put(KeyStroke.getKeyStroke("B"), "snapToBody");
        am.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yOffset += 100;
            }
        });
        am.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yOffset -= 100;
            }
        });
        am.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (followingMode)
                    if (bodyToFollow == 0)
                        bodyToFollow = bodiesToSim - 1;
                    else
                        bodyToFollow--;

                else
                    xOffset += 100;
            }
        });
        am.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (followingMode)
                    if (bodyToFollow == bodiesToSim - 1)
                        bodyToFollow = 0;
                    else
                        bodyToFollow++;
                else
                    xOffset -= 100;
            }
        });
        am.put("zoomIn", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scale *= 1.1;
            }
        });
        am.put("zoomOut", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scale /= 1.1;
            }
        });
        am.put("speedUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeStep++;
            }
        });
        am.put("speedDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeStep > 1)
                    timeStep--;
            }
        });
        am.put("snapToBody", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                followingMode = !followingMode;
            }
        });

        //new Thread(this).start();
        while (true) {
            for (int i = 0; i < bodiesToSim; i++) {
                bodies[i].calculateForce(bodies, timeStep);
            }
            for (int i = 0; i < bodiesToSim; i++) {
                bodies[i].update();
            }
            if (cntr % 500 == 0) {
//                //dayCntr++;
//                //System.out.println("Merc: " + dayCntr + ": " + mercury.xPos + ", " + mercury.yPos + ", "
//                //        + mercury.xVelocity + ", " + mercury.yVelocity);
                if (followingMode) {
                    int temp1 = (int) (bodies[bodyToFollow].xPos / scale);
                    int temp2 = -(int) (bodies[bodyToFollow].yPos / scale);
                    xOffset = -temp1 + 550;
                    yOffset = -temp2 + 600;
                }

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
