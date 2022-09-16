package com.zhou.bean;

public class Wind {
    double direction;
    double speed;

    public double getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Wind(double direction, double speed) {
        this.direction = direction;
        this.speed = speed;
    }
}
