package com.zhou.bean;

public class Grid implements Comparable {
    public boolean isIgnited;
    public double ignitedTime;
    //    int px, py;
    public double slope; // 坡度
    public double direction; // 坡向
    public double type; //可燃物类型

    public double[] LatAndLon;

    public boolean isIgnited() {
        return isIgnited;
    }

    public double getIgnitedTime() {
        return ignitedTime;
    }

    public double getSlope() {
        return slope;
    }

    public double getDirection() {
        return direction;
    }

    public double getType() {
        return type;
    }

    public double[] getLatAndLon() {
        return LatAndLon;
    }

    public void setIgnited(boolean ignited) {
        isIgnited = ignited;
    }

    public void setIgnitedTime(double ignitedTime) {
        this.ignitedTime = ignitedTime;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setType(double type) {
        this.type = type;
    }

    public void setLatAndLon(double[] latAndLon) {
        LatAndLon = latAndLon;
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        else if (o instanceof Grid) {
            Grid g = (Grid) o;
            if (ignitedTime <= g.ignitedTime) {
                return -1;
            } else {
                return 1;
            }
        } else {
            return -1;
        }
    }
}
