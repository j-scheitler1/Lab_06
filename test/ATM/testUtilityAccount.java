package ATM;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class testUtilityAccount {

	private UtilityAccount utilityAccount;
	private String username;
	private String password;

	@Before
	public void initialize() {
		username = generateRandomString(8);
		password = generateRandomString(10);
		utilityAccount = UtilityAccount.createOrLogin(username, "0", password);
	}

	@Test
	public void testCorrectUserNameLogin() {
		boolean login = UtilityAccount.login(username, "0", password); // pass 0 for accountNumber when using username
		assertTrue(login);
	}

	@Test
	public void testAccountNumberLogin() {
		String accountNum = Integer.toString(utilityAccount.getAccountNumber());
		boolean login = UtilityAccount.login("", accountNum, password); // pass null for username when using account number
		assertTrue(login);
	}

	@Test
	public void testWrongLogin() {
		boolean wrongNumber = UtilityAccount.login("", "999999", password); // non-existent account number
		boolean wrongPassword = UtilityAccount.login(username, "0", "incorrect");
		assertFalse(wrongNumber);
		assertFalse(wrongPassword);
	}

	@Test
	public void testGetPaymentHistory() {
		List<Payment> payments = utilityAccount.getPaymentHistory();
		assertEquals(0, payments.size());

		utilityAccount.nextPayment = new Payment(0, 100, "July 3, 2025");
		utilityAccount.savePayment(utilityAccount.getAccountNumber(), 50);
		payments = utilityAccount.getPaymentHistory();

		assertEquals(1, payments.size());
	}

	@Test
	public void testSavePayment() {
		boolean success = utilityAccount.savePayment(utilityAccount.getAccountNumber(), 50.0);
		assertTrue(success);

		boolean fail = utilityAccount.savePayment(123456789, 50.0); // invalid account
		assertFalse(fail);
	}

	@Test
	public void testPaymentFormat() {
		String expected = "!July 4th|50.0|50.0?";
		utilityAccount.nextPayment = new Payment(0, 100, "July 4th");

		utilityAccount.savePayment(utilityAccount.getAccountNumber(), 50);
		List<Payment> history = utilityAccount.getPaymentHistory();

		String actual = history.get(history.size() - 1).toString();
		assertEquals(expected, actual);
	}

	@Test
	public void testDisplayPayment() {
		String expected = "Due Date: July 4th\n" +
						  "Paid Amount: $50.00\n" +
						  "Due Amount: $50.00\n";

		utilityAccount.nextPayment = new Payment(0, 100, "July 4th");
		utilityAccount.savePayment(utilityAccount.getAccountNumber(), 50);

		List<Payment> payments = utilityAccount.getPaymentHistory();
		String actual = utilityAccount.displayPayment(payments.get(0));

		assertEquals(expected, actual);
	}

	@Test
	public void testGetNextBillPayment() {
		utilityAccount.nextPayment = new Payment(0, 100, "July 4th");
		utilityAccount.savePayment(utilityAccount.getAccountNumber(), 50);

		Payment p = utilityAccount.getNextPayment();
		String actual = utilityAccount.displayPayment(p);

		String incorrect = "Due Date: July 4th\n" +
						   "Paid Amount: 50.0\n" +
						   "Due Amount: 50.0\n";

		assertNotEquals(incorrect, actual);
	}

	@Test
	public void testGetAccountNumber() {
		String expected = Integer.toString(utilityAccount.getAccountNumber());
		String actual = Integer.toString(utilityAccount.getAccountNumber());
		assertEquals(expected, actual);
	}

	public static String generateRandomString(int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();

		for (int i = 0; i < length; i++) {
			sb.append(characters.charAt(rand.nextInt(characters.length())));
		}
		return sb.toString();
	}
}
