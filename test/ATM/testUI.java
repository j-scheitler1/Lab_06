package ATM;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testUI {

	private final InputStream systemIn = System.in;
	private final PrintStream systemOut = System.out;

	private ByteArrayInputStream testIn;
	private ByteArrayOutputStream testOut;

	private String generateRandomPin() {
		int pin = (int)(Math.random() * 9000) + 1000; // Generates a number from 1000 to 9999
		return String.valueOf(pin);
	}
	
	@Before
	public void setUpOutput() {
		testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
	}

	@After
	public void restoreSystemInputOutput() {
		System.setIn(systemIn);
		System.setOut(systemOut);
	}

	private void provideInput(String data) {
		testIn = new ByteArrayInputStream(data.getBytes());
		System.setIn(testIn);
	}

	private String getOutput() {
		return testOut.toString();
	}

	@Test
	public void testCreateNewUserAndDeposit() {
		String randomPin = generateRandomPin();

		String input = String.join("\n",
			randomPin,      // Unrecognized PIN (new user)
			"testuser",     // Username
			randomPin,      // New PIN
			"1",            // Checkings
			"1",            // Deposit
			"300",          // Deposit amount
			"6",            // Back
			"9"             // Exit
		);
		provideInput(input);
		ATM.main(new String[]{});

		String output = getOutput();
		assertTrue(output.contains("PIN registered successfully!"));
		assertTrue(output.contains("Successfully deposited $300.0"));
	}


	@Test
	public void testWithdrawOverLimit() {
		String input = String.join("\n",
			"9999",      // Login with existing user
			"1",         // Checkings
			"2",         // Withdraw
			"10000",     // Too much
			"6",         // Back
			"9"          // Exit
		);
		provideInput(input);
		ATM.main(new String[]{});

		String output = getOutput();
		assertTrue(output.contains("Failure to withdraw"));
		assertTrue(output.contains("Oops! You tried to overdraft your account"));
	}
}
