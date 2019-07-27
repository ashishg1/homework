package com.loyalty.homework.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.loyalty.homework.services.CityService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * The base for Post and Reply
 */
@Data
@NoArgsConstructor
@DynamoDBDocument
public abstract class Message {

    @DynamoDBRangeKey
    private String messageId = System.currentTimeMillis() + "-" + UUID.randomUUID().toString();

    private String message;

    private String city = "";

    @DynamoDBIgnore
    @Getter(AccessLevel.NONE)
    private String cityDetails;

    /**
     * The City details string is automatically transformed on load via the CityService
     *
     * @return cityDetails
     */
    @SuppressWarnings("unused")
    public String getCityDetails() {
        if (this.city == null || this.city.isEmpty()) {
            return "";
        } else {
            final City city = CityService.getCity(this.city);
            return city.toString();
        }
    }
}
