package com.example.dcs.service;

public class DataProcessorFactory {

    public static CountryDataProcessor getProcessor(String country) {
        switch (country) {
            case "Wakanda":
                return new WakandaDataProcessor();
            case "Gondor":
                return new GondorDataProcessor();
            // other countries...
            default:
                throw new IllegalArgumentException("No processor available for country: " + country);
        }
    }
}