package com.morbis.service.external.tax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EmptyTaxService implements TaxService {

    private final Logger logger = LoggerFactory.getLogger(EmptyTaxService.class);

    @Override
    public double getTaxRate(double revenueAmount) {
        logger.info("called get tax rate using EmptyTaxService, with revenueAmount :" + revenueAmount);
        return 0;
    }
}
