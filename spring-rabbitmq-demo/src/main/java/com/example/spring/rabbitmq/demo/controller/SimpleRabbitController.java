package com.example.spring.rabbitmq.demo.controller;

import com.example.spring.rabbitmq.demo.simple.SimpleSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit")
public class SimpleRabbitController {

    @Autowired
    private SimpleSender simpleSender;

    @GetMapping("/simple")
    public String simpleTest(){
        try {
            for(int i = 0; i < 10; i++){
                simpleSender.send();
                Thread.sleep(1000);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return "message all send";
    }

    @GetMapping("/fanout")
    public String fanoutTest(){
        return "message all send";
    }

    @GetMapping("/direct")
    public String directTest() {
        return "message all send";
    }

    @GetMapping("/header")
    public String  headerTest() {
        return "message all send";
    }

    @GetMapping("/topic")
    public String  topicTest() {
        return "message all send";
    }

    @GetMapping("/default")
    public String  defaultTest() {
        return "message all send";
    }
}
