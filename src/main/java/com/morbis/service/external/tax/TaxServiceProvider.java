package com.morbis.service.external.tax;

public class TaxServiceProvider implements TaxService {

    private static TaxServiceProvider provider;

    private final TaxService service;

    public static TaxService getInstance() {
        if (provider == null)
            provider = new TaxServiceProvider();

        return provider;
    }

    private TaxServiceProvider() {
        this.service = buildService();
    }

    @Override
    public double getTaxRate(double revenueAmount) {
        return service.getTaxRate(revenueAmount);
    }

    private TaxService buildService() {
        return new EmptyTaxService();
    }
}
