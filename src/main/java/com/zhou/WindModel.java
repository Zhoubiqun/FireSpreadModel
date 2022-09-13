package com.zhou;

public class WindModel {
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

    public WindModel(double direction, double speed) {
        this.direction = direction;
        this.speed = speed;
    }
}
