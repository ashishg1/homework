package com.loyalty.homework.dto;


import lombok.*;

/**
 * City Bean
 */
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Getter
    @Setter
    private String city;

    @Getter
    @Setter
    private String latitude;

    @Getter
    @Setter
    private String longitude;

    @Getter
    @Setter
    private String curTemp;

    public String toString() {
        return "[" + city + " - lat:" + latitude + " lon:" + longitude + " - Temp:" + curTemp + "]";
    }
}
