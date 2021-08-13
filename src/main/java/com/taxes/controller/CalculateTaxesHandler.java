package com.taxes.controller;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.taxes.pojo.Product;
import com.taxes.pojo.Response;
import com.taxes.utils.ProductsEnumType;
import com.taxes.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CalculateTaxesHandler implements RequestHandler<Map<String,Object> ,ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(CalculateTaxesHandler.class);
    private static final String BODY = "body";
    private static final int HTTP_OK = 200;
    private static final int HTTP_INTERNAL_ERROR = 500;
    private static final String STRING_EMPTY = "";


    @Override
    public ApiGatewayResponse handleRequest(Map<String,Object> input, Context context) {
        try {
            LOG.info("received: {}", input);
            BigDecimal totalPrice = new BigDecimal("0");
            BigDecimal currentTax;
            BigDecimal totalTax = new BigDecimal("0");
            List<Product> listTaxesResponse = new ArrayList<>();
            // Get the body from the input
            if (input.get(BODY) != null) {
                JsonNode body = new ObjectMapper().readTree((String) input.get(BODY));
                if (body.isArray()) {
                    //Get each product from array of products
                    for (final JsonNode objNode : body) {
                        String productName = objNode.get("productName") != null ? objNode.get("productName").asText() : STRING_EMPTY;
                        int quantity = objNode.get("quantity") != null ? objNode.get("quantity").asInt() : 0;
                        String productPrice = objNode.get("price") != null ? objNode.get("price").asText() : "0.0";
                        //check if it is imported
                        boolean isImported = productName.contains("imported");
                        ProductsEnumType productType = Utils.checkTypeOfProduct(productName,isImported);
                        Product product = new Product(quantity, productPrice, productName, productType);
                        LOG.info("Current product name {}", product.getProductName());
                        // Standard total price product is price product multiplied for the quantity
                        double priceProductWithQuantity = Double.parseDouble(productPrice) * quantity;
                        currentTax = BigDecimal.valueOf(0);
                        BigDecimal priceProductWithoutTax = new BigDecimal(productPrice);
                        totalPrice = totalPrice.add(BigDecimal.valueOf(priceProductWithQuantity));
                        // Exclude goods product from taxes 10%
                        if (productType.isTaxed()) {
                            BigDecimal tax = Utils.calculateStandardTax(priceProductWithoutTax);
                            // Round up to the nearest 0.05
                            tax = Utils.round(tax,BigDecimal.valueOf(0.05), RoundingMode.UP);
                            // current tax is taxed calculated on single product multiplied for the quantity
                            currentTax = currentTax.add(tax.multiply(new BigDecimal(quantity)));
                        }
                        // If imported product, apply 5% duty taxes in the total price product
                        if (isImported) {
                            BigDecimal importedTax = Utils.calculateImportedTax(priceProductWithoutTax);
                            // Round up to the nearest 0.05
                            importedTax = Utils.round(importedTax,BigDecimal.valueOf(0.05), RoundingMode.UP);
                            // current tax is taxed calculated on single product multiplied for the quantity
                            currentTax = currentTax.add(importedTax.multiply(new BigDecimal(quantity)));
                        }
                        product.setPrice(Utils.roundTwoDecimal(priceProductWithQuantity + currentTax.doubleValue()));
                        listTaxesResponse.add(product);
                        totalPrice = totalPrice.add(currentTax);
                        totalTax = totalTax.add(currentTax);
                    }
                }
            }
            Response response = new Response(listTaxesResponse, Utils.roundTwoDecimal(totalPrice.doubleValue()), Utils.roundTwoDecimal(totalTax.doubleValue()));
            LOG.info("Total Price {}", response.getTotalPrice());
            LOG.info("Total Taxes {}", response.getTaxSales());
            return buildResponse(HTTP_OK, response,
                    Collections.singletonMap("X-Powered-By", "Taxes calculated"));
        }catch (IOException e) {
            LOG.error("Error in retrieving taxes", e);
            String errorMessage = "Error in retrieving taxes for the request:  " + input.get(BODY);
            return buildResponse(HTTP_INTERNAL_ERROR,errorMessage,
                    Collections.singletonMap("X-Powered-By", "Taxes calculated"));
        }
    }

    private ApiGatewayResponse buildResponse(int statusCode, Object objectBody, Map<String,String> headers){
        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(objectBody)
                .setHeaders(headers)
                .build();
    }
}
