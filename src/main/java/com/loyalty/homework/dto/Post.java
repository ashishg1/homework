package com.loyalty.homework.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Post Bean
 */
@SuppressWarnings("Lombok")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "posts")
public class Post extends Message {

    @DynamoDBHashKey
    private String userName;
}
