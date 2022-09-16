import com.zhou.bean.GridMap;
import com.zhou.utils.TiffTransform;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.coverage.Coverage;
import org.opengis.referencing.operation.TransformException;

import java.awt.image.Raster;
import java.io.IOException;

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
}
