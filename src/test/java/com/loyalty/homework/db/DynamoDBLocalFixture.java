package com.loyalty.homework.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;

import static com.amazonaws.SDKGlobalConfiguration.ACCESS_KEY_SYSTEM_PROPERTY;
import static com.amazonaws.SDKGlobalConfiguration.SECRET_KEY_SYSTEM_PROPERTY;

public class DynamoDBLocalFixture extends DynamoDBFixture {

    private static final String ACCESS_KEY = "accessKey";
    private static final String SECRET_KEY = "secretKey";

    public DynamoDBLocalFixture() {
        super();
        System.setProperty(ACCESS_KEY_SYSTEM_PROPERTY, ACCESS_KEY);
        System.setProperty(SECRET_KEY_SYSTEM_PROPERTY, SECRET_KEY);
        System.setProperty("sqlite4java.library.path", "native-libs");
    }

    @Override
    protected AmazonDynamoDB getDynamoClient() {
        return DynamoDBEmbedded.create().amazonDynamoDB();
    }
}
