package com.example.dcs.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PayoutRecord {
    protected String companyTaxNumber;
    protected String paymentDate;
    protected String amount;
}
