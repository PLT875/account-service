package com.bank.atm;

import com.bank.account.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.logging.Logger;

/*
 * ATMServiceImpl.
 */
public class ATMServiceImpl implements ATMService {

    private static final Logger LOGGER = Logger.getLogger(ATMServiceImpl.class.getName());

    private static final String NOT_ENOUGH_CHANGE = "Cannot withdraw %d " +
            "from account no. %s not enough change!";

    private AccountService accountService;

    private Map<Integer, Integer> notes;

    public ATMServiceImpl() {
        this.accountService = new AccountServiceImpl();
        this.notes = new HashMap<Integer, Integer>() {{
            put(5, 0);
            put(10, 0);
            put(20, 0);
            put(50, 0);
        }};
    }

    public Map<Integer, Integer> getNotes() {
        return notes;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void replenish(int mul5s, int mul10s, int mul20s, int mul50s) {
        notes.put(5, notes.get(5) + mul5s);
        notes.put(10, notes.get(10) + mul10s);
        notes.put(20, notes.get(20) + mul20s);
        notes.put(50, notes.get(50) + mul50s);
    }

    public String checkBalance(String accountNo) {
        double balance = accountService.checkBalance(accountNo).doubleValue();
        return "Balance: £" + String.format("%.2f", balance);
    }

    /*
     * CurrentAccount to help track dispensed notes and what
     * is left to withdraw.
     */
    private class CurrentAccount {
        int left;
        List<Integer> dispensed;

        private CurrentAccount(int left) {
            this.left = left;
            this.dispensed = new ArrayList<Integer>();
        }

        /*
         * @param note the denomination that we hope to dispense with
         */
        private void updateDispensedWith(int note) {
            if (this.left >= 0) {
                int required = this.left / note;
                if (required > notes.get(note)) {
                    required = notes.get(note);
                }

                for (int i = 0; i < required; i++) {
                    this.dispensed.add(note);
                    this.left = this.left - (required * note);
                }
            }

        }
    }

    /*
     * Withdraws the amount from the account number. It also updates the
     * occurrence count of each note held in the ATM.
     *
     * @param accountNo
     * @param amount
     * @return a list of dispensed notes
     */
    public Integer[] withdraw(String accountNo, int amount) {
        if (amount < 20 || amount > 250) {
            return null;
        }

        CurrentAccount current = new CurrentAccount(amount);

        // even after divisible by 5?
        boolean even = ((amount / 5) % 2) == 0;

        // if odd then one 5 needed, else if even can you give two 5s?
        if (!even && notes.get(5) == 0) {
            LOGGER.info(String.format(NOT_ENOUGH_CHANGE, amount, accountNo));
            return null;
        } else if (!even && notes.get(5) >= 1) {
            current.left -= 5;
            current.dispensed.add(5);
        } else if (even && notes.get(5) >= 2) {
            current.left -= 10;
            current.dispensed.add(5);
            current.dispensed.add(5);
        }

        current.updateDispensedWith(50);
        current.updateDispensedWith(20);
        current.updateDispensedWith(10);

        if (current.left > 0) {
            LOGGER.info(String.format(NOT_ENOUGH_CHANGE, amount));
            return null;
        }

        Double w = accountService.withdraw(accountNo, (double) amount);
        // if amount to withdraw exceeds balance
        if (w.doubleValue() == 0.0) {
            return null;
        }

        Integer[] toDispense = current.dispensed.toArray(new Integer[current.dispensed.size()]);
        LOGGER.info(String.format("Dispensing %s for account no. %s",
                Arrays.toString(toDispense), accountNo));
        updateNotes(toDispense);

        return toDispense;
    }

    /*
     * Updates the note counts given what has been withdrew.
     *
     * @param notes list of 5s, 10s, 20s and 50s that have been withdrew.
     */
    public void updateNotes(Integer[] notes) {
        for (Integer m: notes) {
            this.notes.put(m, this.notes.get(m) - 1);
        }
    }
}