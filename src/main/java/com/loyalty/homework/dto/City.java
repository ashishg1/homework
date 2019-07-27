package com.loyalty.homework.dto;


import com.loyalty.homework.services.CityService;
import lombok.Setter;


public class City {

    @Setter
    private String city;

    @Setter
    private String latitude;

    @Setter
    private String longitude;

    private String curTemp;

    public String toString() {
        return "[" + city + " - lat:" + latitude + " lon:" + longitude + " - Temp:" + CityService.getLiveTemperature(city) + "]";
    }
}
