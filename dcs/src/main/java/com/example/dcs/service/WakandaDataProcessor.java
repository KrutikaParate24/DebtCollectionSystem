package com.example.dcs.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.dcs.model.WakandaPayoutData;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class WakandaDataProcessor implements CountryDataProcessor{

    private static final Logger LOGGER = Logger.getLogger(WakandaDataProcessor.class.getName());

    private String DIRECTORY_PATH;
    private static final char FIELD_DELIMITER = ';';
    private static final char TEXT_DELIMITER = '\"';
    private static final DateTimeFormatter FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String CSV_FILE_PREFIX = "WK_payouts_";
    private static final String CSV_FILE_EXTENSION = ".csv";

    private Properties prop;

    public void setProperties(Properties properties) {
        this.prop = properties;
    }

    @Override
    public void processPayoutData() throws IOException {
        LOGGER.info("CSV File Processing Started Successfully.");

        prop = new Properties();

        FileInputStream file = new FileInputStream("dcs//src//main//resources//application.properties");
        prop.load(file);
        DIRECTORY_PATH = prop.getProperty("wakanda.unprocessed");

        LocalDate today = LocalDate.now();
        String expectedFileName = buildExpectedFileName(today);
        Path filePath = Paths.get(DIRECTORY_PATH, expectedFileName);

        if (Files.exists(filePath)) {
            try {
                List<WakandaPayoutData> payoutDataList = readAndParseCSVFile(filePath);
                payoutDataList.forEach(this::submitPayoutData);

                LOGGER.info("File processed successfully.");
            } catch(Exception ex){
                LOGGER.log(Level.WARNING, "Failed to process file : " + ex);
            }
        }

        LOGGER.info("CSV File Processing Completed Successfully.");
    }

    public String buildExpectedFileName(LocalDate date) {
        LocalDateTime dateTime = date.atTime(23, 59, 59); // Assuming file is named with 23:59:59 timestamp
        return CSV_FILE_PREFIX + FILE_NAME_FORMATTER.format(dateTime) + CSV_FILE_EXTENSION;
    }

    public List<WakandaPayoutData> readAndParseCSVFile(Path filePath) throws FileNotFoundException, IOException, CsvValidationException {
        List<WakandaPayoutData> payoutDataList = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath.toFile(), StandardCharsets.ISO_8859_1))
                .withCSVParser(new CSVParserBuilder()
                    .withSeparator(FIELD_DELIMITER)
                    .withQuoteChar(TEXT_DELIMITER)
                    .build())
                .withSkipLines(1)
                .build()) {
                    String[] fields;
                    while ((fields = reader.readNext()) != null) {
                        if (fields.length < 5) {
                            continue;
                        }
                            WakandaPayoutData payout = new WakandaPayoutData();
                            payout.setCompanyTaxNumber(fields[1]);
                            payout.setPaymentDate(fields[3]);
                            payout.setAmount(fields[4]);
                            payoutDataList.add(payout);
                        }
                    }
        return payoutDataList;
    }

    public void submitPayoutData(WakandaPayoutData data) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/payout"; //http://intrum.mocklab.io/payout
        restTemplate.postForObject(apiUrl, data, String.class);
    }
    
}