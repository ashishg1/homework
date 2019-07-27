package com.loyalty.homework.services;

import com.loyalty.homework.dto.City;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;

public final class CityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityService.class);

    public static City getCity(final String city) {
        try {
            final HttpClient client = HttpClientBuilder.create().build();
            final String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=9de243494c0b295cca9337e1e96b00e2";
            final HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                final JSONObject jsonObject = (JSONObject) new JSONParser()
                        .parse(IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset()));
                final String kelvinStringTemp = ((JSONObject) jsonObject.get("main")).get("temp").toString();
                final BigDecimal temp = BigDecimal.valueOf(Double.valueOf(kelvinStringTemp) - 273).setScale(2, RoundingMode.HALF_UP);
                final String curTemp = temp.toString() + "C";
                final String lat = ((JSONObject) jsonObject.get("coord")).get("lat").toString();
                final String lon = ((JSONObject) jsonObject.get("coord")).get("lat").toString();
                return new City(city, lat, lon, curTemp);
            }
        } catch (Exception ex) {
            LOGGER.error("Temperature could not be retrieved", ex);
        }
        return new City(city, "N/A", "N/A", "N/A");
    }

}
