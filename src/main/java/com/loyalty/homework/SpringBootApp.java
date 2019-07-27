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
     * @param args First argument will be taken as the profile. Pass "Integration" to not setup dynamo on live server
     */
    public static void main(String[] args) throws Exception {
        LOGGER.info("Starting Application");
        final Environment env = SpringApplication.run(SpringBootApp.class, args).getEnvironment();
        if (args.length > 0) {
            System.setProperty("spring.profiles.active", args[0]);
        }
        final boolean integration = Arrays.stream(env.getActiveProfiles()).anyMatch(profile -> profile.contains("Integration"));
        if (!integration) {
            LOGGER.info("This is not an integration enviornment. Attempting to create connection to live DynamodDB");
            String currentEnvName = env.getProperty("currentEnvName");
            if (currentEnvName == null) {
                currentEnvName = "homework";
            }
            final AmazonDynamoDB dynamoDB = new DynamoDBLiveFixture().startDynamoDB();
            DBSupplier.setDynamoDB(dynamoDB, currentEnvName);
            LOGGER.info("Creating Tables");
            new DynamoDBOperations(new DBSupplier()).createTables(false);
            LOGGER.info("DynamoDB Set up");
        }

    }
}