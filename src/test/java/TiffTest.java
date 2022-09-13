import com.sun.media.jai.opimage.PatternRIF;
import com.zhou.utils.TiffTransform;
import org.junit.Test;
import org.opengis.coverage.Coverage;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;

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
}
