package com.example.dcs.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WakandaPayoutData extends PayoutRecord{

    public WakandaPayoutData(String identifier, String paymentDate, String paymentAmount) {
        this.companyTaxNumber = identifier;
        this.paymentDate = paymentDate;
        this.amount = paymentAmount;
    }

    public WakandaPayoutData() {
    }
}
