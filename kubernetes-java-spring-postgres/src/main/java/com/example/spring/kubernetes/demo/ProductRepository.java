package com.example.spring.kubernetes.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jun Chen on 11/10/21.
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();
}
