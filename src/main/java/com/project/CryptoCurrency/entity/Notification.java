package com.project.CryptoCurrency.entity;

import jakarta.persistence.*;

@Entity(name = "notifications")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "symbol"})})
public class Notification {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "current_price")
    private double price;
    @Column(name = "symbol")
    private String symbol;
}
