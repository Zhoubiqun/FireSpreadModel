package com.zhou.bean;

import com.zhou.utils.TiffTransform;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.Envelope2D;
import org.opengis.referencing.operation.TransformException;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class GridMap {
    int width;
    int height;
    double[] unitLength;
    Grid[][] map;
    /**
     * 经纬度范围，分别表示minX，minY，maxX，maxY
     */
    double[] latAndLongRange;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double[] getUnitLength() {
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

    public void setUnitLength(double[] unitLength) {
        this.unitLength = unitLength;
    }

    public void setMap(Grid[][] map) {
        this.map = map;
    }

    public GridMap(int width, int height, double[] unitLength) {
        this.width = width;
        this.height = height;
        this.unitLength = unitLength;
        map = new Grid[height][width];
        if (latAndLongRange == null) {
            latAndLongRange = new double[4];
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = new Grid();
            }
        }
    }

    public GridMap(String slopePath, String typePath, String directionPath) throws IOException, TransformException {
        GridCoverage2D coverage = TiffTransform.readTiff(slopePath);
        RenderedImage renderedImage = coverage.getRenderedImage();
        Raster raster = renderedImage.getData();

        if (latAndLongRange == null) {
            latAndLongRange = new double[4];
        }

        width = renderedImage.getWidth();
        height = renderedImage.getHeight();
        map = new Grid[height][width];
        unitLength = TiffTransform.getUnitLength(coverage);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = new Grid();
            }
        }


        // 获取坡度
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j].slope = Math.toRadians(raster.getSampleFloat(j, i, 0));
                map[i][j].LatAndLon = TiffTransform.getLatAndLon(coverage, j, i);
            }
        }

        Envelope2D envelope2D = coverage.getEnvelope2D();
        // 获取经纬度范围
        this.latAndLongRange[0] = envelope2D.getBounds2D().getMinX();
        this.latAndLongRange[1] = envelope2D.getBounds2D().getMinY();
        this.latAndLongRange[2] = envelope2D.getBounds2D().getMaxX();
        this.latAndLongRange[3] = envelope2D.getBounds2D().getMaxY();

        coverage = TiffTransform.readTiff(typePath);
        raster = coverage.getRenderedImage().getData();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j].type = raster.getSampleFloat(j, i, 0);
            }
        }

        //获取坡向
        try {
            coverage = TiffTransform.readTiff(directionPath);
            raster = coverage.getRenderedImage().getData();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    map[i][j].direction = Math.toRadians(raster.getSampleFloat(j, i, 0));
                }
            }
        } catch (Exception e) {

        }

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

    public int[] LatAndLonToIdx(double lat, double lon) {
        // latAndLongRange=[left_lon,bottom_lat,right_lon,top_lat]
        // lat是纬度，用于计算i,lon是经度，用于计算j
        double left_lon = latAndLongRange[0];
        double bottom_lat = latAndLongRange[1];
        double right_lon = latAndLongRange[2];
        double top_lat = latAndLongRange[3];
        int i = (int) ((top_lat - lat) / (top_lat - bottom_lat) * height);
        int j = (int) ((lon - left_lon) / (right_lon - left_lon) * width);
//        int i = (int) ((lat - latAndLongRange[0]) / (latAndLongRange[2] - latAndLongRange[0]) * width);
//        int j = (int) ((latAndLongRange[3] - lon) / (latAndLongRange[3] - latAndLongRange[1]) * height);
        return new int[]{i, j};
    }
}
