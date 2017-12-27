package com.bank.account;

/**
 * AccountService interface specifying balance checks and withdrawals.
 */
public interface AccountService {
    public Double checkBalance(String accountNo);

    public Double withdraw(String accountNo, double amount);
}