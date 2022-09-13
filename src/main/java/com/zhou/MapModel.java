package com.zhou;

public class MapModel {
    int width;
    int height;
    double unitLength;
    Grid[][] map;

    public MapModel(int width, int height, double unitLength) {
        this.width = width;
        this.height = height;
        this.unitLength = unitLength;
        map = new Grid[width][height];
    }

    public void ignite(int x, int y, double t) {
        map[x][y].isIgnited = true;
        map[x][y].ignitedTime = t;
    }

    public void init() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = new Grid();
                map[i][j].isIgnited = false;
                map[i][j].ignitedTime = -1.0f;
//                map[i][j].px = i;
//                map[i][j].py = j;
            }
        }
    }
}

class Grid implements Comparable {
    boolean isIgnited;
    double ignitedTime;
    //    int px, py;
    double slope; // 坡度
    double direction; // 坡向

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