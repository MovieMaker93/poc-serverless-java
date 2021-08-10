package com.claranet.controller;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.claranet.pojo.Product;
import com.claranet.pojo.ProductWithTaxes;
import com.claranet.pojo.Response;
import com.claranet.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GetTaxesHandler implements RequestHandler<Map<String,Object> ,ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(GetTaxesHandler.class);


    @Override
    public ApiGatewayResponse handleRequest(Map<String,Object> input, Context context) {
        try {
            LOG.info("received: {}", input);
            double totalPrice = 0.0;
            double taxSales = 0.0;
            List<ProductWithTaxes> listTaxesResponse = new ArrayList<>();
            // Get the body from the input
            if (input.get("body") != null) {
                JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
                if (body.isArray()) {
                    //Get each product from array of products
                    for (final JsonNode objNode : body) {
                        Product product = new Product(objNode.get("quantity").asInt(), objNode.get("price").asDouble(), objNode.get("productName").asText(),
                                objNode.get("imported").asBoolean(), objNode.get("type").asText());
                        LOG.info("Current product name {}", product.getProductName());
                        double priceProduct = product.getPrice();
                        int quantity = product.getQuantity();
                        // Standard total price product is price product multiplied for the quantity
                        double totalPriceProductWithoutTax = priceProduct * quantity;
                        double totalPriceWithTax = totalPriceProductWithoutTax;
                        // Exclude goods product from taxes 10%
                        if (!Utils.checkProductType(product.getType())) {
                            double tax = Utils.calculateStandardTax(totalPriceProductWithoutTax);
                            totalPriceWithTax = totalPriceWithTax + tax;
                            taxSales = taxSales + tax;
                        }
                        // If imported product, apply 5% duty taxes in the total price product
                        if (product.isImported()) {
                            double importedTax = Utils.calculateImportedTax(totalPriceProductWithoutTax);
                            totalPriceWithTax = totalPriceWithTax + importedTax;
                            taxSales = taxSales + importedTax;
                        }
                        listTaxesResponse.add(new ProductWithTaxes(quantity, product.getProductName(), Utils.round(totalPriceWithTax, 2)));
                        totalPrice = totalPrice + totalPriceWithTax;
                    }
                }
            }
            Response response = new Response(listTaxesResponse, Utils.round(totalPrice, 2), Utils.round(taxSales, 2));
            LOG.info("Total Price {}", response.getTotalPrice());
            LOG.info("Total Taxes {}", response.getTaxSales());
            return buildResponse(200, response,
                    Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"));
        }catch (IOException e) {
            LOG.error("Error in retrieving taxes", e);
            String errorMessage = "Error in retrieving taxes: " + input;
            return buildResponse(500,errorMessage,
                    Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"));
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
