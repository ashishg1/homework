package com.loyalty.homework.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@DynamoDBTable(tableName = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @DynamoDBHashKey
    private String messageId;

    @DynamoDBRangeKey
    private long time;
    private String message;


    public Message(final String message) {
        this.messageId = UUID.randomUUID().toString();
        this.message = message;
        this.time = System.currentTimeMillis();
    }

}
