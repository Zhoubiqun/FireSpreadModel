package com.zhou.utils;

import javafx.beans.property.SimpleMapProperty;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.media.jai.ImageLayout;
import javax.media.jai.PlanarImage;
import java.awt.color.ColorSpace;
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

}
