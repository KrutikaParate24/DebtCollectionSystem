package com.example.dcs.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dcs.service.WakandaDataProcessor;

@RestController
@RequestMapping("/api")
public class TestingController {

    private final WakandaDataProcessor wakandaDataProcessor;

    public TestingController(WakandaDataProcessor wakandaDataProcessor) {
        this.wakandaDataProcessor = wakandaDataProcessor;
    }

    @PostMapping("/trigger-processing")
    public ResponseEntity<String> triggerFileProcessing() {
        try {
            wakandaDataProcessor.processPayoutData();
            return ResponseEntity.ok("File processing triggered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while processing files");
        }
    }
}