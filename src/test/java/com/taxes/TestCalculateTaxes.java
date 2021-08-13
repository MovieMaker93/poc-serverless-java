package com.taxes;


import com.amazonaws.services.lambda.runtime.Context;
import com.taxes.controller.ApiGatewayResponse;
import com.taxes.controller.CalculateTaxesHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


class TestCalculateTaxes {

    private static final String BODY = "body";
    private static final int HTTP_200 = 200;
    private static final int HTTP_500 = 500;

    @ParameterizedTest
    @CsvSource({
            "/mock/request/input-request-1.json, /mock/response/response-1.json",
            "/mock/request/input-request-2.json, /mock/response/response-2.json",
            "/mock/request/input-request-3.json, /mock/response/response-3.json"
    })
    void testRequests(String request, String response) throws IOException {
        ApiGatewayResponse result = getApiGatewayResponse(getResourceAsStream(request));
        assertEquals(HTTP_200, result.getStatusCode());
        assertEquals(convertInputStreamToString(getResourceAsStream(response),StandardCharsets.UTF_8.name()),result.getBody());
    }

    @Test
    void testRequestFail() throws IOException {
        ApiGatewayResponse result = getApiGatewayResponse(getResourceAsStream("/mock/request/input-request-fail.json"));
        assertEquals(HTTP_500, result.getStatusCode());
    }

    private ApiGatewayResponse getApiGatewayResponse(InputStream request) throws IOException {
        HashMap<String,Object> event = new HashMap<>();
        String inputRequest = convertInputStreamToString(request, StandardCharsets.UTF_8.name());
        event.put(BODY, inputRequest);
        Context context = new TestContext();
        CalculateTaxesHandler handler = new CalculateTaxesHandler();
        return handler.handleRequest(event, context);
    }

    private InputStream getResourceAsStream(String path) {
        return getClass().getResourceAsStream(path);
    }

    private String convertInputStreamToString(InputStream inputStream, String coding) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = inputStream.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        return result.toString(coding);
    }
}

