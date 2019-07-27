package com.loyalty.homework.db;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.lang.NonNull;

/**
 * The Live Dynamo DB
 */
public class DynamoDBLiveFixture extends DynamoDBFixture {


    /**
     * DynamoDBLiveFixture Constructor
     */
    public DynamoDBLiveFixture() {
        super();
    }

    /**
     * Creates a standard dynamo db client and connects to US_EAST_1.
     * This instance will use the DefaultCredentialsChain and try to look for aws credentials automatically
     *
     * @return Dynamo db client
     */
    @NonNull
    @Override
    protected AmazonDynamoDB getDynamoClient() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();
    }
}
