package com.loyalty.homework.services;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * A service class to be used to statically retrieve city data
 */
public class CityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityService.class);

    private static final LoadingCache<String, City> CITY_LOADING_CACHE = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, City>() {
                        public City load(@NonNull final String city) {
                            return getCityFromWeb(city);
                        }
                    });

    /**
     * Returns the City object from either web or cache.
     * If the cache was filled within last minute then the api call is not made and value is returned from the cache instead
     *
     * @param city The city to retrieve
     * @return The city Object filled with either the data or N/A if exception occurred
     */
    @NonNull
    public static City getCity(@NonNull final String city) {
        try {
            return CITY_LOADING_CACHE.get(city);
        } catch (final Exception e) {
            //No exceptions should be possible here. If they happen they should be ignored
            return new City(city, "N/A", "N/A", "N/A");
        }
    }

    @VisibleForTesting
    static City getCityFromWeb(final String city, final CityService cityService) {
        try {
            final String apiResponseJson = cityService.getApiResponseJson(city);
            if (apiResponseJson == null) {
                throw new IllegalStateException("Bad Api");
            }
            final JSONObject jsonObject = (JSONObject) new JSONParser()
                    .parse(apiResponseJson);
            final String kelvinStringTemp = ((JSONObject) jsonObject.get("main")).get("temp").toString();
            final BigDecimal temp = BigDecimal.valueOf(Double.valueOf(kelvinStringTemp) - 273.15).setScale(2, RoundingMode.HALF_UP);
            final String curTemp = temp.toString() + "C";
            final String lat = ((JSONObject) jsonObject.get("coord")).get("lat").toString();
            final String lon = ((JSONObject) jsonObject.get("coord")).get("lon").toString();
            return new City(city, lat, lon, curTemp);
        } catch (final Exception ex) {
            LOGGER.error("API Json could not be parsed", ex);
        }
        return new City(city, "N/A", "N/A", "N/A");
    }

    private static City getCityFromWeb(final String city) {
        return getCityFromWeb(city, new CityService());
    }

    @Nullable
    @VisibleForTesting
    String getApiResponseJson(@NonNull final String city) {
        try {
            final HttpClient client = HttpClientBuilder.create().build();
            final String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=9de243494c0b295cca9337e1e96b00e2";
            final HttpGet request = new HttpGet(url);
            final HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                return IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
            } else {
                throw new IllegalStateException("API failed: " + response.getStatusLine().getStatusCode());
            }
        } catch (final Exception ex) {
            LOGGER.error("Temperature could not be retrieved", ex);
        }
        return null;
    }

}
