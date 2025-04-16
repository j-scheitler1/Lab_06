package ATM;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testUI {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @Before
    public void setupStreams() {
        System.setOut(new PrintStream(output));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private void runWithInput(String simulatedInput) {
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);
        ATM.main(null);
    }

    @Test
    public void testLoginAndNavigate() {
        String input = String.join(System.lineSeparator(),
            "9999",            // new PIN (triggers registration)
            "testuser",        // username
            "9999",            // login again with new PIN
            "9"                // exit ATM
        );

        runWithInput(input);
        String out = output.toString();
        assertTrue(out.contains("PIN not recognized") || out.contains("Welcome to the ATM"));
        assertTrue(out.contains("See you later!"));
    }

    @Test
    public void testCheckingsDeposit() {
        String input = String.join(System.lineSeparator(),
            "1234",             // Assume existing PIN mapped to test user
            "1",                // Go to checking
            "1",                // Deposit
            "500",              // Amount
            "6",                // Back
            "9"                 // Exit
        );

        runWithInput(input);
        String out = output.toString();
        assertTrue(out.contains("Successfully deposited $500.0"));
        assertTrue(out.contains("Remaining deposit limit"));
    }

    @Test
    public void testUtilityHistory() {
        String input = String.join(System.lineSeparator(),
            "1234",              // Assume existing PIN
            "3",                 // Utility account
            "testuser",          // Username for utility login
            "",                  // No account number
            "password123",       // Password
            "1",                 // View payment history
            "3",                 // Back
            "9"                  // Exit
        );

        runWithInput(input);
        String out = output.toString();
        assertTrue(out.contains("Welcome to Utility Company"));
        assertTrue(out.contains("Payment history"));
    }

    @Test
    public void testWithdrawOverLimit() {
        String input = String.join(System.lineSeparator(),
            "1234",              // PIN
            "1",                 // Checkings
            "1",                 // Deposit
            "500",               // Deposit $500
            "2",                 // Withdraw
            "600",               // Over withdraw limit
            "6",                 // Back
            "9"                  // Exit
        );

        runWithInput(input);
        String out = output.toString();
        assertTrue(out.contains("Failure to withdraw"));
        assertTrue(out.contains("daily withdraw limit remaining"));
    }

    @Test
    public void testNegativeDeposit() {
        String input = String.join(System.lineSeparator(),
            "1234",              // PIN
            "1",                 // Checkings
            "1",                 // Deposit
            "-100",              // Negative amount
            "6",                 // Back
            "9"                  // Exit
        );

        runWithInput(input);
        String out = output.toString();
        assertTrue(out.contains("Failure to deposit"));
    }

    @Test
    public void testSavingsTransferOverLimit() {
        String input = String.join(System.lineSeparator(),
            "1234",              // PIN
            "2",                 // Savings
            "1",                 // Deposit
            "500",               // Deposit
            "2",                 // Transfer
            "150",               // Over transfer limit (100)
            "4",                 // Back
            "9"                  // Exit
        );

        runWithInput(input);
        String out = output.toString();
        assertTrue(out.contains("Transfer failed"));
    }
}
