package com.qapital.bankdata.transaction;


import java.util.List;

public interface TransactionsService {

    List<Transaction> latestTransactionsForUser(Long userId);

}
