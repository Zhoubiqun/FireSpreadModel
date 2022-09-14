package com.zhou;

import org.checkerframework.checker.units.qual.K;

import java.util.*;

public class FireSpreadModel {

    private double fuelMoistureContent;
    private FuelType fuelType;
    private MapModel gridMap;
    private WindModel wind;
    private List<int[]> startPoints;
    private double T;

    private double R0;
    private static Map<Integer, Double> KsMap = new HashMap();

    static {
        KsMap.put(1, 0.4);
        KsMap.put(2, 0.7);
        KsMap.put(3, 1.0);
        KsMap.put(4, 1.6);
    }


    /**
     * @param fuelMoistureContent 可燃物湿度
     */
    public void setParameter(double fuelMoistureContent, MapModel map, WindModel wind,
                             List<int[]> startPoints, double T) {
        this.fuelMoistureContent = fuelMoistureContent;
        this.R0 = 1.0372 * Math.exp(-0.057 * fuelMoistureContent);
        this.gridMap = map;
        this.wind = wind;
        this.startPoints = startPoints;
        this.T = T;
    }

    public double getR0() {
        return R0;
    }

    private double getR(double forwardDirection, Grid grid) {
        double Kw = Math.exp(0.1783 * wind.speed * Math.cos(Math.abs(wind.direction - forwardDirection)));
//        double Kslot = 1;// 坡度因子，暂时为1
        double Kslot = Math.exp(3.533 * Math.tan(grid.slope * (-Math.cos(grid.direction - forwardDirection))));
        return R0 * KsMap.getOrDefault((int) grid.type, 0.0) * Kw * Kslot;
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
                int[] point = new int[]{(int) (l / gridMap.width), (int) (l % gridMap.width)};
                Grid grid = gridMap.map[point[0]][point[1]];
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i != 0 || j != 0) {
                            int nx = point[0] + i, ny = point[1] + j;
                            if (nx < 0 || nx >= gridMap.height || ny < 0 || ny >= gridMap.width) {
                                continue;
                            }
                            Grid neighborGrid = gridMap.map[nx][ny];
                            double dist = gridMap.unitLength * (Math.sqrt(i * i + j * j)); // 目标点距离
                            double forwardDirection = -Math.atan2(-i, j) + Math.PI / 2; // 扩散方向角, 以(i=-1,j=0)为北向以及0度，则j=cos,-i=-sin
                            double v = getR(forwardDirection, grid);
                            double t = minTime + dist / v;
                            if (t > T) {
                                continue;
                            }
                            if (!neighborGrid.isIgnited || t < neighborGrid.ignitedTime) {
                                long key = (long) nx * gridMap.width + ny;
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
                                if (t == minTime) {
                                    int b = 0;
                                }
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
        char[][] t = new char[gridMap.height][gridMap.width];
        for (int i = 0; i < gridMap.height; i++) {
            for (int j = 0; j < gridMap.width; j++) {
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


