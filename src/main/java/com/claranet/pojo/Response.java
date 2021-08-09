package com.claranet.pojo;

import java.util.List;

public class Response {

    private final List<ProductWithTaxes> productWithTaxesList;
    private final double totalPrice;
    private final double taxSales;

    public Response(List<ProductWithTaxes> productWithTaxesList, double totalPrice, double taxSales) {
        this.productWithTaxesList = productWithTaxesList;
        this.totalPrice = totalPrice;
        this.taxSales = taxSales;
    }

    public List<ProductWithTaxes> getProductWithTaxesList() {
        return productWithTaxesList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getTaxSales() {
        return taxSales;
    }
}
