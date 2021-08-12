package com.claranet;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class TestContext implements Context {

    public TestContext() {
    }

    public String getAwsRequestId() {
        return new String("35a8e0b1-7603-4cb3-92db-0c990cc97059");
    }

    public String getLogGroupName() {
        return new String("/aws/lambda/taxes-exercise-dev-calculateTaxes");
    }

    public String getLogStreamName() {
        return new String("2020/02/26/[$LATEST]704f8dxmpla04097b9134246b8438f1a");
    }

    public String getFunctionName() {
        return new String("taxes-exercise-dev-calculateTaxes");
    }

    public String getFunctionVersion() {
        return new String("$LATEST");
    }

    public String getInvokedFunctionArn() {
        return new String("arn:aws:lambda:eu-south-1:153542261860:function:taxes-exercise-dev-calculateTaxes");
    }

    public CognitoIdentity getIdentity() {
        return null;
    }

    public ClientContext getClientContext() {
        return null;
    }

    public int getRemainingTimeInMillis() {
        return 300000;
    }

    public int getMemoryLimitInMB() {
        return 512;
    }

    public LambdaLogger getLogger() {
        return new TestLogger();
    }
}