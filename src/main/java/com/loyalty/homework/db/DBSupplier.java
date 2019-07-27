package com.loyalty.homework.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.apache.commons.lang3.Validate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * The supplier that holds the connection to the dynamo db and configuration for the current env
 */
@Repository
public class DBSupplier {

    private static AmazonDynamoDB dynamoDB = null;
    private static String currentEnvName = null;

    /**
     * This method should be called at startup to setup the dynamo db instance
     * All internal classes will use this connection from there on.
     * This method can only set dynamo and other configuration once
     *
     * @param dynamoDB       The dynamo db client
     * @param currentEnvName current environment name
     */
    public static void setDynamoDB(@NonNull final AmazonDynamoDB dynamoDB, @NonNull final String currentEnvName) {
        Validate.notEmpty(currentEnvName, "Current Env name must not be empty");
        if (DBSupplier.dynamoDB == null && DBSupplier.currentEnvName == null) {
            DBSupplier.dynamoDB = dynamoDB;
            DBSupplier.currentEnvName = currentEnvName;
        }
    }

    /**
     * Get dynamo db instance
     *
     * @return dynamo db client
     */
    @NonNull
    public AmazonDynamoDB getDynamoDB() {
        if (dynamoDB == null) {
            throw new IllegalStateException("DynamoDB has not been set");
        }
        return dynamoDB;
    }

    /**
     * Get the table name prefix that should be set for all dynamo db tables
     *
     * @return table name prefix
     */
    @NonNull
    String getTableNamePrefix() {
        if (currentEnvName == null) {
            throw new IllegalStateException("Current enviornment name has not been set");
        }
        return currentEnvName + "-";
    }

    /**
     * Get the DynamoDBMapperConfig builder that overrides the table name with current env prefix
     *
     * @return DynamoDBMapperConfig builder
     */
    @NonNull
    public DynamoDBMapperConfig.Builder getConfigBuilder() {
        final DynamoDBMapperConfig.Builder builder = DynamoDBMapperConfig.builder();
        builder.withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(getTableNamePrefix()));
        return builder;
    }
}
