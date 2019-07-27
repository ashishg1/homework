package com.loyalty.homework.services;

import com.google.common.collect.Maps;
import com.loyalty.homework.dto.City;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public final class CityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityService.class);

    private static final Map<String, City> cityMap = Maps.newConcurrentMap();

    public static void loadCities() {
        try (final InputStream inputStream = getCityFileAsInputStream()) {
            final JSONArray jsonArray = (JSONArray) new JSONParser().parse(new InputStreamReader(inputStream));
            jsonArray
                    .forEach(arrayItem -> {
                        final City city = new City();
                        final String cityName = ((JSONObject) arrayItem).get("name") + "," + ((JSONObject) arrayItem).get("country");
                        city.setCity(cityName);
                        final JSONObject coord = (JSONObject) ((JSONObject) arrayItem).get("coord");
                        city.setLatitude(coord.get("lat").toString());
                        city.setLongitude(coord.get("lon").toString());
                        cityMap.put(cityName, city);
                    });
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static City getCity(final String cityName) {
        return cityMap.get(cityName);
    }

    public static String getLiveTemperature(final String city) {
        try {
            final HttpClient client = HttpClientBuilder.create().build();
            final String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=9de243494c0b295cca9337e1e96b00e2";
            final HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                final String kelvinStringTemp = ((JSONObject) ((JSONObject) new JSONParser()
                        .parse(IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset())))
                        .get("main")).get("temp").toString();
                final BigDecimal temp = BigDecimal.valueOf(Double.valueOf(kelvinStringTemp) - 273).setScale(2, RoundingMode.HALF_UP);
                return temp.toString() + "C";
            }
        } catch (Exception ex) {
            LOGGER.error("Temperature could not be retrieved", ex);
        }
        return "N/A";
    }

    private static InputStream getCityFileAsInputStream() throws IOException {
        try {
            return new GZIPInputStream(new FileInputStream(ResourceUtils.getFile("classpath:city.list.json.gz")));
        } catch (final FileNotFoundException fnfe) {
            return new GZIPInputStream(new ClassPathResource("classpath:city.list.json.gz").getInputStream());
        }
    }

}
