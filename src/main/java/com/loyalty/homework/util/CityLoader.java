package com.loyalty.homework.util;

import com.google.common.collect.Maps;
import com.loyalty.homework.dto.City;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class CityLoader {

    private static final Map<String, City> cityMap = Maps.newConcurrentMap();

    static {
        try (final InputStream inputStream = getCityFileAsInputStream()){
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

    public static Set<String> getCities() {
        return cityMap.keySet();
    }

    public static City getCity(final String cityName) {
        return cityMap.get(cityName);
    }

    private static InputStream getCityFileAsInputStream() throws IOException {
        try {
            return new GZIPInputStream(new FileInputStream(ResourceUtils.getFile("classpath:city.list.json.gz")));
        } catch (final FileNotFoundException fnfe) {
            return new GZIPInputStream(new ClassPathResource("classpath:city.list.json.gz").getInputStream());
        }
    }

}
