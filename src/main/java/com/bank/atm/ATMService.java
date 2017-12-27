package com.bank.atm;

public interface ATMService {
    public void replenish(int mul5s, int mul10s, int mul20s, int mul50s);

    public String checkBalance(String accountNo);

    public Integer[] withdraw(String accountNo, int amount);

}
