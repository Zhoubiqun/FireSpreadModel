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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getUnitLength() {
        return unitLength;
    }

    public Grid[][] getMap() {
        return map;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setUnitLength(double unitLength) {
        this.unitLength = unitLength;
    }

    public void setMap(Grid[][] map) {
        this.map = map;
    }

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
