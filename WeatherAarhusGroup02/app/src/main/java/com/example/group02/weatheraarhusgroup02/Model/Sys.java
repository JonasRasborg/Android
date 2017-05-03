package com.example.group02.weatheraarhusgroup02.Model;

import java.util.HashMap;
import java.util.Map;

public class Sys {

    public Integer type;
    public Integer id;
    public Double message;
    public String country;
    public Integer sunrise;
    public Integer sunset;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}