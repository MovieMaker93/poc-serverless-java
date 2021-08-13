package com.taxes.pojo;

import com.taxes.utils.ProductsEnumType;

import java.io.Serializable;



public class Product implements Serializable, Cloneable{

    private final int quantity;
    private String price;
    private final String productName;
    private final ProductsEnumType productsEnumType;

    public Product(int quantity, String price, String productName, ProductsEnumType productsEnumType) {
        this.quantity = quantity;
        this.price = price;
        this.productName = productName;
        this.productsEnumType = productsEnumType;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }
}
