package com.loyalty.homework.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.loyalty.homework.dto.Post;
import com.loyalty.homework.dto.Reply;
import org.springframework.beans.factory.annotation.Autowired;

public class DynamoDBOperations {

    private final DBSupplier dbSupplier;

    public DynamoDBOperations(@Autowired DBSupplier dbSupplier) {
        this.dbSupplier = dbSupplier;
    }

    public void createTables(boolean delete) throws Exception {
        final AmazonDynamoDB dynamodb = dbSupplier.getDynamoDB();
        final DynamoDBMapper mapper = new DynamoDBMapper(dynamodb, dbSupplier.getConfigBuilder().build());
        createTable(delete, dynamodb, mapper, Post.class);
        createTable(delete, dynamodb, mapper, Reply.class);
    }

    private <T> void createTable(boolean delete, final AmazonDynamoDB dynamodb, final DynamoDBMapper mapper, Class<T> clazz) throws InterruptedException {

        final String tableName = dbSupplier.getTableNamePrefix() + clazz.getDeclaredAnnotation(DynamoDBTable.class).tableName();
        boolean tableExists = dynamodb.listTables().getTableNames().contains(tableName);
        if (delete && tableExists) {
            System.out.println("Deleting " + tableName + " table");
            dynamodb.deleteTable(tableName);
            try {
                DescribeTableResult describeTableResult = dynamodb.describeTable(tableName);
                while (describeTableResult.getTable().getTableStatus().equals("DELETING")) {
                    Thread.sleep(1000);
                    describeTableResult = dynamodb.describeTable(tableName);
                }
            } catch (ResourceNotFoundException e) {
                tableExists = false;
            }
        }
        if (!tableExists) {
            System.out.println("Creating " + tableName + " table");
            CreateTableRequest request = mapper.generateCreateTableRequest(clazz);
            request.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
            dynamodb.createTable(request);
            DescribeTableResult describeTableResult = dynamodb.describeTable(tableName);
            while (describeTableResult.getTable().getTableStatus().equals("CREATING")) {
                Thread.sleep(1000);
                describeTableResult = dynamodb.describeTable(tableName);
            }
        }
    }

}
