package com.loyalty.homework.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import static com.amazonaws.SDKGlobalConfiguration.ACCESS_KEY_SYSTEM_PROPERTY;
import static com.amazonaws.SDKGlobalConfiguration.SECRET_KEY_SYSTEM_PROPERTY;

/**
 * This class demonstrates how to use DynamoDB Local as a test fixture.
 *
 * @author Alexander Patrikalakis
 */
public abstract class DynamoDBFixture {

    private final String accessKey;
    private final String secretKey;

    protected DynamoDBFixture(final String accessKey, final String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public AmazonDynamoDB startDynamoDB() throws Exception {
        System.setProperty(ACCESS_KEY_SYSTEM_PROPERTY, accessKey);
        System.setProperty(SECRET_KEY_SYSTEM_PROPERTY, secretKey);
        return getDynamoClient();
    }

    protected abstract AmazonDynamoDB getDynamoClient();


}