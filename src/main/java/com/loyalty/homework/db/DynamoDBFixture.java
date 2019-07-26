package com.loyalty.homework.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

/**
 * This class demonstrates how to use DynamoDB Local as a test fixture.
 *
 * @author Alexander Patrikalakis
 */
public abstract class DynamoDBFixture {

    public AmazonDynamoDB startDynamoDB() throws Exception {
        return getDynamoClient();
    }

    protected abstract AmazonDynamoDB getDynamoClient();
}