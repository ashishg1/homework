package com.loyalty.homework.services;

import com.loyalty.homework.dto.City;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CityServiceTest {

    private static final String CITY_API_RESPONSE = "{\"coord\":{\"lon\":-79.39,\"lat\":43.65},\"weather\":[{\"id\":521,\"main\":\"Rain\",\"description\":\"shower rain\",\"icon\":\"09d\"}],\"base\":\"stations\",\"main\":{\"temp\":301.21,\"pressure\":1021,\"humidity\":65,\"temp_min\":299.15,\"temp_max\":303.71},\"visibility\":24140,\"wind\":{\"speed\":4.6,\"deg\":200},\"clouds\":{\"all\":75},\"dt\":1564249930,\"sys\":{\"type\":1,\"id\":1002,\"message\":0.0091,\"country\":\"CA\",\"sunrise\":1564221662,\"sunset\":1564274811},\"timezone\":-14400,\"id\":6167865,\"name\":\"Toronto\",\"cod\":200}";

    @Test
    public void testJSONConversion() {
        final CityService cityService = mock(CityService.class);
        when(cityService.getApiResponseJson("Toronto,CA")).thenReturn(CITY_API_RESPONSE);
        final City city = CityService.getCityFromWeb("Toronto,CA", cityService);
        assertThat("Lat was not set", city.getLatitude(), is("43.65"));
        assertThat("Lon was not set", city.getLongitude(), is("-79.39"));
        assertThat("CurTemp was not set", city.getCurTemp(), is("28.06C"));
    }

    @Test
    public void testAPIFailure() {
        final CityService cityService = mock(CityService.class);
        when(cityService.getApiResponseJson("Toronto,CA")).thenReturn(null);
        final City city = CityService.getCityFromWeb("Toronto,CA", cityService);
        assertThat("Lat was not set", city.getLatitude(), is("N/A"));
        assertThat("Lon was not set", city.getLongitude(), is("N/A"));
        assertThat("CurTemp was not set", city.getCurTemp(), is("N/A"));
    }
}