package com.loyalty.homework.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;

public class DynamoDBLocalFixture extends DynamoDBFixture {

    private static final String ACCESS_KEY = "accessKey";
    private static final String SECRET_KEY = "secretKey";

    public DynamoDBLocalFixture() {
        super(ACCESS_KEY, SECRET_KEY);
        System.setProperty("sqlite4java.library.path", "native-libs");
    }

    @Override
    protected AmazonDynamoDB getDynamoClient() {
        return DynamoDBEmbedded.create().amazonDynamoDB();
    }
}
