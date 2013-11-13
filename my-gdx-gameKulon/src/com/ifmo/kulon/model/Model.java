package com.ifmo.kulon.model;

import java.util.ArrayList;
import java.util.List;

/**
 * author Vladiev Aleksey (avladiev2@gmail.com)
 */
public class Model {

    public static final double K_KULON = 9 * Math.pow(10, 9);

    public static final double DEFAULT_K_FRICTION = Math.pow(10, -8);

    List<Point> oldState;

    List<Point> newState;

    private double kFriction = DEFAULT_K_FRICTION;

    Vector circleCenter;
    double circleRadius;


    public Model(List<Point> curState, Vector circleCenter, double circleRadius) {
        this.oldState = curState;
        this.newState = new ArrayList<Point>();
        for (Point point : curState) {
            newState.add(new Point(point));
        }
        this.circleCenter = circleCenter;
        this.circleRadius = circleRadius;
    }




    public void render(double timeFrame) {
        for (int i = 0; i < oldState.size(); ++i) {
            Point oldPoint = oldState.get(i);
            Point newPoint = newState.get(i);
            newPoint.setFields(oldPoint);
            Vector acceleration = countAcceleration(newPoint, oldPoint, oldState);
            // System.out.println("i=" + i + "  " + acceleration);
            // Vector acceleration = new Vector(0,0);
            Vector velocity = countVelocity(oldPoint, acceleration, timeFrame);
            Vector moving = countMoving(oldPoint, acceleration, timeFrame);
            setParam(newPoint, velocity, moving);
            checkBorder(newPoint, oldPoint);
        }
        swapState();
    }

    private void checkBorder(Point newPoint, Point oldPoint) {
        double i = (newPoint.getX() - circleCenter.getX());
        double j = (newPoint.getY() - circleCenter.getY());
        double r = circleRadius;
        double r2 = r * r;
        if (i * i + j * j > r2) {
            double x1 = oldPoint.getX();
            double y1 = oldPoint.getY();
            double x2 = newPoint.getX();
            double y2 = newPoint.getY();

            double cx = circleCenter.getX();
            double cy = circleCenter.getY();

            double a = (y1 - y2) / (x1 - x2);

            double b = (x1 * y2 - x2 * y1)
                    / (x1 - x2);


            double a2 = a * a;

            double b2 = b * b;
            double d = Math.sqrt(a2 * r2 - a2 * cx * cx - 2 * a * b * cx +
                    2 * a * cx * cy - b2 + 2 * b * cy + r2 - cy * cy);

            double rx1 = (-d - a * b + a * cy + cx) / (a2 + 1);
            double rx2 = (d - a * b + a * cy + cx) / (a2 + 1);

            double ry1 = a * rx1 + b;
            double ry2 = a * rx2 + b;


            if (Double.isInfinite(a)) {
                rx1 = x1;
                rx2 = x2;
                ry1 = Math.sqrt(r2 - (rx1 - cx) * (rx1 - cx)) + cy;
                ry2 = -Math.sqrt(r2 - (rx1 - cx) * (rx1 - cx)) + cy;
            }

            double distance1 = new Vector(rx1 - x2, ry1 - y2).countModule();
            double distance2 = new Vector(rx2 - x2, ry2 - y2).countModule();

            Vector rPoint;
            if (distance1 < distance2) {
                rPoint = new Vector(rx1, ry1);
                System.out.println("X");
            } else {
                rPoint = new Vector(rx2, ry2);
            }

            double vReflectedX = rPoint.getX() - newPoint.getvX() - cx;
            double vReflectedY = rPoint.getY() - newPoint.getvY() - cy;

            double vA = (cy - vReflectedY) / (cx - vReflectedX);
            Vector reflectedPoint = getReflectedVelocity(new Vector(vReflectedX, vReflectedY),a);

            double vx = reflectedPoint.getX() - rPoint.getX() - cx;
            double vy = reflectedPoint.getY() - rPoint.getY() - cy;
            Vector newVelocity = new Vector(vx, vy);

            newPoint.setvX(vx);
            newPoint.setvY(vy);
            newPoint.setX(rPoint.getX());
            newPoint.setY(rPoint.getY());
        }
    }

     private Vector getReflectedVelocity(Vector velocity, double a) {
        double x = 2*(a*velocity.getY()+velocity.getX())/(1+a*a) - velocity.getX();
        double y = 2*a*x - velocity.getX();
        return new Vector(x,y);
    }

    private void setParam(Point newPoint, Vector velocity, Vector moving) {
        newPoint.setvX(velocity.getX());
        newPoint.setvY(velocity.getY());

        newPoint.setX(moving.getX());
        newPoint.setY(moving.getY());
    }

    private Vector countMoving(Point oldPoint, Vector acceleration, double timeFrame) {
        double sX = oldPoint.getX() + oldPoint.getvX() * timeFrame + (acceleration.getX() * timeFrame * timeFrame) / 2;
        double sY = oldPoint.getY() + oldPoint.getvY() * timeFrame + (acceleration.getY() * timeFrame * timeFrame) / 2;
        return new Vector(sX, sY);
    }

    private Vector countVelocity(Point oldPoint, Vector acceleration, double timeFrame) {
        double vX = oldPoint.getvX() + acceleration.getX() * timeFrame;
        double vY = oldPoint.getvY() + acceleration.getY() * timeFrame;
        return new Vector(vX, vY);
    }


    private Vector countAcceleration(Point newPoint, Point oldPoint, List<Point> oldState) {
        Vector fKulon = countForceKulon(newPoint, oldPoint, oldState);
        //Vector fKulon = new Vector(0,0);
        double aX = (-kFriction * oldPoint.getvX() + fKulon.getX()) / newPoint.getM();
        double aY = (-kFriction * oldPoint.getvY() + fKulon.getY()) / newPoint.getM();
        return new Vector(aX, aY);

    }


    private Vector countForceKulon(Point newPoint, Point oldPoint, List<Point> oldState) {
        double fX = 0;
        double fY = 0;

        for (Point point : oldState) {
            if (point == oldPoint) {   //should be ==
                continue;
            }

            Vector distance = countDistance(newPoint, point);
            double moduleDistance = distance.countModule();
            double tmp = point.getQ() / (moduleDistance * moduleDistance);
            fX += tmp * distance.getX() / moduleDistance;
            fY += tmp * distance.getY() / moduleDistance;
        }

        fX *= K_KULON * newPoint.getQ();
        fY *= K_KULON * newPoint.getQ();

        return new Vector(fX, fY);
    }

    private Vector countDistance(Point newPoint, Point point) {
        double dx = newPoint.getX() - point.getX();
        double dy = newPoint.getY() - point.getY();
        return new Vector(dx, dy);
    }


    private void swapState() {
        List<Point> tmp = oldState;
        oldState = newState;
        newState = tmp;

    }

    public List<Point> getOldState() {
        return oldState;
    }
}
