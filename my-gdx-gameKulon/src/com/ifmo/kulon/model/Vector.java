package com.ifmo.kulon.model;

/**
 * author Vladiev Aleksey (avladiev2@gmail.com)
 */
public class Vector {

    private double x;
    private double y;


    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    double countModule(){
        return Math.sqrt(x*x + y*y);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
