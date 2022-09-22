package com.zhou.controller;

import com.zhou.bean.GridMap;
import com.zhou.bean.Wind;
import com.zhou.model.FireSpreadModel;
import com.zhou.utils.TiffTransform;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
public class FirePredictController {

    @Value("${filename.slope}")
    private String slopePath;

    @Value("${filename.type}")
    private String typePath;

    @Value("${filename.direction}")
    private String directionPath;

    @Value("${filename.result}")
    private String resultPath;

    @RequestMapping("/startRun")
    public String run(@RequestParam("ws") Double windSpeed,
                      @RequestParam("wd") Double windDirection,
                      @RequestParam("spLat") Double startPointLat,
                      @RequestParam("spLon") Double startPointLon,
                      @RequestParam("mT") double maxTime,
                      @RequestParam("fuelMc") double fuelMoistureContent) {
        Wind wind = new Wind(windDirection, windSpeed);
        GridMap mapModel = null;

        try {
//            File slopeFile = ResourceUtils.getFile(this.getClass().getClassLoader().getResource("").getPath()+slopePath);
//            File typeFile = ResourceUtils.getFile(this.getClass().getClassLoader().getResource("").getPath()+typePath);
//            File directionFile = ResourceUtils.getFile(this.getClass().getClassLoader().getResource("").getPath()+directionPath);
            mapModel = new GridMap(slopePath, typePath, directionPath);
        } catch (IOException e) {
            e.printStackTrace();
            return "Read file error :" + e;
        } catch (TransformException e) {
            e.printStackTrace();
            return String.valueOf(e);
        }

        int[] loc = mapModel.LatAndLonToIdx(startPointLat, startPointLon);
        List<int[]> startPoints = Arrays.asList(loc);
        FireSpreadModel fireSpreadModel = new FireSpreadModel();
        fireSpreadModel.setParameter(fuelMoistureContent, mapModel, wind, startPoints, maxTime);
        fireSpreadModel.run();


        try {
//            File resultFile = ResourceUtils.getFile(this.getClass().getClassLoader().getResource("").getPath()+resultPath);
//            GeoTiffReader geoTiffReader = new GeoTiffReader(this.getClass().getClassLoader().getResource("").getPath()+slopePath);
            String resultFilePath = String.format("%s/result_ws=%.1f&wd=%.2f&spLat=%.2f&spLon=%.2f&mT=%.1f&fuelMc=%.1f.tif",
                    resultPath, windSpeed, windDirection, startPointLat, startPointLon, maxTime, fuelMoistureContent);
            GeoTiffReader geoTiffReader = new GeoTiffReader(slopePath);
            GridCoverage2D gridCoverage2D = TiffTransform.readTiff(slopePath);
            GridCoverage2D toTiff = TiffTransform.saveToTiff(geoTiffReader, gridCoverage2D, mapModel, resultFilePath);
        } catch (Exception e) {
            return "Save result error :" + e;
        }
//        return "redirect:localhost:11117/result.tif";
        return "ok";
    }

}
