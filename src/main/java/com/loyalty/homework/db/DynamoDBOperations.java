package com.loyalty.homework.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.google.common.annotations.VisibleForTesting;
import com.loyalty.homework.dto.Post;
import com.loyalty.homework.dto.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

/**
 * A helper class that creates all the tables required for the project on given dynamo db instance as required
 */
public class DynamoDBOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamoDBOperations.class);
    private final DBSupplier dbSupplier;

    /**
     * Dynamo DB constuctor
     *
     * @param dbSupplier The db supplier to supply the dynamo db client and configuration
     */
    @VisibleForTesting
    public DynamoDBOperations(@Autowired @NonNull final DBSupplier dbSupplier) {
        this.dbSupplier = dbSupplier;
    }

    /**
     * Create tables on dynamodb
     *
     * @param delete If table exists set this to true to delete them first
     * @throws Exception Any exception during table creation
     */
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
            LOGGER.info("Deleting " + tableName + " table");
            dynamodb.deleteTable(tableName);
            try {
                DescribeTableResult describeTableResult = dynamodb.describeTable(tableName);
                //Loop till the table is in the deleting stage and check every second till it is done deleting
                while (describeTableResult.getTable().getTableStatus().equals("DELETING")) {
                    Thread.sleep(1000);
                    describeTableResult = dynamodb.describeTable(tableName);
                }
            } catch (ResourceNotFoundException e) {
                tableExists = false;
            }
        }
        if (!tableExists) {
            LOGGER.info("Creating " + tableName + " table");
            CreateTableRequest request = mapper.generateCreateTableRequest(clazz);
            request.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
            dynamodb.createTable(request);
            //Loop till the table is in the creating stage and check every second till it is done creating
            DescribeTableResult describeTableResult = dynamodb.describeTable(tableName);
            while (describeTableResult.getTable().getTableStatus().equals("CREATING")) {
                Thread.sleep(1000);
                describeTableResult = dynamodb.describeTable(tableName);
            }
        }
    }

}
