package com.loyalty.homework;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.loyalty.homework.db.DBSupplier;
import com.loyalty.homework.db.DynamoDBLiveFixture;
import com.loyalty.homework.db.DynamoDBOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.util.Arrays;


/**
 * The main spring boot application
 */
@ComponentScan(basePackages = "com.loyalty.homework")
@SpringBootApplication
public class SpringBootApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootApp.class);

    /**
     * Main method that launches the spring boot application
     *
     * @param args Arguments to be passed in
     */
    public static void main(String[] args) throws Exception {
        LOGGER.info("Starting Application");
        final Environment env = SpringApplication.run(SpringBootApp.class, args).getEnvironment();
        final boolean integration = Arrays.stream(env.getActiveProfiles()).anyMatch(profile -> profile.contains("Integration"));
        if (!integration) {
            LOGGER.info("This is not an integration enviornment. Attempting to create connection to live DynamodDB");
            final String dynamoDBAccessKey = env.getProperty("dynamoDBAccessKey");
            final String dynamoDBSecretKey = env.getProperty("dynamoDBSecretKey");
            final String currentEnvName = env.getProperty("currentEnvName");
            final AmazonDynamoDB dynamoDB = new DynamoDBLiveFixture(dynamoDBAccessKey, dynamoDBSecretKey).startDynamoDB();
            DBSupplier.setDynamoDB(dynamoDB, currentEnvName);
            LOGGER.info("Creating Tables");
            new DynamoDBOperations(new DBSupplier()).createTables(true);
            LOGGER.info("DynamoDB Set up");
        }

    }
}