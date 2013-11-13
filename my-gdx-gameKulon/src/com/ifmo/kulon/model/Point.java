package com.ifmo.kulon.model;

/**
 * author Vladiev Aleksey (avladiev2@gmail.com)
 */
public class Point {

    public static final double DEFAULT_M = 1;
    public static final double DEFAULT_Q = 1;

    private double x;
    private double y;

    private double vX;
    private double vY;

    private double m = DEFAULT_M;

    private double q = DEFAULT_Q;


    public Point(double x, double y, double vX, double vY) {
        this.x = x;
        this.y = y;
        this.vX = vX;
        this.vY = vY;
        this.m = m;
        this.q = q;
    }

    public Point() {
    }

    public Point(Point point) {
        this(point.x, point.y, point.vX, point.vY);
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getvX() {
        return vX;
    }

    public void setvX(double vX) {
        this.vX = vX;
    }

    public double getvY() {
        return vY;
    }

    public void setvY(double vY) {
        this.vY = vY;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }

    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public void setFields(Point point) {
        x = point.x;
        y = point.y;

        vX = point.vX;
        vY = point.vY;
    }



}
