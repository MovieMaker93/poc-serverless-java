package com.claranet;


import com.amazonaws.services.lambda.runtime.Context;
import com.claranet.controller.ApiGatewayResponse;
import com.claranet.controller.GetTaxesHandler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


public class TestGetTaxes {

    private static final String BODY = "body";
    private static final int HTTP_OK = 200;

    @Test
    void testRequest1() throws IOException {
        ApiGatewayResponse result = getApiGatewayResponse(getResourceAsStream("/mock/request/input-request-1.json"));
        assertEquals(HTTP_OK, result.getStatusCode());
        assertEquals(convertInputStreamToString(getResourceAsStream("/mock.response/response-1.json"),StandardCharsets.UTF_8.name()),result.getBody());
    }

    @Test
    void testRequest2() throws IOException {
        ApiGatewayResponse result = getApiGatewayResponse(getResourceAsStream("/mock/request/input-request-2.json"));
        assertEquals(HTTP_OK, result.getStatusCode());
        assertEquals(convertInputStreamToString(getResourceAsStream("/mock.response/response-1.json"),StandardCharsets.UTF_8.name()),result.getBody());
    }

    @Test
    void testRequest3() throws IOException {
        ApiGatewayResponse result = getApiGatewayResponse(getResourceAsStream("/mock/request/input-request-3.json"));
        assertEquals(HTTP_OK, result.getStatusCode());
        assertEquals(convertInputStreamToString(getResourceAsStream("/mock.response/response-1.json"),StandardCharsets.UTF_8.name()),result.getBody());
    }

    private ApiGatewayResponse getApiGatewayResponse(InputStream request) throws IOException {
        HashMap<String,Object> event = new HashMap<>();
        String inputRequest = convertInputStreamToString(request, StandardCharsets.UTF_8.name());
        event.put(BODY, inputRequest);
        Context context = new TestContext();
        GetTaxesHandler handler = new GetTaxesHandler();
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

