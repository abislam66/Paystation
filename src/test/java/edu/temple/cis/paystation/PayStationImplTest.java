package edu.temple.cis.paystation;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class PayStationImplTest {

    private PayStation ps;

    @Before
    public void setUp() {
        ps = new PayStationImpl(new Linear1RateStrategy());
    }

    @Test
    public void shouldDisplay2MinFor5Cents() throws IllegalCoinException {
        ps.addPayment(5);
        assertEquals(2, ps.readDisplay());
    }

    @Test
    public void shouldDisplay10MinFor25Cents() throws IllegalCoinException {
        ps.addPayment(25);
        assertEquals(10, ps.readDisplay());
    }

    @Test
    public void shouldAccumulateTimeAcrossCoins() throws IllegalCoinException {
        ps.addPayment(5);
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals(16, ps.readDisplay());
    }

    @Test(expected = IllegalCoinException.class)
    public void shouldRejectIllegalCoin() throws IllegalCoinException {
        ps.addPayment(17);
    }

    @Test
    public void illegalCoinShouldNotChangeDisplay() throws IllegalCoinException {
        ps.addPayment(25);
        try {
            ps.addPayment(1);
            fail("Expected IllegalCoinException");
        } catch (IllegalCoinException expected) {
            // expected
        }
        assertEquals(10, ps.readDisplay());
    }

    @Test
    public void buyShouldReturnReceiptWithTimeBought() throws IllegalCoinException {
        ps.addPayment(25);
        ps.addPayment(25);
        Receipt receipt = ps.buy();
        assertNotNull(receipt);
        assertEquals(20, receipt.value());
    }

    @Test
    public void buyWithNoCoinsShouldReturnZeroReceipt() {
        Receipt receipt = ps.buy();
        assertNotNull(receipt);
        assertEquals(0, receipt.value());
    }

    @Test
    public void buyShouldResetForNextTransaction() throws IllegalCoinException {
        ps.addPayment(25);
        ps.buy();
        assertEquals(0, ps.readDisplay());
        ps.addPayment(5);
        assertEquals(2, ps.readDisplay());
    }

    @Test
    public void cancelShouldReturnOneCoin() throws IllegalCoinException {
        ps.addPayment(10);
        Map<Integer, Integer> coins = ps.cancel();
        assertEquals(1, coins.size());
        assertEquals(Integer.valueOf(1), coins.get(10));
    }

    @Test
    public void cancelShouldReturnMixtureOfCoins() throws IllegalCoinException {
        ps.addPayment(5);
        ps.addPayment(25);
        ps.addPayment(25);
        Map<Integer, Integer> coins = ps.cancel();
        assertEquals(Integer.valueOf(1), coins.get(5));
        assertEquals(Integer.valueOf(2), coins.get(25));
    }

    @Test
    public void cancelShouldOnlyContainInsertedCoinTypes() throws IllegalCoinException {
        ps.addPayment(25);
        Map<Integer, Integer> coins = ps.cancel();
        assertFalse(coins.containsKey(5));
        assertFalse(coins.containsKey(10));
    }

    @Test
    public void cancelWithNoCoinsShouldReturnEmptyMap() {
        Map<Integer, Integer> coins = ps.cancel();
        assertNotNull(coins);
        assertTrue(coins.isEmpty());
    }

    @Test
    public void cancelShouldResetDisplay() throws IllegalCoinException {
        ps.addPayment(25);
        ps.cancel();
        assertEquals(0, ps.readDisplay());
    }

    @Test
    public void cancelledMoneyShouldNotBeCollected() throws IllegalCoinException {
        ps.addPayment(25);
        ps.cancel();
        assertEquals(0, ps.empty());
    }

    @Test
    public void buyShouldClearCoinsSoCancelReturnsNothing() throws IllegalCoinException {
        ps.addPayment(25);
        ps.buy();
        assertTrue(ps.cancel().isEmpty());
    }

    @Test
    public void emptyShouldReturnTotalFromAllPurchases() throws IllegalCoinException {
        ps.addPayment(25);
        ps.buy();
        ps.addPayment(10);
        ps.addPayment(5);
        ps.buy();
        assertEquals(40, ps.empty());
    }

    @Test
    public void emptyShouldResetTotalToZero() throws IllegalCoinException {
        ps.addPayment(25);
        ps.buy();
        ps.empty();
        assertEquals(0, ps.empty());
    }

    @Test
    public void changingRateShouldRepriceCoinsAlreadyInserted() throws IllegalCoinException {
        ps.addPayment(25);
        ps.setRateStrategy(new Linear2RateStrategy());
        assertEquals(5, ps.readDisplay());
    }
}
