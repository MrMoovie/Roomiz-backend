package com.roomiz.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

public class Product extends BaseEntity{
    private String itemName;
    private double amount;
    private double price;

    @JsonIgnore
    private Set<UserEntity> users;
    public Product(){

    }
    public Product(String itemName, int amount, double price) {
        this.itemName = itemName;
        this.amount = amount;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }
}
