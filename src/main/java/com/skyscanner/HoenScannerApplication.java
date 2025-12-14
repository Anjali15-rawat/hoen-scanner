package com.skyscanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    public static List<SearchResult> searchResults = new ArrayList<>();

    public static void main(final String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public String getName() {
        return "hoen-scanner";
    }

    @Override
    public void initialize(final Bootstrap<HoenScannerConfiguration> bootstrap) {
        // nothing yet
    }

    @Override
    public void run(final HoenScannerConfiguration configuration,
                    final Environment environment) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        // Load rental cars
        try (InputStream carsStream = getClass().getResourceAsStream("/rental_cars.json")) {
            List<SearchResult> cars = mapper.readValue(
                    carsStream,
                    new TypeReference<List<SearchResult>>() {}
            );
            for (SearchResult r : cars) {
                searchResults.add(new SearchResult(r.getCity(), "car", r.getTitle()));
            }
        }

        // Load hotels
        try (InputStream hotelsStream = getClass().getResourceAsStream("/hotels.json")) {
            List<SearchResult> hotels = mapper.readValue(
                    hotelsStream,
                    new TypeReference<List<SearchResult>>() {}
            );
            for (SearchResult r : hotels) {
                searchResults.add(new SearchResult(r.getCity(), "hotel", r.getTitle()));
            }
        }
        //register resources
        environment.jersey().register(new SearchResource());
    }
}
