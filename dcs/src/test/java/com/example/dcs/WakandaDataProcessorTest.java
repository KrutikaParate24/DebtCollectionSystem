package com.example.dcs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.client.RestTemplate;

import com.example.dcs.model.WakandaPayoutData;
import com.example.dcs.service.WakandaDataProcessor;
import com.opencsv.exceptions.CsvValidationException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WakandaDataProcessor.class, Logger.class})
public class WakandaDataProcessorTest {
    
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WakandaDataProcessor processor;

    Logger logger = Mockito.mock(Logger.class);

    private Properties properties;
    private Path testFilePath;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        properties = new Properties();
        properties.setProperty("wakanda.unprocessed", "test_data/");
        processor.setProperties(properties);

        testFilePath = Paths.get("test_data/WK_payouts_20231222_235959.csv");

        Files.createDirectories(testFilePath.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(testFilePath)) {
            writer.write("\"Company name\";\"companyTaxNumber\";\"Status\";\"paymentDate\";\"amount\"\n");
            writer.write("\"abc\";\"12345\";\"PENDING\";\"2023-12-22\";\"1000\"\n");
        }
    }

    @Test
    public void testProcessPayoutData() throws IOException, CsvValidationException {
        String expectedUrl = "http://localhost:8080/api/payout";
        Object expectedRequest = new Object();
        String expectedResponse = "OK";

        Mockito.when(restTemplate.postForObject(anyString(), any(), eq(String.class))).thenReturn(expectedResponse);

        processor.processPayoutData();

        String actualResponse = restTemplate.postForObject(expectedUrl, expectedRequest, String.class);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testBuildExpectedFileName() {
        LocalDate date = LocalDate.of(2023, 12, 22);
        String expectedFileName = processor.buildExpectedFileName(date);
        assertEquals("WK_payouts_20231222_235959.csv", expectedFileName);
    }

    @Test
    public void testReadAndParseCSVFile() throws IOException, CsvValidationException {
        List<WakandaPayoutData> payoutDataList = processor.readAndParseCSVFile(testFilePath);
        assertFalse(payoutDataList.isEmpty());
        WakandaPayoutData data = payoutDataList.get(0);
        assertEquals("12345", data.getCompanyTaxNumber());
        assertEquals("2023-12-22", data.getPaymentDate());
        assertEquals("1000", data.getAmount());
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(testFilePath);
    }
}
