package com.itsthatjun.airportapi.controller;

import com.itsthatjun.airportapi.service.PlaneService;
import com.itsthatjun.airportcore.model.PlaneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PlaneController {

    @Autowired
    PlaneService planeService;

    @GetMapping("/planes")
    public ResponseEntity<PlaneDTO> getPlane(){
        PlaneDTO planeDTO = planeService.getPlane();
        return new ResponseEntity<>(planeDTO, HttpStatus.OK);
    }
}
