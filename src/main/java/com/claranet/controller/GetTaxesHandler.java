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
        LOG.info("received: {}", input.get("body"));
        try {
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            List products = body.findValuesAsText("products");
            LOG.info("json object body", products);
        } catch (IOException e) {
            e.printStackTrace();
        }
      /*  try {
            products = objectMapper.readValue((String)input, Product.class);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        List<ProductWithTaxes> listTaxesResponse = new ArrayList<>();
        listTaxesResponse.add(new ProductWithTaxes(2,"food-product",0.0));
        Response response = new Response(listTaxesResponse);
        /*for(Product product : products) {
            Double totalPrice = null;
            if (Utils.checkProductType(product.getType())){
                totalPrice = Utils.calculateImportedTax(product.getPrice(), product.isImported());
            }
            else{
                totalPrice = Utils.calculateImportedTax(Utils.calculateStandardTax(product.getPrice()),product.isImported());
            }
            listTaxesResponse.add(new ProductWithTaxes(product.getQuantity(),product.getProductName(),totalPrice));
        }*/

		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(response)
				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
				.build();
    }
}
