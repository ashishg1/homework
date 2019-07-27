package com.loyalty.homework.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.loyalty.homework.db.DBSupplier;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Base for simple Dynamo operations
 *
 * @param <R> The table pojo that declares hash, range keys and the table name
 */
abstract class DynamoDAO<R> {

    private final DBSupplier dbSupplier;

    private DynamoDBMapper dynamoDBMapper = null;

    /**
     * Constructor for DynamodDAO
     */
    DynamoDAO() {
        this.dbSupplier = new DBSupplier();
    }

    /**
     * Method used to get a single row from dynamo table given a range and hash key
     *
     * @param id    Hash key
     * @param range Range Key
     * @param clazz The table
     * @return The object row retrieved
     */
    @Nullable
    R get(@NonNull final String id, @Nullable final String range, @NonNull Class<R> clazz) {
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

    /**
     * This method saved a single object to dynamo db
     *
     * @param r object to be saved
     */
    public void put(@NonNull final R r) {
        initDBMapper();
        final DynamoDBMapperConfig.Builder builder = dbSupplier.getConfigBuilder();
        builder.setSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER);
        dynamoDBMapper.save(r, builder.build());
    }

    /**
     * Used to query dynamo db and return multiple objects based on Hash hey value
     *
     * @param clazz     The type of object
     * @param project   all the column names that should be returned by dynamodb for this query
     * @param id        The object containing the hash key to be retrieved
     * @param ascending The ordering of the result, false is descending
     * @param <T>       The type of object that will be saved in the list
     * @return The list of returned objects
     */
    @NonNull
    <T> List<T> getAll(@NonNull final Class<T> clazz,
                       @NonNull final String project,
                       @NonNull final T id,
                       final boolean ascending) {
        initDBMapper();
        final DynamoDBMapperConfig.Builder builder = dbSupplier.getConfigBuilder();
        return dynamoDBMapper.query(clazz,
                new DynamoDBQueryExpression<T>()
                        .withConsistentRead(true)
                        .withHashKeyValues(id)
                        .withScanIndexForward(ascending)
                        .withProjectionExpression(project), builder.build());

    }
}
