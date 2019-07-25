package com.loyalty.homework.db;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class DynamoDBLiveFixture extends DynamoDBFixture {


    public DynamoDBLiveFixture(final String accessKey, final String secretKey) {
        super(accessKey, secretKey);
    }

    @Override
    protected AmazonDynamoDB getDynamoClient() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();
    }
}
