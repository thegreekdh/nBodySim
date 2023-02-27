import java.awt.*;
import java.math.BigInteger;
import java.util.Deque;
import java.util.LinkedList;

public class Body implements Runnable {
    double mass;
    double xPos;
    double yPos;
    double zPos;
    double xVelocity;
    double yVelocity;
    double zVelocity;
    Color color;
    int radius;
    int updateOldPos = 0;
    double tempXPos;
    double tempYPos;
    double tempZPos;
    double tempXVelocity;
    double tempYVelocity;
    double tempZVelocity;

    LinkedList<Double> oldXs;
    LinkedList<Double> oldYs;
    private class VectorForce {
        double magnitude;
        double angle;

        public VectorForce(double magnitude, double angle) {
            this.magnitude = magnitude;
            this.angle = angle;
        }
    }

    private class threeDVector {
        double mag;
        double angle;
        double elev;

        public threeDVector(double mag, double angle, double elev) {
            this.mag = mag;
            this.angle = angle;
            this.elev = elev;
        }
    }

    public Body(double mass, double xPos, double yPos, double zPos, double xVelocity,
                double yVelocity, double zVelocity, Color color, int radius) {
        this.mass = mass;
        this.xPos = xPos *  1000;
        this.yPos = yPos *  1000;
        this.zPos = zPos *  1000;
        this.xVelocity = xVelocity *  1000;
        this.yVelocity = yVelocity *  1000;
        this.zVelocity = zVelocity *  1000;
        this.color = color;
        this.radius = radius;
        tempXPos = 0.0;
        tempYPos = 0.0;
        tempXVelocity = 0;
        tempYVelocity = 0;
        oldXs = new LinkedList<>();
        oldYs = new LinkedList<>();

    }

    public void update() {
        updateOldPos++;
        if (updateOldPos == 1000) {
            updateOldPos = 0;
            oldXs.push(xPos);
            oldYs.push(yPos);
            if (oldXs.size() > 100) {
                oldXs.removeLast();
                oldYs.removeLast();
            }
        }
        xPos = tempXPos;
        yPos = tempYPos;
        zPos = tempZPos;
        xVelocity = tempXVelocity;
        yVelocity = tempYVelocity;
        zVelocity = tempZVelocity;
    }

    public double degToRad(double degrees) {
        return degrees * Math.PI / 180.0;
    }

    public double radToDeg(double radians) {
        return radians * 180.0 / Math.PI;
    }

    public void calculateForce(Body[] bodies, int timeStep) {
        double indForce = 0.0;
        double indAngle = 0.0;
        double indElev = 0.0;
        double dist = 0.0;
        double xDistance = 0.0;
        double yDistance = 0.0;
        double zDistance = 0.0;
        double totalXForce = 0.0;
        double totalYForce = 0.0;
        double totalZForce = 0.0;

        threeDVector[] forces3d = new threeDVector[bodies.length];


//        VectorForce[] forces = new VectorForce[bodies.length];
//
//        // Calculate the forces on this body from all other bodies
//        for (int i = 0; i < bodies.length; i++) {
//            if (bodies[i] != this) {
//                xDistance = bodies[i].xPos - xPos;
//                yDistance = bodies[i].yPos - yPos;
//                dist = xDistance * xDistance + yDistance * yDistance;
//                indForce = bodies[i].mass * mass / dist * 6.67408e-11;
//                indAngle = radToDeg(Math.atan2(yDistance, xDistance));
//                forces[i] = new VectorForce(indForce, indAngle);
//            }
//            else
//                forces[i] = new VectorForce(0.0, 0.0);
//        }

        // Add the forces
        //VectorForce total = new VectorForce(0, 0);
        threeDVector total = new threeDVector(0, 0, 0);
        for (int i = 0; i < forces3d.length; i++) {
            if (bodies[i] != this) {
                xDistance = bodies[i].xPos - xPos;
                yDistance = bodies[i].yPos - yPos;
                zDistance = bodies[i].zPos - zPos;
                dist = xDistance * xDistance + yDistance * yDistance + zDistance * zDistance;
                indForce = bodies[i].mass * mass / dist * 6.67408e-11;
                indAngle = Math.atan2(yDistance, xDistance);
                indElev = Math.atan2(zDistance, Math.sqrt(xDistance * xDistance + yDistance * yDistance));
                forces3d[i] = new threeDVector(indForce, indAngle, indElev);
            }
            else
                forces3d[i] = new threeDVector(0.0, 0.0, 0.0);
        }


        for (int i = 0; i < forces3d.length; i++) {
            totalXForce += forces3d[i].mag * Math.cos(forces3d[i].angle);
            totalYForce += forces3d[i].mag * Math.sin(forces3d[i].angle);
            totalZForce += forces3d[i].mag * Math.sin(forces3d[i].elev);


        }

        total.mag = Math.sqrt(totalXForce * totalXForce + totalYForce * totalYForce + totalZForce * totalZForce);
        total.angle = Math.atan2(totalYForce, totalXForce);
        total.elev = Math.atan2(totalZForce, Math.sqrt(totalXForce * totalXForce + totalYForce * totalYForce));

        double xAccel = total.mag / mass * Math.cos(total.angle);
        double yAccel = total.mag / mass * Math.sin(total.angle);
        double zAccel = total.mag / mass * Math.sin(total.elev);
        // Update temps
        //double accel = total.magnitude / mass;
        tempXVelocity = xVelocity + xAccel * timeStep;
        tempYVelocity = yVelocity + yAccel * timeStep;
        tempZVelocity = zVelocity + zAccel * timeStep;
        tempXPos = xPos + tempXVelocity * timeStep + 0.5 * xAccel * timeStep * timeStep;
        tempYPos = yPos + tempYVelocity * timeStep + 0.5 * yAccel * timeStep * timeStep;
        tempZPos = zPos + tempZVelocity * timeStep + 0.5 * zAccel * timeStep * timeStep;


    }

    public void run() {

    }

}
