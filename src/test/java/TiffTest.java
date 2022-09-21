import com.zhou.bean.Grid;
import com.zhou.bean.GridMap;
import com.zhou.bean.Wind;
import com.zhou.model.FireSpreadModel;
import com.zhou.utils.TiffTransform;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.coverage.Coverage;
import org.opengis.referencing.operation.TransformException;

import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author zbq
 * @date 2022/9/13 14:53
 */
public class TiffTest {
    private String filepathSlope = "D:\\作业\\实习\\论文\\GIS林火蔓延模型\\test\\zhejiangDEM_clip_slope.tif";
    private String filepathType = "D:\\作业\\实习\\论文\\GIS林火蔓延模型\\test\\zhejiangDEM_clip_type.tif";

    @Test
    public void test() throws IOException {
        TiffTransform tiffTransform = new TiffTransform();
        tiffTransform.Trans(filepathSlope);
    }

    @Test
    public void test1() throws IOException {
        Coverage coverage = TiffTransform.readTiff(filepathType);
        //System.out.println(coverage);

    }

    @Test
    public void loadSlope() throws IOException, TransformException {
        GridMap mapModel = new GridMap(filepathSlope, filepathType, null);
        int t=0;

    }

    @Test
    public void testDist() throws IOException {
        GridCoverage2D gridCoverage2D = TiffTransform.readTiff(filepathSlope);
        Envelope2D envelope2D = gridCoverage2D.getEnvelope2D();
        //获取经纬度
        double minX = envelope2D.getBounds2D().getMinX();
        double minY = envelope2D.getBounds2D().getMinY();
        double maxX = envelope2D.getBounds2D().getMaxX();
        double maxY = envelope2D.getBounds2D().getMaxY();
        System.out.println(minX + " " + minY + " "+maxX +" "+maxY);
        // 84坐标系构造Geo
        GeodeticCalculator geodeticCalculator = new GeodeticCalculator(DefaultGeographicCRS.WGS84);
        geodeticCalculator.setStartingGeographicPoint(minX,minY);
        geodeticCalculator.setDestinationGeographicPoint(maxX,minY);
        double distance = geodeticCalculator.getOrthodromicDistance();

        geodeticCalculator.setStartingGeographicPoint(minX,minY);
        geodeticCalculator.setDestinationGeographicPoint(minX,maxY);
        double distance1 = geodeticCalculator.getOrthodromicDistance();
        System.out.println(distance+" "+distance1);

        Raster data = gridCoverage2D.getRenderedImage().getData();
        int width = data.getWidth();
        int height = data.getHeight();
        System.out.println(distance/width);
        System.out.println(distance1/height);
    }

    @Test
    public void testLat() throws TransformException, IOException {
        GridCoverage2D gridCoverage2D = TiffTransform.readTiff(filepathSlope);
        double[] latAndLon = TiffTransform.getLatAndLon(gridCoverage2D, 0, 0);
        System.out.println(latAndLon[0]+" "+latAndLon[1]);
    }

    @Test
    public void testSave() throws IOException, TransformException {
        GeoTiffReader geoTiffReader = new GeoTiffReader(filepathSlope);
        GridCoverage2D gridCoverage2D = TiffTransform.readTiff(filepathSlope);

        GridMap mapModel = new GridMap(filepathSlope, filepathType, null);
        Wind windModel = new Wind(0, 0);
        windModel.setDirection(-Math.PI / 2); // 西
        Grid[][] map = mapModel.getMap();
        List<int[]> startPoints = Arrays.asList(new int[]{50, 50});

        for (int i = 0; i < mapModel.getHeight(); i++) {
            for (int j = 0; j < mapModel.getWidth(); j++) {
                if (i < 50)
                    map[i][j].type = 4;
                else
                    map[i][j].type = 2;
            }
        }
        FireSpreadModel fireSpreadModel = new FireSpreadModel();
        fireSpreadModel.setParameter(7.4, mapModel, windModel, startPoints, 1000);
        fireSpreadModel.run();

        int i1 = filepathSlope.lastIndexOf("\\");
        String ds = filepathSlope.substring(0,i1) + File.separator + "temp.tiff";
        GridCoverage2D toTiff = TiffTransform.saveToTiff(geoTiffReader, gridCoverage2D,mapModel,ds);

        Raster data = gridCoverage2D.getRenderedImage().getData();
        int width = data.getWidth();
        int height = data.getHeight();

        Raster data1 = toTiff.getRenderedImage().getData();
        int width1 = data1.getWidth();
        int height1 = data1.getHeight();
        System.out.println(width1==width);
        System.out.println(height1==height);
        System.out.println(width1);
        System.out.println(height1);

        for(int i=0;i<height1;i++){
            for(int j=0;j<width1;j++){
                System.out.println(mapModel.getMap()[i][j].ignitedTime + "," + data1.getSampleFloat(j,i,0));
            }
        }
    }
}
