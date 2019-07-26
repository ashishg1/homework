package com.loyalty.homework.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@DynamoDBDocument
public abstract class Message {

    @DynamoDBRangeKey
    private String messageId = System.currentTimeMillis() + UUID.randomUUID().toString();

    private String message;

    private String city = "";

}
