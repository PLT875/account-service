package com.bank.account;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/*
 * Test suite for AccountServiceImpl.
 */
public class AccountServiceImplTest {
    private AccountServiceImpl service;

    @Before
    public void init() throws Exception {
        service = new AccountServiceImpl();
    }

    @Test
    public void testCheckBalance() {
        double balance01001 = service.checkBalance("01001").doubleValue();
        assertEquals(2738.59, balance01001, .1);

        double balance01002 = service.checkBalance("01002").doubleValue();
        assertEquals(23.00, balance01002, .1);

        double balance01003 = service.checkBalance("01003").doubleValue();
        assertEquals(0.00, balance01003, .1);

        Double balanceUnknown = service.checkBalance("unknown");
        assertNull(balanceUnknown);
    }

    @Test
    public void testWithdraw() {
        service.withdraw("01001", 700.00).doubleValue();
        double balance01001 = service.checkBalance("01001").doubleValue();
        assertEquals(2038.59, balance01001, .1);

        service.withdraw("01002", 20.00).doubleValue();
        double balance01002 = service.checkBalance("01002").doubleValue();
        assertEquals(3.00, balance01002, .1);

        service.withdraw("01003", 5.00).doubleValue();
        double balance01003 = service.checkBalance("01003").doubleValue();
        assertEquals(0.00, balance01003, .1);

        Double unknown = service.withdraw("unknown", 99.00);
        assertNull(unknown);
    }
}
