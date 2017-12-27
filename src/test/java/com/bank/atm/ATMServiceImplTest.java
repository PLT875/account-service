package com.bank.atm;

import com.bank.account.AccountServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

/*
 * Test suite for ATMServiceImpl.
 */
public class ATMServiceImplTest {

    @Test
    public void testReplenish() {
        ATMServiceImpl service = new ATMServiceImpl();
        service.replenish(2, 4, 6, 8);

        Map<Integer, Integer> notes = service.getNotes();
        assertEquals(2, notes.get(5).intValue());
        assertEquals(4 , notes.get(10).intValue());
        assertEquals(6 , notes.get(20).intValue());
        assertEquals(8 , notes.get(50).intValue());

        service.replenish(4, 6, 8, 10);
        assertEquals(6, notes.get(5).intValue());
        assertEquals(10 , notes.get(10).intValue());
        assertEquals(14 , notes.get(20).intValue());
        assertEquals(18 , notes.get(50).intValue());
    }

    @Test
    public void testCheckBalance() {
        ATMServiceImpl service = new ATMServiceImpl();

        String balance01001 = service.checkBalance("01001");
        assertEquals("Balance: £2738.59", balance01001);

        String balance01002 = service.checkBalance("01002");
        assertEquals("Balance: £23.00", balance01002);

        String balance01003 = service.checkBalance("01003");
        assertEquals("Balance: £0.00", balance01003);
    }

    @Test
    public void testUpdateMultiples() {
        ATMServiceImpl service = new ATMServiceImpl();
        service.replenish(2, 4, 6, 8);

        Integer[] dispensed = new Integer[]{ 5, 10, 10, 10, 20, 20, 20, 50 };
        service.updateNotes(dispensed);

        Map<Integer, Integer> notes = service.getNotes();
        assertEquals(1, notes.get(5).intValue());
        assertEquals(1, notes.get(10).intValue());
        assertEquals(3, notes.get(20).intValue());
        assertEquals(7, notes.get(50).intValue());
    }

    @Test
    public void testWithdrawNo5s() {
        ATMServiceImpl service = new ATMServiceImpl();
        service.replenish(0, 2, 2, 2);

        Integer[] dispensed = service.withdraw("01001", 35);
        assertNull(dispensed);
    }

    @Test
    public void testWithdrawWhenExceedsBalance() {
        ATMServiceImpl service = new ATMServiceImpl();
        service.replenish(2, 2, 2, 2);

        Integer[] dispensed = service.withdraw("01002", 25);
        assertNull(dispensed);
    }

    @Test
    public void testWithdraw55ThenWithdraw30() {
        ATMServiceImpl service = new ATMServiceImpl();
        service.replenish(2, 3, 4, 5);

        Integer[] dispense55 = service.withdraw("01001", 55);
        assertTrue(Arrays.equals(new Integer[]{5, 50}, dispense55));

        Integer[] dispense30 = service.withdraw("01001", 30);
        assertTrue(Arrays.equals(new Integer[]{20, 10}, dispense30));

        Map<Integer, Integer> notes = service.getNotes();

        assertEquals(1, notes.get(5).intValue());
        assertEquals(2, notes.get(10).intValue());
        assertEquals(3, notes.get(20).intValue());
        assertEquals(4, notes.get(50).intValue());
    }

    @Test
    public void testWithdraw80() {
        ATMServiceImpl service = new ATMServiceImpl();
        service.replenish(2, 3, 3, 3);

        Integer[] dispense30 = service.withdraw("01001", 80);
        assertTrue(Arrays.equals(new Integer[]{5, 5, 50, 20}, dispense30));

        Map<Integer, Integer> notes = service.getNotes();

        assertEquals(0, notes.get(5).intValue());
        assertEquals(3, notes.get(10).intValue());
        assertEquals(2, notes.get(20).intValue());
        assertEquals(2, notes.get(50).intValue());
    }


}
