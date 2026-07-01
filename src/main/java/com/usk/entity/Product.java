package com.usk.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name="products")
public class Product extends PanacheEntity {

    public String name;

    public Double price;

}
