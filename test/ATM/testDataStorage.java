package ATM;

import static org.junit.Assert.*;

import java.io.*;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class testDataStorage {

    private String username;
    private String password;
    private User user;
    private UtilityAccount utilityAccount;

    @Before
    public void setup() {
        username = generateRandomString(8);
        password = generateRandomString(10);
        user = new User(username);
        utilityAccount = UtilityAccount.createOrLogin(username, "0", password);
    }

    // ---------- USER STORAGE TESTS ----------

    @Test
    public void testSaveAndLoadUserAccounts() {
        // Save account with known balances
        user.getCheckings().setBalance(123.45);
        user.getSavings().setBalance(678.90);
        boolean saved = user.saveAccounts(username);
        assertTrue(saved);

        // Reload to verify data persisted
        User newUser = new User(username);
        assertEquals(123.45, newUser.getCheckings().getBalance(), 0.001);
        assertEquals(678.90, newUser.getSavings().getBalance(), 0.001);
    }

    @Test
    public void testPinMappingSaveAndLookup() {
        String pin = String.valueOf(1000 + new Random().nextInt(9000));

        boolean saved = User.savePinMapping(pin, username);
        assertTrue(saved);

        String lookup = User.getUsernameFromPin(pin);
        assertEquals(username, lookup);
    }

    @Test
    public void testSaveAndRetrievePayment() {
        utilityAccount.nextPayment = new Payment(0, 100.0, "June 30 2025");
        boolean success = utilityAccount.savePayment(utilityAccount.getAccountNumber(), 50.0);
        assertTrue(success);

        List<Payment> history = utilityAccount.getPaymentHistory();
        assertFalse(history.isEmpty());
        Payment last = history.get(history.size() - 1);

        assertEquals(50.0, last.getPaidAmount(), 0.001);
        assertEquals(50.0, last.getDueAmount(), 0.001);
        assertEquals("June 30 2025", last.getDueDate());
    }

    @Test
    public void testUpdatePartialAndFullPayment() {
        utilityAccount.nextPayment = new Payment(0, 200.0, "May 15 2026");
        utilityAccount.savePayment(utilityAccount.getAccountNumber(), 150.0);

        // Still has balance
        Payment partial = utilityAccount.getNextPayment();
        assertEquals(150.0, partial.getPaidAmount(), 0.001);
        assertEquals(50.0, partial.getDueAmount(), 0.001);

        // Full payment should reset
        utilityAccount.savePayment(utilityAccount.getAccountNumber(), 50.0);
        Payment newBill = utilityAccount.getNextPayment();
        assertNotEquals("May 15 2026", newBill.getDueDate()); // due date changed
        assertEquals(0.0, newBill.getPaidAmount(), 0.001); // reset paid
    }

    @Test
    public void testMalformedPaymentHistoryIgnored() {
        // Manually inject bad data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("payment_history.txt", true))) {
            writer.write(utilityAccount.getAccountNumber() + ",!BADENTRY|oops|nope?");
            writer.newLine();
        } catch (IOException e) {
            fail("Couldn't write malformed payment for test.");
        }

        List<Payment> history = utilityAccount.getPaymentHistory();
        // Should not crash and should still parse previous valid entries
        assertNotNull(history);
    }

    // ---------- UTILITY HELPERS ----------

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(rand.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
