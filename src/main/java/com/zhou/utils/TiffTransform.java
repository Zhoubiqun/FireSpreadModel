package com.zhou.utils;

import com.zhou.bean.Grid;
import com.zhou.bean.GridMap;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.jaitools.imageutils.ImageUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.DirectPosition;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import sun.awt.image.SunWritableRaster;

import javax.media.jai.ImageLayout;
import javax.media.jai.PlanarImage;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;

/**
 * @author zbq
 * @date 2022/9/13 14:18
 */
public class TiffTransform {

    public void Trans(String filePath) throws IOException {
        /*
         * 使用GeoTiffReader
         */
        GeoTiffReader geoTiffReader = new GeoTiffReader(filePath);
        //获取到其bbox
        GeneralEnvelope envelope = geoTiffReader.getOriginalEnvelope();
        Rectangle2D rectangle2D = envelope.toRectangle2D();
        System.out.println("envelope:" + envelope);
        System.out.println("rectangle2D:" + rectangle2D);

        //获取到投影信息
        CoordinateReferenceSystem crs = geoTiffReader.getCoordinateReferenceSystem();
        System.out.println("crs:" + crs);

        //获取tiff的gridrange(网格范围，按像素说就是像素数量，也就是是tiff的rectangle)
        GridEnvelope gridEnvelope = geoTiffReader.getOriginalGridRange();
        System.out.println("gridEnvelope:" + gridEnvelope);

        //获取其图像相关信息
        ImageLayout imageLayout = geoTiffReader.getImageLayout();
        System.out.println("imageLayout:" + imageLayout);

        //获取color-model
        ColorModel colorModel = imageLayout.getColorModel(null);

        //查看其通道数量
        int num = colorModel.getNumComponents();

        //是否支持透明
        boolean isAlpha = colorModel.hasAlpha();

        //获取颜色空间
        ColorSpace clorspace = colorModel.getColorSpace();

        //获取samplemodel
        SampleModel sampleModel = imageLayout.getSampleModel(null);

        //数据类型
        int datatype = sampleModel.getDataType();
    }

    public static GridCoverage2D readTiff(String tiffPath) throws IOException {
        File f = new File(tiffPath);
        ParameterValue<OverviewPolicy> policy = AbstractGridFormat.OVERVIEW_POLICY.createValue();
        policy.setValue(OverviewPolicy.IGNORE);
        ParameterValue<String> gridsize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
        ParameterValue<Boolean> useJaiRead = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJaiRead.setValue(true);
        GridCoverage2D image = new GeoTiffReader(f).read(new GeneralParameterValue[]{policy, gridsize, useJaiRead});

//        Envelope2D envelope2D = image.getEnvelope2D();
//        double minX = envelope2D.getBounds().getMinX();
//        double minY = envelope2D.getBounds().getMinY();
//        double maxX = envelope2D.getBounds().getMaxX();
//        double maxY = envelope2D.getBounds().getMaxY();
//        System.out.println(minX + " " + minY + " "+maxX +" "+maxY);
//
//        RenderedImage renderedImage = image.getRenderedImage();
//        Raster raster = renderedImage.getData();
//        Envelope envelope = image.getEnvelope();
//        int width = renderedImage.getWidth();
//        int height = renderedImage.getHeight();
//        for(int i=1;i<height;i++){
//            for(int j=1;j<width-1;j++){
//                float sampleFloat = raster.getSampleFloat(i, j, 0);
//                System.out.print("("+i+","+j+","+sampleFloat+")");
//            }
//        }

        return image;
    }

    public static double[] getUnitLength(GridCoverage2D coverage){
        Envelope2D envelope2D = coverage.getEnvelope2D();
        //获取经纬度
        double minX = envelope2D.getBounds2D().getMinX();
        double minY = envelope2D.getBounds2D().getMinY();
        double maxX = envelope2D.getBounds2D().getMaxX();
        double maxY = envelope2D.getBounds2D().getMaxY();
//        System.out.println(minX + " " + minY + " "+maxX +" "+maxY);
        // 84坐标系构造Geo
        GeodeticCalculator geodeticCalculator = new GeodeticCalculator(DefaultGeographicCRS.WGS84);
        // 左上角经纬度
        geodeticCalculator.setStartingGeographicPoint(minX,maxY);
        // 右上角经纬度
        geodeticCalculator.setDestinationGeographicPoint(maxX,maxY);
        // 计算距离：单位米
        double distanceLon = geodeticCalculator.getOrthodromicDistance();

        // 左上角经纬度
        geodeticCalculator.setStartingGeographicPoint(minX,maxY);
        // 左下角经纬度
        geodeticCalculator.setDestinationGeographicPoint(minX,minY);
        // 计算距离：单位米
        double distanceLat = geodeticCalculator.getOrthodromicDistance();
//        System.out.println(distance+" "+distance1);

        Raster data = coverage.getRenderedImage().getData();
        int width = data.getWidth();
        int height = data.getHeight();
        //栅格平均距离
//        System.out.println(distance/width);
//        System.out.println(distance1/height);
        return new double[]{distanceLat/height,distanceLon/width};
    }

    // 初始化获取各个像素点经纬度
    public static double[] getLatAndLon(GridCoverage2D coverage,int i,int j) throws TransformException {
        GridEnvelope gridRange = coverage.getGridGeometry().getGridRange();
        GeometryFactory gf = new GeometryFactory();

        GridCoordinates2D coord = new GridCoordinates2D(i,j);
        DirectPosition p = coverage.getGridGeometry().gridToWorld(coord);
        Point point = gf.createPoint(new Coordinate(p.getOrdinate(0), p.getOrdinate(1)));
        //Geometry wgsP = JTS.transform(point, targetToWgs);
//        System.out.format("(%d %d) -> POINT(%.2f %.2f)%n", i, j, point.getCoordinate().x,
//                point.getCoordinate().y);
        //wgsP.getCentroid().getCoordinate().x, wgsP.getCentroid().getCoordinate().y

        return new double[]{point.getCoordinate().y, point.getCoordinate().x};
    }

    // 根据经纬度算距离
    public static double getDist(Grid g1, Grid g2){
        // 84坐标系构造Geo
        GeodeticCalculator geodeticCalculator = new GeodeticCalculator(DefaultGeographicCRS.WGS84);
        // 左上角经纬度
        geodeticCalculator.setStartingGeographicPoint(g1.LatAndLon[0], g1.LatAndLon[1]);
        // 右上角经纬度
        geodeticCalculator.setDestinationGeographicPoint(g2.LatAndLon[0], g2.LatAndLon[1]);

        return geodeticCalculator.getOrthodromicDistance();
    }

    //

    /**
     * 根据几何模型进行影像切割
     * @param reader 原始印象
     */
    public static GridCoverage2D saveToTiff( GeoTiffReader reader, GridCoverage2D coverage,GridMap gridMap,String dstFile) {
        try{
            //包围盒
            Envelope2D envelope2D = coverage.getEnvelope2D();
            //获取经纬度
            double minX = envelope2D.getBounds2D().getMinX();
            double minY = envelope2D.getBounds2D().getMinY();
            double maxX = envelope2D.getBounds2D().getMaxX();
            double maxY = envelope2D.getBounds2D().getMaxY();

            //转换为数组块
            int width = gridMap.getWidth();
            int height = gridMap.getHeight();
            float[][] arrBlock = new float[height][width];
            for(int row =0; row < height; row ++){
                for(int col =0; col < width; col ++){
                    arrBlock[row][col] = (float) gridMap.getMap()[row][col].getIgnitedTime();
                }
            }

            //保存输出
            Envelope2D tmEnvelope = new Envelope2D(reader.getCoordinateReferenceSystem(),minX,minY,maxX - minX,maxY - minY);
            GridCoverageFactory gridFactory = new GridCoverageFactory();
            GridCoverage2D outputCoverage = gridFactory.create("subtractTiff", arrBlock,tmEnvelope);

            GeoTiffWriter writer = new GeoTiffWriter(new File(dstFile));
            writer.write(outputCoverage, null);
            writer.dispose();
            return  outputCoverage;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }

    }
}
