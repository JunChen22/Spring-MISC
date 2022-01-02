package com.example.spring.kubernetes.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Jun Chen on 11/10/21.
 */

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/all")
    public List getAllProduct(){
        return productRepository.findAll();
    }

    // save
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Product saveProduct(@RequestBody Product product){
        System.out.println(("The id is " +  product.getId()));
        Product product1 = productRepository.save(product);
        return product1;
    }

    // find
    @GetMapping("/{id}")
    public Product findOne(@PathVariable long id) throws Exception {
        return productRepository.findById(id).orElseThrow(
                () -> new Exception("Product id" + id + " not found"));
    }

    // delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) throws Exception {
        //Product product = productRepository.findById(id);
        //if(product != null) productRepository.deleteById(id);
        productRepository.deleteById(id);
    }

    //update
    @PutMapping("/{id}")
    public Product updateProduct(@RequestBody Product product, @PathVariable long id){
        if(product.getId() != id) return product; // need to throw exception
        Product p = productRepository.findById(id).orElse(null);
        if(p == null) return product; // need to throw unfound exception
        return productRepository.save(product);
    }
}
