import com.zhou.FireSpreadModel;
import com.zhou.MapModel;
import com.zhou.WindModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FireSpreadModelTest {
    @Test
    public void testR0() {
//        FireSpreadModel fireSpreadModel = new FireSpreadModel();
//        fireSpreadModel.setParameter(7.4, null, null, null);
//        System.out.println(fireSpreadModel.getR0());
//        fireSpreadModel.setParameter(13.8, null, null, null);
//        System.out.println(fireSpreadModel.getR0());
//        fireSpreadModel.setParameter(16.8, null, null, null);
//        System.out.println(fireSpreadModel.getR0());
//        fireSpreadModel.setParameter(20.9, null, null, null);
//        System.out.println(fireSpreadModel.getR0());
//        fireSpreadModel.setParameter(12.0, null, null, null);
//        System.out.println(fireSpreadModel.getR0());
//        fireSpreadModel.setParameter(13.6, null, null, null);
//        System.out.println(fireSpreadModel.getR0());
    }

    @Test
    public void testKs() {
//        FireSpreadModel fireSpreadModel = new FireSpreadModel();
//        fireSpreadModel.setParameter(7.4, new FuelType("水"), null, null);
//        System.out.println(fireSpreadModel.getKs());
//        fireSpreadModel.setParameter(7.4, new FuelType("针叶林"), null, null);
//        System.out.println(fireSpreadModel.getKs());
    }

    @Test
    public void testRun() throws IOException {
        String filepathSlope = "C:\\code\\java\\javaweb\\forestFireSimulation\\src\\main\\java\\com\\wy\\v1\\test\\zhejiangDEM_clip_slope.tif";
        String filepathType = "C:\\code\\java\\javaweb\\forestFireSimulation\\src\\main\\java\\com\\wy\\v1\\test\\zhejiangDEM_clip_type.tif";

        MapModel mapModel = new MapModel(filepathSlope, filepathType, null);
        WindModel windModel = new WindModel(0, 6);
//        windModel.direction = Math.PI;//南
        windModel.setDirection(Math.PI / 2);// 东
//        windModel.direction = -Math.PI / 2;// 西
//        WindModel windModel = new WindModel(-Math.PI, 6);
//        WindModel windModel = new WindModel(Math.PI / 2, 6);
//        WindModel windModel = new WindModel(-Math.PI / 2, 6);

//        WindModel windModel = new WindModel(Math.PI / 4, 6);
        List<int[]> startPoints = Arrays.asList(new int[]{50, 50});

        FireSpreadModel fireSpreadModel = new FireSpreadModel();
        fireSpreadModel.setParameter(7.4, mapModel, windModel, startPoints, 10);
        fireSpreadModel.run();
    }

}