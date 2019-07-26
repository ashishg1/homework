package com.loyalty.homework.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class City {

    private String city;
    private String latitude;
    private String longitude;
    private String curTemp;

}
