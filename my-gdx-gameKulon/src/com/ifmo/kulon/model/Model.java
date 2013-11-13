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

    private double kFriction =DEFAULT_K_FRICTION;


    public Model() {
        this.oldState = new ArrayList<Point>();
        this.newState = new ArrayList<Point>();
    }

    public Model(List<Point> curState) {
        this.oldState = curState;
        this.newState = new ArrayList<Point>();
        for (Point point : curState) {
            newState.add(new Point(point));
        }
    }


    /*private Vector2 getReflectedVelocity(Vector2 velocity, float a) {
        float x = 2*(a*velocity.y+velocity.x)/(1+a*a) - velocity.x;
        float y = 2*a*x - velocity.y;
        return new Vector2(x,y);
    }*/

   public void render(double timeFrame) {
        for (int i = 0; i < oldState.size(); ++i) {
            Point oldPoint = oldState.get(i);
            Point newPoint = newState.get(i);
            newPoint.setFields(oldPoint);
            Vector acceleration = countAcceleration(newPoint, oldPoint, oldState);
            System.out.println("i="+i+"  " +acceleration);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }

            // Vector acceleration = new Vector(0,0);
            Vector velocity = countVelocity(oldPoint, acceleration, timeFrame);
            Vector moving = countMoving(oldPoint, acceleration, timeFrame);
            setParam(newPoint, velocity, moving);
        }
        swapState();
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
