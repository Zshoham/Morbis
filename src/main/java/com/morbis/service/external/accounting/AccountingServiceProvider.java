package com.morbis.service.external.accounting;

public class AccountingServiceProvider implements AccountingService {

    private static AccountingServiceProvider provider;

    private final AccountingService service;

    public static AccountingServiceProvider getInstance() {
        if (provider == null)
            provider = new AccountingServiceProvider();

        return provider;
    }

    private AccountingServiceProvider() {
        this.service = buildService();
    }

    @Override
    public boolean addPayment(String teamName, String date, double amount) {
        return service.addPayment(teamName, date, amount);
    }

    private AccountingService buildService() {
        return new EmptyAccountingService();
    }
}
