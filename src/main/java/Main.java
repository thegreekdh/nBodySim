
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main extends JPanel {


    Body[] bodies = new Body[23];
    int bodiesToSim = 23;
    int xOffset = 550;
    int yOffset = 600;
    int yOffsetSideView = yOffset;
    int midPoint = 1100;
    boolean followingMode = false;
    double scale = 500000000.0;
    int bodyToFollow = 0;
    int timeStep = 5;  // seconds
    boolean exitSignal = false;
    int totalThreads = 5;
    boolean paused = true;
    DecimalFormat d = new DecimalFormat("#.##E00");
    CyclicBarrier myBarrier = new CyclicBarrier(totalThreads + 1);
    CyclicBarrier drawBarrier = new CyclicBarrier(totalThreads + 1);

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

            int temp1 = (int) (bodies[i].xPos / scale);
            int temp2 = -(int) (bodies[i].yPos / scale);
            temp1 += xOffset;
            temp2 += yOffset;
            temp1 -= (bodies[i].radius / 2);
            temp2 -= (bodies[i].radius / 2);

            g.setColor(bodies[i].color);
            if (temp1 < 0 || temp1 > 1050 || temp2 < 0 || temp2 > 1050)
                continue;
            g.fillOval(temp1, temp2,
                    bodies[i].radius, bodies[i].radius);

            temp1 += (bodies[i].radius / 2);
            temp2 += (bodies[i].radius / 2);
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
            if (followingMode)
                temp2 += yOffsetSideView + (int) (bodies[bodyToFollow].zPos / scale);
            else
                temp2 += yOffsetSideView;
            if (temp1 < 1150 || temp1 > 2200 || temp2 < 0 || temp2 > 1050)
                continue;
            g.drawLine(temp1, yOffsetSideView, temp1, temp2);
            temp1 -= (bodies[i].radius / 2);
            temp2 -= (bodies[i].radius / 2);

            g.fillOval(temp1, temp2,
                    bodies[i].radius, bodies[i].radius);

        }

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("Mode : " + (followingMode ? "Following body" : "Free"), 10, 200);
        g.drawString("Scale : " + d.format(scale), 10, 225);
        g.drawString("Time step : " + timeStep, 10, 250);
        g.drawString("Bodies : " + bodiesToSim, 10, 275);
        g.drawString("Threads : " + totalThreads, 10, 300);
        g.drawString("X offset : " + xOffset, 10, 325);
        g.drawString("Y offset : " + yOffset, 10, 350);


        if (followingMode) {
            g.drawString("Name : " + bodies[bodyToFollow].name, 10, 600);
            g.drawString("Mass : " + d.format(bodies[bodyToFollow].mass), 10, 625);
            g.drawString("X pos : " + d.format(bodies[bodyToFollow].xPos), 10, 650);
            g.drawString("Y pos : " + d.format(bodies[bodyToFollow].yPos), 10, 675);
            g.drawString("Z pos : " + d.format(bodies[bodyToFollow].zPos), 10, 700);
            g.drawString("X vel : " + d.format(bodies[bodyToFollow].xVelocity), 10, 725);
            g.drawString("Y vel : " + d.format(bodies[bodyToFollow].yVelocity), 10, 750);
            g.drawString("Z vel : " + d.format(bodies[bodyToFollow].zVelocity), 10, 775);

        }
    }

    private class MyThread extends Thread {
        int set;
        public MyThread(int set) {
            this.set = set;
        }
        @SuppressWarnings("InfiniteLoopStatement")
        @Override
        public void run() {
            while (true) {
                for (int i = bodiesToSim / (totalThreads - 1) * set; i < bodiesToSim / (totalThreads - 1) * (set + 1); i++)
                    if (i < bodiesToSim)
                        bodies[i].calculateForce(bodies, timeStep);


                try {
                    myBarrier.await();
                    drawBarrier.await();
                } catch (InterruptedException | BrokenBarrierException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    // ephemeris data from https://ssd.jpl.nasa.gov/horizons.cgi feb 26th 2023
    public void start() {
        Body sun = new Body("Sol", 1.989e30,
                -1.343760924609516E+06, -6.047191362649841E+04, 3.179594186041649E+04,
                2.841473940679482E-03, -1.525973169332030E-02, 5.700220793954363E-05, Color.YELLOW, 50);
        Body mercury = new Body("Mercury", 3.3011e23,
                1.711125709127513E+07, -6.479770815640976E+07, -6.951318790816076E+06,
                3.708392700753278E+01, 1.581866756195965E+01, -2.107248961476617E+00, Color.GRAY, 10);
        Body venus = new Body("Venus", 4.8675e24,
                6.858873399242932E+07, 8.231814517972817E+07, -2.872485832412515E+06,
                -2.680372526403628E+01, 2.249962496572153E+01, 1.855975221710441E+00, Color.WHITE, 20);
        Body earth = new Body("Earth", 5.972e24,
                -1.374578929623432E+08, 5.828629402214251E+07, 2.938774468573555E+04,
                -1.220909157713714E+01, -2.751588919426942E+01, 4.882913994546101E-04, Color.GREEN, 25);
        Body mars = new Body("Mars", 6.4171e23,
                -1.017378283695528E+08, 2.209920802322008E+08, 7.127262757147059E+06,
                -2.114100953927668E+01, -7.974960381357101E+00, 3.518835870921877E-01, Color.RED, 15);
        Body jupiter = new Body("Jupiter", 1.8986e27,
                7.055924787247471E+08, 2.204514214682221E+08, -1.670061914610689E+07,
                -4.044486567644700E+00, 1.308417710370551E+01, 3.615302501804774E-02, Color.ORANGE, 40);
        Body saturn = new Body("Saturn", 5.6834e26,
                1.240447749705632E+09, -7.845212423386828E+08, -3.574725983464175E+07,
                4.621403504557823E+00, 8.145608399404992E+00, -3.257302331113943E-01, Color.YELLOW, 35);
        Body uranus = new Body("Uranus", 8.6810e25,
                1.974191936542250E+09, 2.178993208448020E+09, -1.748319367804456E+07,
                -5.096844721068565E+00, 4.255135540231182E+00, 8.161828707303309E-02, Color.CYAN, 30);
        Body neptune = new Body("Neptune", 1.0243e26,
                4.453144242684819E+09, -4.138905088272247E+08, -9.410400143731076E+07,
                4.673996655521420E-01, 5.443818432396268E+00, -1.235090747211511E-01, Color.BLUE, 30);
        Body pluto = new Body("Pluto", 1.303e22,
                2.442548562633492E+09, -4.577248595625036E+09, -2.167356603368096E+08,
                4.948830971912519E+00, 1.383320622318901E+00, -1.588267256009569E+00, Color.DARK_GRAY, 10);
        Body ceres = new Body("Ceres", 9.393e20,
                -3.760270381504925E+08, 3.792730059730145E+07, 7.025117451664409E+07,
                -2.469971454729070E+00, -1.914948577918830E+01, -1.494377118401031E-01, Color.PINK, 10);
        Body moon = new Body("Luna", 7.34767309e22,
                -1.372154672737438E+08, 5.858916861173135E+07, 3.878218492670730E+04,
                -1.295101330709591E+01, -2.683730152708864E+01, 4.882913994546101E-04, Color.WHITE, 5);
        Body io = new Body("Io", 8.931938e22,
                7.059811559222276E+08, 2.206162022719481E+08, -1.668891879840240E+07,
                -1.074892601280871E+01, 2.904066745883379E+01, 4.980292541964140E-01, Color.WHITE, 5);
        Body europa = new Body("Europa", 4.799844e22,
                7.061599609226100E+08, 2.200866843803024E+08, -1.670078199216314E+07,
                3.250463883889849E+00, 2.463168528862401E+01, 6.115150561556053E-01, Color.WHITE, 5);
        Body ganymede = new Body("Ganymede", 1.4819e23,
                7.046707735447637E+08, 2.199065229102390E+08, -1.673422526231553E+07,
                1.512512431266026E+00, 3.745291866738162E+00, -2.426067702312382E-01, Color.WHITE, 5);
        Body callisto = new Body("Callisto", 1.075938e23,
                7.072815217090786E+08, 2.196461586913177E+08, -1.670343493291690E+07,
                -5.241670251298696E-01, 2.054553561221208E+01, 3.177641489852636E-01, Color.WHITE, 5);
        Body titan = new Body("Titan", 1.3452e23,
                1.240178874942972E+09, -7.855682211623263E+08, -3.518056014547974E+07,
                1.007389420466657E+01, 6.966986358379626E+00, -2.612912464657913E-01, Color.WHITE, 5);
        Body triton = new Body("Triton", 2.14e22,
                4.452886603526668E+09, -4.138567419493906E+08, -9.386242675709552E+07,
                2.454327203831407E+00, 9.005865957760395E+00, 1.498215865783683E+00, Color.WHITE, 5);
        Body rhea = new Body("Rhea", 2.306e21,
                1.239948078526074E+09, -7.843569154307170E+08, -3.578823278745639E+07,
                1.987068088689806E+00, 1.116683365834246E+00, 3.611127088087865E+00, Color.WHITE, 5);
        Body iapetus = new Body("Iapetus", 1.805e21,
                1.241236774419622E+09, -7.812211436045376E+08, -3.666961357972354E+07,
                1.485750372643162E+00, 9.100492500327301E+00, 8.660027569193884E-02, Color.WHITE, 5);
        Body eris = new Body("Eris", 1.66e22,
                1.281842755254781E+10, 5.768357509329563E+09, -2.763248013545221E+09,
                -7.636909830733408E-01, 1.508759271203845E+00, 1.611927739257515E+00, Color.LIGHT_GRAY, 10);
        Body charon = new Body("Charon", 1.586e21,
            2.442544097752507E+09, -4.577261948732002E+09 , -2.167492904697700E+08,
            4.792975328491395E+00, 1.297807060967669E+00 , -1.453453013328513E+00, Color.WHITE, 5);
        Body deimos = new Body("Deimos", 1.4762e15,
            -1.017518854619716E+08, 2.210092190168600E+08, 7.134919816656277E+06,
            -2.207181013902220E+01, -8.892239200333885E+00 , 6.968481860412905E-01, Color.WHITE, 5);

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
        bodies[12] = io;
        bodies[13] = europa;
        bodies[14] = ganymede;
        bodies[15] = callisto;
        bodies[16] = titan;
        bodies[17] = triton;
        bodies[18] = rhea;
        bodies[19] = iapetus;
        bodies[20] = eris;
        bodies[21] = charon;
        //bodies[22] = phobos;
        bodies[22] = deimos;


        int cntr = 0;

        setDoubleBuffered(true);
        JFrame f = new JFrame();
        f.add(this);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(2200, 1200);
        f.setVisible(true);

        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
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
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
        im.put(KeyStroke.getKeyStroke("SPACE"), "pause");
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
                scale /= 1.1;
            }
        });
        am.put("zoomOut", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scale *= 1.1;
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
        am.put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitSignal = true;
            }
        });
        am.put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paused = !paused;
            }
        });

        ArrayList<MyThread> threads = new ArrayList<>();
        for (int i = 0; i < totalThreads; i++) {
            threads.add(new MyThread(i));
            threads.get(i).start();
        }

        while (true) {

            if (!paused) {
                try {
                    myBarrier.await();
                } catch (InterruptedException | BrokenBarrierException ex) {
                    ex.printStackTrace();
                }

                for (int i = 0; i < bodiesToSim; i++) {
                    bodies[i].update();
                }

                try {
                    drawBarrier.await();
                } catch (InterruptedException | BrokenBarrierException ex) {
                    ex.printStackTrace();
                }
            }

            if (cntr % 500 == 0) {
                if (followingMode) {
                    int temp1 = (int) (bodies[bodyToFollow].xPos / scale);
                    int temp2 = -(int) (bodies[bodyToFollow].yPos / scale);
                    xOffset = -temp1 + 550;
                    yOffset = -temp2 + 600;
                }

                repaint();
                cntr = 0;
            }

            if (exitSignal)
                break;

            cntr++;

        }

        System.exit(0);


    }
}
