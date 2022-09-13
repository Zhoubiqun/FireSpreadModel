package com.zhou;

import com.zhou.utils.TiffTransform;
import org.geotools.coverage.grid.GridCoverage2D;
import org.opengis.coverage.Coverage;
import org.opengis.geometry.Envelope;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;

public class MapModel {
    int width;
    int height;
    double unitLength = 1;
    Grid[][] map;

    public MapModel(int width, int height, double unitLength) {
        this.width = width;
        this.height = height;
        this.unitLength = unitLength;
        map = new Grid[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = new Grid();
            }
        }
    }

    public MapModel(String slopePath, String typePath, String directionPath) throws IOException {
        GridCoverage2D coverage = TiffTransform.readTiff(slopePath);
        RenderedImage renderedImage = coverage.getRenderedImage();
        Raster raster = renderedImage.getData();

        width = renderedImage.getWidth();
        height = renderedImage.getHeight();
        map = new Grid[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = new Grid();
            }
        }


        for (int i = 1; i < height; i++) {
            for (int j = 1; j < width - 1; j++) {
                map[i][j].slope = raster.getSampleFloat(i, j, 0);
            }
        }

        coverage = TiffTransform.readTiff(typePath);
        raster = coverage.getRenderedImage().getData();
        for (int i = 1; i < height; i++) {
            for (int j = 1; j < width - 1; j++) {
                map[i][j].type = raster.getSampleFloat(i, j, 0);
            }
        }

//        coverage = TiffTransform.readTiff(typePath);
//        raster = coverage.getRenderedImage().getData();
//        for (int i = 1; i < height; i++) {
//            for (int j = 1; j < width - 1; j++) {
//                map[i][j].direction = raster.getSampleFloat(i, j, 0);
//            }
//        }
    }

    public void ignite(int x, int y, double t) {
        map[x][y].isIgnited = true;
        map[x][y].ignitedTime = t;
    }

    public void init() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j].isIgnited = false;
                map[i][j].ignitedTime = -1.0f;
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
    double type; //可燃物类型

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