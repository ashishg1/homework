package com.loyalty.homework.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The Reply Bean
 */
@SuppressWarnings("Lombok")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "replies")
public class Reply extends Message {

    @DynamoDBHashKey
    private String rootMessageId;

    private int messageDepth = 1;

    private String userName = "";

    public Reply(String rootMessageId) {
        this.rootMessageId = rootMessageId;
    }
}
