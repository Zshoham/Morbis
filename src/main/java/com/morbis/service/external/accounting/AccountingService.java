package com.morbis.service.external.accounting;

public interface AccountingService {

    boolean addPayment(String teamName, String date, double amount);
}
