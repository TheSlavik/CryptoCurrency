package com.project.CryptoCurrency.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "currencies")
public class Currency {

    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "price")
    private double price;
    @Column(name = "symbol")
    private String symbol;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
