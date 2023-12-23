package com.example.dcs.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dcs.model.WakandaPayoutData;
import com.opencsv.exceptions.CsvValidationException;

@RestController
@RequestMapping("/api")
public class PayoutController {

    @PostMapping("/payout")
    public ResponseEntity<String> submitPayoutData(@RequestBody WakandaPayoutData payoutRecord) throws FileNotFoundException, IOException, CsvValidationException{
        if (payoutRecord.getCompanyTaxNumber() == null ||
            payoutRecord.getPaymentDate() == null ||
            payoutRecord.getAmount() == null) {
            return ResponseEntity.badRequest().body("All fields are mandatory");
        }
        return ResponseEntity.ok("Payout data submitted successfully");
    }
}