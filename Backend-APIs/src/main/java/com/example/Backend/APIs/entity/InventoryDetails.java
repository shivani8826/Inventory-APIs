package com.example.Backend.APIs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "inventory_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDetails {
    private static final long serialVersionUID = 4914459504751874271L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "batch")
    private String batch;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "deal")
    private Integer deal;

    @Column(name = "free")
    private Integer free;

    @Column(name = "mrp")
    private Double mrp;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "company")
    private String company;

    @Column(name = "exp")
    private String exp;

    @Column(name = "supplier")
    private String supplier;

}
