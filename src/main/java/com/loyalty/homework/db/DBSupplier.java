package com.loyalty.homework.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Repository;

@Repository
public class DBSupplier {

    private static AmazonDynamoDB dynamoDB = null;
    private static String currentEnvName = null;

    public static void setDynamoDB(final AmazonDynamoDB dynamoDB, final String currentEnvName) {
        Validate.notNull(dynamoDB, "DynamodDB must not be null");
        Validate.notEmpty(currentEnvName, "Current Env name must not be null or empty");
        if (DBSupplier.dynamoDB == null && DBSupplier.currentEnvName == null) {
            DBSupplier.dynamoDB = dynamoDB;
            DBSupplier.currentEnvName = currentEnvName;
        }
    }

    public AmazonDynamoDB getDynamoDB() {
        if (dynamoDB == null) {
            throw new IllegalStateException("DynamoDB has not been set");
        }
        return dynamoDB;
    }

    String getTableNamePrefix() {
        if (currentEnvName == null) {
            throw new IllegalStateException("Current enviornment name has not been set");
        }
        return currentEnvName + "-";
    }

    public DynamoDBMapperConfig.Builder getConfigBuilder() {
        final DynamoDBMapperConfig.Builder builder = DynamoDBMapperConfig.builder();
        builder.withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(getTableNamePrefix()));
        return builder;
    }
}
