package com.zhou.bean;

import java.util.HashMap;
import java.util.Map;

public class FuelType {
    private String fuelName;
    private static Map<String, Double> map = new HashMap<>();

    static {
        map.put("针叶林", 0.4);
        map.put("次生林", 0.7);
        map.put("平铺松针", 0.8);
        map.put("红松", 1.0);
        map.put("云南松", 1.0);
        map.put("华山松", 1.0);
        map.put("枯枝落叶", 1.2);
        map.put("茅草杂草", 1.6);
        map.put("莎草矮桦", 1.8);
        map.put("针牧场草原", 2.0);
    }

    public FuelType(String fuelTypeName) {
        fuelName = fuelTypeName;
    }

    public String getFuelName() {
        return fuelName;
    }

    public double getKs() {
        return map.getOrDefault(fuelName, 0.0);
    }
}

