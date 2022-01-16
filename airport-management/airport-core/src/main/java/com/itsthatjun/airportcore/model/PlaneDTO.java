package com.itsthatjun.airportcore.model;

import lombok.Getter;

@Getter
public class PlaneDTO {
    String name;
    int engineCount;

    public PlaneDTO(String name, int engineCount) {
        this.name = name;
        this.engineCount = engineCount;
    }
}
