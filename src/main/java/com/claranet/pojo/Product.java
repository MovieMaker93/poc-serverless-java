package com.claranet.pojo;

public class Product {

    private int quantity;
    private double price;
    private String productName;
    private boolean imported;
    private final String type;

    public Product(int quantity, double price, String productName, boolean imported, String type) {
        this.quantity = quantity;
        this.price = price;
        this.productName = productName;
        this.imported = imported;
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getProductName() {
        return productName;
    }

    public boolean isImported() {
        return imported;
    }

    public String getType(){
        return type;
    }
}
