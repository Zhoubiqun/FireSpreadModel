package com.zhou.bean;

/**
 * @author zbq
 * @date 2022/9/2 15:33
 */
public class Grid {

    /**
     * 坡度角，分别表示z轴到xoy平面的tan，xoy平面的角度
     */
    private final Double[] slope;
    /**
     * 栅格大小
     */
    private final Double[] size;
    /**
     * 过火时间，即被烧到的时间
     */
    private Double time;
    /**
     * 蔓延速度，从正东方向开始，按顺时针方向标记为0-7
     */
    private Double[] velocities;
    /**
     * 坐标
     */
    private Integer[] coordinate;


    public Grid(Double[] slope,Double[] size,Integer[] coordinate){
        this.slope = slope;
        this.size = size;
        this.coordinate = coordinate;
        this.velocities = new Double[8];

        //八个方向的角度
        //规定正东方向为0度，按顺时针
        int[] dx = new int[]{-1,-1,0,1,1,1,0,-1};
        int[] dy = new int[]{0,1,1,1,0,-1,-1,-1};
        int[] radios = new int[]{0,45,90,135,180,225,270,335};
        //计算蔓延速度
        for(int i=0;i<8;i++){
            double forwardDirection = Math.atan2(dx[i], dy[i]) - Math.PI / 2;
            velocities[i] = this.R(forwardDirection,isUpSlope(radios[i]),0,0);
        }
    }

    public Grid(Grid grid){
        this.slope = grid.slope;
        this.size = grid.size;
        this.velocities = grid.velocities;
        this.time = grid.time;
        this.coordinate = grid.coordinate;
    }

    public Double[] getSlope() {
        return slope;
    }

    public Double[] getSize() {
        return size;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Double[] getVelocities() {
        return velocities;
    }

    public void setVelocities(Double[] velocities) {
        this.velocities = velocities;
    }

    public Integer[] getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Integer[] coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * 蔓延速度计算公式
     * @param vdd 计算方向
     * @param flag 上下坡
     * @return 蔓延速度
     */
    public Double R(double vdd,int flag,int wdd,float wsp){
        double R  = 0.0;
        //风作用项
        double ka = Math.exp(0.1783 * wsp * Math.cos(Math.toRadians(vdd - wdd)));
        //坡度作用项
        double kb = Math.exp(flag * 3.533 * Math.pow(this.slope[0], 1.2));
        //胸径作用项
        double kc = 1;
        //最小湿度作用项
        double kd = 1;
        R = 0.6 * ka * kb * kc * kd;
        return  R;
    }

    public int isUpSlope(double radio){
        if(this.slope[1]>=90&&this.slope[1]<=270){//二三象限
            return radio>=this.slope[1]-90&&radio<=this.slope[1]+90?1:-1;
        }else if(this.slope[1]>270){//第四象限
            return (radio>=this.slope[1]-90&&radio<=360)||((radio>=0&&radio<=this.slope[1]-270))?1:-1;
        }else if(this.slope[1]<90){//第一象限
            return (radio>=0&&radio<=this.slope[1]+90)||(radio>this.slope[1]+270&&radio<=360)?1:-1;
        }
        return 0;
    }
}
