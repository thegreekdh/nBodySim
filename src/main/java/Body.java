import java.awt.*;
import java.math.BigInteger;
import java.util.Deque;
import java.util.LinkedList;

public class Body implements Runnable {
    double mass;
    double xPos;
    double yPos;
    double xVelocity;
    double yVelocity;
    Color color;
    int radius;
    int updateOldPos = 0;
    double tempXPos;
    double tempYPos;
    double tempXVelocity;
    double tempYVelocity;

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

    public Body(double mass, double xPos, double yPos, double xVelocity,
                double yVelocity, Color color, int radius) {
        this.mass = mass;
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
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
        xVelocity = tempXVelocity;
        yVelocity = tempYVelocity;
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
        double dist = 0.0;
        double xDistance = 0.0;
        double yDistance = 0.0;

        VectorForce[] forces = new VectorForce[bodies.length];

        // Calculate the forces on this body from all other bodies
        for (int i = 0; i < bodies.length; i++) {
            if (bodies[i] != this) {
                xDistance = bodies[i].xPos - xPos;
                yDistance = bodies[i].yPos - yPos;
                dist = xDistance * xDistance + yDistance * yDistance;
                indForce = bodies[i].mass * mass / dist * 6.67408e-11;
                indAngle = radToDeg(Math.atan2(yDistance, xDistance));
                forces[i] = new VectorForce(indForce, indAngle);
            }
            else
                forces[i] = new VectorForce(0.0, 0.0);
        }

        // Add the forces
        VectorForce total = new VectorForce(0, 0);
        double totalXForce = 0.0;
        double totalYForce = 0.0;

        for (int i = 0; i < forces.length; i++) {
            totalXForce += forces[i].magnitude * Math.cos(degToRad(forces[i].angle));
            totalYForce += forces[i].magnitude * Math.sin(degToRad(forces[i].angle));
        }

        total.magnitude = Math.sqrt(totalXForce * totalXForce + totalYForce * totalYForce);
        total.angle = radToDeg(Math.atan2(totalYForce, totalXForce));

        double xAccel = total.magnitude / mass * Math.cos(degToRad(total.angle));
        double yAccel = total.magnitude / mass * Math.sin(degToRad(total.angle));
        // Update temps
        //double accel = total.magnitude / mass;
        tempXVelocity = xVelocity + xAccel * timeStep;
        tempYVelocity = yVelocity + yAccel * timeStep;
        tempXPos = xPos + tempXVelocity * timeStep + 0.5 * xAccel * timeStep * timeStep;
        tempYPos = yPos + tempYVelocity * timeStep + 0.5 * yAccel * timeStep * timeStep;


    }

    public void run() {

    }

}
