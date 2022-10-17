package com.zhou.bean;

public class FirePoint implements Comparable<FirePoint>
{
    private float time;
    private double lon;
    private double lat;
    public FirePoint(float time, double lon, double lat)
    {
        this.time = time;
        this.lon = lon;
        this.lat = lat;
    }

    // getter && setter
    public float getTime() {
        return time;
    }
    public void setTime(float time) {
        this.time = time;
    }
    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public double getlat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }

    //重写Comparable接口的compareTo方法
    @Override
    public int compareTo(FirePoint fp)
    {
        // 根据time升序排列，降序修改相减顺序即可
        return (int)(this.time*1000 - fp.getTime()*1000);
    }
}