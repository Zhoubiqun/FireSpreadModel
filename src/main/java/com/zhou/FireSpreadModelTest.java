package com.zhou;


import org.junit.jupiter.api.Test;

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
    public void testRun() {
        MapModel mapModel = new MapModel(300, 300, 1);
        WindModel windModel = new WindModel(0, 6);
//        windModel.direction = Math.PI;//南
        windModel.direction = Math.PI / 2;// 东
//        windModel.direction = -Math.PI / 2;// 西
//        WindModel windModel = new WindModel(-Math.PI, 6);
//        WindModel windModel = new WindModel(Math.PI / 2, 6);
//        WindModel windModel = new WindModel(-Math.PI / 2, 6);

//        WindModel windModel = new WindModel(Math.PI / 4, 6);
        FuelType fuelType = new FuelType("针叶林");
        List<int[]> startPoints = Arrays.asList(new int[]{50, 50}, new int[]{100, 50});

        FireSpreadModel fireSpreadModel = new FireSpreadModel();
        fireSpreadModel.setParameter(7.4, fuelType, mapModel, windModel, startPoints, 60);
        fireSpreadModel.run();
    }

}