package com.example.spring.docker.sql.demo;

import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class ProductRepository {

}
