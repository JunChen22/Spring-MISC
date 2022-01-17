package com.example.docker.spring.helloworld.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/hello")
    public String getHello(){
        return "hello world spring docker";
    }
}
