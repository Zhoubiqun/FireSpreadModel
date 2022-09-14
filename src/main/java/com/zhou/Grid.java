package com.zhou;

public class Grid implements Comparable {
    public boolean isIgnited;
    public double ignitedTime;
    //    int px, py;
    public double slope; // 坡度
    public double direction; // 坡向
    public double type; //可燃物类型

    public double[] LatAndLon;

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
