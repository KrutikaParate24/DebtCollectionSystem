package com.example.dcs.service;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PayoutFileProcessor {

    private static final Logger LOGGER = Logger.getLogger(PayoutFileProcessor.class.getName());

    @Scheduled(cron = "0 59 23 * * *") // Scheduled to run daily at 23:59
    public void processDailyPayoutFiles() throws IOException {
        LOGGER.info("CSV File Processing Started.");

        CountryDataProcessor processor = DataProcessorFactory.getProcessor("Wakanda");
        processor.processPayoutData();

        LOGGER.info("CSV File Processing Completed.");
    }

    /* End to End Testing */
    public void processFilesForTesting() throws IOException {
        processDailyPayoutFiles();
    }

}