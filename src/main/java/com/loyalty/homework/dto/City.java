package com.loyalty.homework.dto;


import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
public class City {

    @Setter
    private String city;

    @Setter
    private String latitude;

    @Setter
    private String longitude;

    @Setter
    private String curTemp;

    public String toString() {
        return "[" + city + " - lat:" + latitude + " lon:" + longitude + " - Temp:" + curTemp + "]";
    }
}
