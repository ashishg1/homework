package com.loyalty.homework.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.springframework.lang.NonNull;

/**
 * The base Dynamo db fixture
 *
 */
public abstract class DynamoDBFixture {

    /**
     * Start the dynamo db client and connect to whichever instance the implementation is using
     *
     * @return Dynamo db client
     */
    @NonNull
    public AmazonDynamoDB startDynamoDB() {
        return getDynamoClient();
    }

    /**
     * Based on current env implemetor should create the right dynamo connection and return the client
     *
     * @return dynamo db client
     */
    @NonNull
    protected abstract AmazonDynamoDB getDynamoClient();
}