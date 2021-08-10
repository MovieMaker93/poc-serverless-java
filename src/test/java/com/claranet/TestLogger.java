package com.claranet;

import com.claranet.controller.GetTaxesHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class TestLogger implements LambdaLogger {
    private static final Logger logger = LogManager.getLogger(GetTaxesHandler.class);
    public void log(String message){
        logger.info(message);
    }
    public void log(byte[] message){
        logger.info(new String(message));
    }
}