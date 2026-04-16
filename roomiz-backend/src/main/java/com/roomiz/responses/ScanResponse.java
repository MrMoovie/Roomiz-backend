package com.roomiz.responses;

import com.roomiz.entities.Product;

import java.util.List;

public class ScanResponse extends BasicResponse{
    private List<Product> products;
    public ScanResponse(boolean success, Integer errorCode, List<Product> products){
        super(success, errorCode);
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
