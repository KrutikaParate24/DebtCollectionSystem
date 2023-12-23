package com.example.dcs.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GondorPayoutData extends PayoutRecord {

    private String name;

    public GondorPayoutData(String taxId, String name, String paymentDate, String paymentAmount) {
        this.companyTaxNumber = taxId;
        this.name = name;
        this.paymentDate = paymentDate;
        this.amount = paymentAmount;
    }
    
}
