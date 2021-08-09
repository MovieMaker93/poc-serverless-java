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

    private static final Logger LOG = LogManager.getLogger(Handler.class);


    @Override
    public ApiGatewayResponse handleRequest(Map<String,Object> input, Context context) {
        LOG.info("received: {}", input);
        Product product = null;
        double totalPrice = 0.0;
        double taxSales = 0.0;
        List<ProductWithTaxes> listTaxesResponse = new ArrayList<>();
        try {
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            if(body.isArray()){
                for (final JsonNode objNode : body){
                    product = new Product(objNode.get("quantity").asInt(),objNode.get("price").asDouble(),objNode.get("productName").asText(),
                            objNode.get("imported").asBoolean(),objNode.get("type").asText());
                    double totalPriceProduct = product.getPrice() * product.getQuantity();
                    if (!Utils.checkProductType(product.getType())){
                        double tax = Utils.calculateStandardTax(product.getPrice());
                        totalPriceProduct = totalPriceProduct + tax;
                        taxSales = taxSales + tax;
                    }
                    if(product.isImported()){
                        double importedTax = Utils.calculateImportedTax(product.getPrice());
                        totalPriceProduct = totalPriceProduct + importedTax;
                        taxSales = taxSales + importedTax;
                    }
                    listTaxesResponse.add(new ProductWithTaxes(product.getQuantity(),product.getProductName(),Utils.round(totalPriceProduct,2)));
                    totalPrice = totalPrice + totalPriceProduct;
                }
            }
            LOG.info("json object body", product);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Response response = new Response(listTaxesResponse,Utils.round(totalPrice,2),Utils.round(taxSales,2));
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(response)
				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
				.build();
    }
}
