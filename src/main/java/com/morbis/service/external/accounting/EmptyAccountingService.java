package com.morbis.service.external.accounting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmptyAccountingService implements AccountingService {

    private final Logger logger = LoggerFactory.getLogger(EmptyAccountingService.class);

    @Override
    public boolean addPayment(String teamName, String date, double amount) {

        return true;
    }
}
