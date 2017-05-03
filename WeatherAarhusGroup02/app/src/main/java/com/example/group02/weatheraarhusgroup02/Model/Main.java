package com.example.group02.weatheraarhusgroup02.Model;

/**
 * Created by Rune Rask on 03-05-2017.
 */

import java.util.HashMap;
import java.util.Map;

public class Main {

    public Double temp;
    public Integer pressure;
    public Integer humidity;
    public Double tempMin;
    public Double tempMax;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}