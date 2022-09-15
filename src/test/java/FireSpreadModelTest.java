import com.zhou.FireSpreadModel;
import com.zhou.Grid;
import com.zhou.MapModel;
import com.zhou.WindModel;
import org.junit.jupiter.api.Test;
import org.opengis.referencing.operation.TransformException;

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
    public void testRun() throws IOException, TransformException {
        String filepathSlope = "C:\\code\\java\\javaweb\\forestFireSimulation\\src\\main\\java\\com\\wy\\v1\\test\\zhejiangDEM_clip_slope.tif";
        String filepathType = "C:\\code\\java\\javaweb\\forestFireSimulation\\src\\main\\java\\com\\wy\\v1\\test\\zhejiangDEM_clip_type.tif";

        MapModel mapModel = new MapModel(filepathSlope, filepathType, null);
        WindModel windModel = new WindModel(0, 0);
//        windModel.setDirection(Math.PI / 2);// 东
//        windModel.setDirection(Math.PI); // 南
//        windModel.setDirection(0); // 北
        windModel.setDirection(-Math.PI / 2); // 西
        windModel.setDirection(Math.PI / 4); // 东北

        List<int[]> startPoints = Arrays.asList(new int[]{50, 50});

        Grid[][] map = mapModel.getMap();

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
        char[][] t2 = getView(mapModel);
        int t = 0;
    }


    @Test
    public void testSlope() {
        String filepathSlope = "C:\\code\\java\\javaweb\\forestFireSimulation\\src\\main\\java\\com\\wy\\v1\\test\\zhejiangDEM_clip_slope.tif";
        String filepathType = "C:\\code\\java\\javaweb\\forestFireSimulation\\src\\main\\java\\com\\wy\\v1\\test\\zhejiangDEM_clip_type.tif";

        MapModel mapModel = new MapModel(300, 300, new double[]{1.0, 1.0});
        WindModel windModel = new WindModel(0, 0);

        // 测试同一坡度，不同坡向
        List<int[]> startPoints = Arrays.asList(new int[]{50, 50}, new int[]{150, 50}, new int[]{250, 50});
        Grid[][] map = mapModel.getMap();
        for (int i = 0; i < mapModel.getHeight(); i++) {
            for (int j = 0; j < mapModel.getWidth(); j++) {
                map[i][j].type = 2; //设置为2
                map[i][j].slope = Math.PI / 2 / 4;
                if (i < 100) {
                    map[i][j].direction = 0; //北坡
                } else if (i < 200) {
                    map[i][j].direction = Math.PI / 2; //东坡
                } else {
                    map[i][j].direction = Math.PI + Math.PI / 4; //西南坡
                }
            }
        }
        FireSpreadModel fireSpreadModel = new FireSpreadModel();
        fireSpreadModel.setParameter(7.4, mapModel, windModel, startPoints, 10);
        fireSpreadModel.run();
        char[][] t1 = getView(mapModel);
        // 测试同一坡向,不同坡度
        for (int i = 0; i < mapModel.getHeight(); i++) {
            for (int j = 0; j < mapModel.getWidth(); j++) {
                map[i][j].type = 2; //设置为2
                map[i][j].direction = 0;
                if (i < 100) {
                    map[i][j].slope = Math.PI / 16;
                } else if (i < 200) {
                    map[i][j].slope = Math.PI / 8;
                } else {
                    map[i][j].slope = Math.PI / 4;
                }
            }
        }
        fireSpreadModel.setParameter(7.4, mapModel, windModel, startPoints, 10);
        fireSpreadModel.run();
        char[][] t2 = getView(mapModel);
        int t = 0;
    }

    @Test
    public void testStartPoints() throws TransformException, IOException {
        String filepathSlope = "C:\\code\\java\\javaweb\\forestFireSimulation\\src\\main\\java\\com\\wy\\v1\\test\\zhejiangDEM_clip_slope.tif";
        String filepathType = "C:\\code\\java\\javaweb\\forestFireSimulation\\src\\main\\java\\com\\wy\\v1\\test\\zhejiangDEM_clip_type.tif";

        MapModel mapModel = new MapModel(filepathSlope, filepathType, null);
        double lat = 30.364368794296, lon = 119.671559873054;
        int[] loc = mapModel.LatAndLonToIdx(lat, lon);
        Grid grid = (mapModel.getMap())[loc[0]][loc[1]];
        System.out.println(String.format("lat=%f,lon=%f,loc=(%d,%d),grid_lat=%f,grid_lon=%f", lat, lon, loc[0], loc[1], grid.LatAndLon[1], grid.LatAndLon[0]));

    }

    public char[][] getView(MapModel gridMap) {
        char[][] t = new char[gridMap.getHeight()][gridMap.getWidth()];
        for (int i = 0; i < gridMap.getHeight(); i++) {
            for (int j = 0; j < gridMap.getWidth(); j++) {
                if ((gridMap.getMap())[i][j].isIgnited) {
                    t[i][j] = '-';
                } else {
                    t[i][j] = 'o';
                }
            }
        }
        return t;
    }
}