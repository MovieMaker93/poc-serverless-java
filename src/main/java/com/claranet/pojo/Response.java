package com.claranet.pojo;

import java.util.List;

public class Response {

    private final List<Product> productWithTaxes;
    private final String totalPrice;
    private final String taxSales;

    public Response(List<Product> productWithTaxes, String totalPrice, String taxSales) {
        this.productWithTaxes = productWithTaxes;
        this.totalPrice = totalPrice;
        this.taxSales = taxSales;
    }

    public List<Product> getProductWithTaxes() {
        return productWithTaxes;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getTaxSales() {
        return taxSales;
    }
}
