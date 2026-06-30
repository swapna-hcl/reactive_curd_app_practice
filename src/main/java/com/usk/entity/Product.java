package com.usk.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name="products")
public class Product extends PanacheEntity {


   public String name;

   public Double price;

}
