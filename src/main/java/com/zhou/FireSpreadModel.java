package com.zhou;

import java.util.*;

public class FireSpreadModel {

    private double fuelMoistureContent;
    private FuelType fuelType;
    private MapModel gridMap;
    private WindModel wind;
    private List<int[]> startPoints;
    private double T;

    private double R0;
    private double Ks;


    /**
     * @param fuelMoistureContent 可燃物湿度
     */
    public void setParameter(double fuelMoistureContent, FuelType fuelType, MapModel map, WindModel wind,
                             List<int[]> startPoints, double T) {
        this.fuelMoistureContent = fuelMoistureContent;
        this.R0 = 1.0372 * Math.exp(-0.057 * fuelMoistureContent);
        this.gridMap = map;
        this.wind = wind;
        this.startPoints = startPoints;
        this.T = T;

        this.fuelType = fuelType;
        if (fuelType == null) {
            Ks = 0.0;
        } else {
            Ks = fuelType.getKs();
        }
    }

    public double getR0() {
        return R0;
    }

    public double getKs() {
        return Ks;
    }

    private double getR(double forwardDirection, Grid grid) {
        double Kw = Math.exp(0.1783 * wind.speed * Math.cos(Math.abs(wind.direction - forwardDirection)));
        double Kslot = 1;// 坡度因子，暂时为1
        return R0 * Ks * Kw * Kslot;
    }

    public void run() {
        // init map
        gridMap.init();
        // set startPoints
        TreeMap<Double, HashSet<Long>> timeToSet = new TreeMap<>();

        timeToSet.put(0.0, new HashSet<>());
        for (int[] p : startPoints) {
            timeToSet.get(0.0).add(((long) p[0] * gridMap.width + p[1]));
            gridMap.ignite(p[0], p[1], 0.0);
        }
        // start run
        while (timeToSet.size() > 0) {
            double minTime = timeToSet.firstKey();
            HashSet<Long> list = timeToSet.get(minTime);
            for (Long l : list) {
                int[] point = new int[]{(int) (l / gridMap.width), (int) (l % gridMap.height)};
                Grid grid = gridMap.map[point[0]][point[1]];
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        if (i != 0 || j != 0) {
                            int nx = point[0] + i, ny = point[1] + j;
                            if (nx < 0 || nx >= gridMap.width || ny < 0 || ny > gridMap.height) {
                                continue;
                            }
                            Grid neighborGrid = gridMap.map[nx][ny];
                            double dist = gridMap.unitLength * (Math.sqrt(i * i + j * j)); // 目标点距离
                            double forwardDirection = -Math.atan2(-i, j) + Math.PI / 2; // 扩散方向角, 以(i=-1,j=0)为北向以及0度，则j=cos,-i=-sin
                            double v = getR(forwardDirection, grid);
                            double t = grid.ignitedTime + dist / v;
                            if (t > T) {
                                continue;
                            }
                            if (!neighborGrid.isIgnited || t < neighborGrid.ignitedTime) {
                                long key = (long) nx * gridMap.height + ny;
                                if (timeToSet.containsKey(neighborGrid.ignitedTime)) {
                                    // 从旧的set中删除此neighborGrid
                                    timeToSet.get(neighborGrid.ignitedTime).remove(key);
                                    if (timeToSet.get(neighborGrid.ignitedTime).size() == 0) {
                                        timeToSet.remove(neighborGrid.ignitedTime);
                                    }
                                }
                                // 添加到新的time集合中
                                neighborGrid.isIgnited = true;
                                neighborGrid.ignitedTime = t;
                                if (!timeToSet.containsKey(t)) {
                                    timeToSet.put(t, new HashSet<>());
                                }
                                timeToSet.get(t).add(key);
                            }
                        }
                    }
                }
            }
            // 遍历完成后，此set就可以删除了，不可能会有更小的t被加入这个set中
            timeToSet.remove(minTime);
        }
        char[][] t = new char[gridMap.width][gridMap.height];
        for (int i = 0; i < gridMap.width; i++) {
            for (int j = 0; j < gridMap.height; j++) {
                if (gridMap.map[i][j].isIgnited) {
                    t[i][j] = '-';
                } else {
                    t[i][j] = 'o';
                }
            }
        }
        System.out.println("run finished");
    }
}


