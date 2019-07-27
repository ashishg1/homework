package com.loyalty.homework.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.loyalty.homework.db.DBSupplier;

import java.util.List;

public abstract class DynamoDAO<R> {

    private DBSupplier dbSupplier;

    private DynamoDBMapper dynamoDBMapper = null;

    DynamoDAO() {
        this.dbSupplier = new DBSupplier();
    }

    R get(String id, String range, Class<R> clazz) {
        initDBMapper();
        final DynamoDBMapperConfig.Builder builder = dbSupplier.getConfigBuilder();
        builder.setConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);
        if (range == null) {
            return dynamoDBMapper.load(clazz, id, builder.build());
        } else {
            return dynamoDBMapper.load(clazz, id, range, builder.build());
        }
    }

    private void initDBMapper() {
        if (dynamoDBMapper == null) {
            this.dynamoDBMapper = new DynamoDBMapper(dbSupplier.getDynamoDB());
        }
    }

    public void put(R r) {
        initDBMapper();
        final DynamoDBMapperConfig.Builder builder = dbSupplier.getConfigBuilder();
        builder.setSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER);
        dynamoDBMapper.save(r, builder.build());
    }

    R getOrDefault(String id, String range, Class<R> clazz, R defaultValue) {
        initDBMapper();
        R r;
        try {
            r = get(id, range, clazz);
        } catch (Exception e) {
            r = null;
            System.err.println(e.getMessage());
        }
        return r != null ? r : defaultValue;
    }

    <T> List<T> getAll(Class<T> clazz, final String project, final T id, final boolean ascending) {
        initDBMapper();
        final DynamoDBMapperConfig.Builder builder = dbSupplier.getConfigBuilder();
        if (id == null) {
            return dynamoDBMapper.scan(clazz,
                    new DynamoDBScanExpression()
                            .withConsistentRead(true)
                            .withProjectionExpression(project), builder.build());
        } else {
            return dynamoDBMapper.query(clazz,
                    new DynamoDBQueryExpression<T>()
                            .withConsistentRead(true)
                            .withHashKeyValues(id)
                            .withScanIndexForward(ascending)
                            .withProjectionExpression(project), builder.build());
        }
    }
}
