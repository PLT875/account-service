package com.bank.account;

import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;

/*
 * AccountServiceImpl.
 */
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = Logger.getLogger(AccountServiceImpl.class.getName());

    private Map<String, Double> accounts;

    public AccountServiceImpl() {
        this.accounts = new HashMap<String, Double>() {{
            put("01001", new Double(2738.59));
            put("01002", new Double(23.00));
            put("01003", new Double(0.00));
        }};
    }

    public Double checkBalance(String accountNo) {
        if (!accounts.containsKey(accountNo)) {
            LOGGER.info(String.format("Unknown account no. %s", accountNo));
            return null;
        }

        return accounts.get(accountNo);
    }

    public Map<String, Double> getAccounts() {
        return accounts;
    }

    public Double withdraw(String accountNo, double amount) {
        LOGGER.info(String.format("Request to withdraw %.2f from account no. " +
                "%s", amount, accountNo));

        if (!accounts.containsKey(accountNo)) {
            LOGGER.info(String.format("Unknown account no. %s", accountNo));
            return null;
        }

        if (accounts.get(accountNo).doubleValue() < amount) {
            LOGGER.info(String.format("Cannot withdraw %.2f from account " +
                    "no. %s as it exceeds balance!", amount, accountNo));

            return new Double(0.0);
        }

        Double newBalance = new Double(accounts.get(accountNo).doubleValue() -
                amount);

        LOGGER.info(String.format("Updating balance for account no. %s " +
                "to %.2f", accountNo, newBalance));

        accounts.put(accountNo, newBalance);
        return new Double(amount);
    }
}
