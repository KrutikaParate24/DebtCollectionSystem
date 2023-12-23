package com.example.dcs.service;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface CountryDataProcessor {
    void processPayoutData() throws FileNotFoundException, IOException;
}
