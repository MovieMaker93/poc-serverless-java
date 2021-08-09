package com.claranet.pojo;

public class ProductWithTaxes {

    private final int quantity;
    private final String productName;
    private final double totalPrice;

    public ProductWithTaxes(int quantity, String productName, double totalPrice) {
        this.quantity = quantity;
        this.productName = productName;
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
