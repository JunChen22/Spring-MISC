package com.itsthatjun.airportapi.service;

import com.itsthatjun.airportcore.model.PlaneDTO;
import org.springframework.stereotype.Service;

@Service
public class PlaneService {

    public PlaneDTO getPlane(){
        return new PlaneDTO("B-2", 4);
    }
}
