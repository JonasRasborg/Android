package com.example.group02.weatheraarhusgroup02.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherInfo {

    public Coord coord;
    public List<Weather> weather = null;
    public String base;
    public Main main;
    public Integer visibility;
    public Wind wind;
    public Clouds clouds;
    public Integer dt;
    public Sys sys;
    public Integer id;
    public String name;
    public Integer cod;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
