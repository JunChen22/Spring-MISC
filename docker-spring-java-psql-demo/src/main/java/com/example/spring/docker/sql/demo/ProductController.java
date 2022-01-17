package com.example.spring.docker.sql.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    @GetMapping("/allproduct")
    public List<String> getAllProduct(){
        List<String> products = new ArrayList<>();
        products.add("books");
        products.add("laptop");
        products.add("smartphone");
        return products;
    }

    @GetMapping("/")
    public String landing(){
        return "at homepage";
    }
}
