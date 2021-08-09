package com.claranet.pojo;

import java.util.List;

public class Response {

    private final List<ProductWithTaxes> productWithTaxesList;

    public Response(List<ProductWithTaxes> productWithTaxesList) {
        this.productWithTaxesList = productWithTaxesList;
    }

    public List<ProductWithTaxes> getProductWithTaxesList() {
        return productWithTaxesList;
    }
}
