package ATM;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class testAccount {

	public User user;
	public Checkings checkingsAccount;
	public Savings savingsAccount;

	private static final double DELTA = 1e-15;

	@Before
	public void initialize() {
		// This username is just for testing purposes
		String username = "testUser";

		this.user = new User(username);
		this.checkingsAccount = user.getCheckings();
		this.savingsAccount = user.getSavings();

		// Reset account state before every test
		checkingsAccount.setBalance(0.0);
		checkingsAccount.resetDailyLimits(5000.0, 500.0);
		savingsAccount.setBalance(0.0);
		savingsAccount.resetDailyLimits(5000.0, 100.0);
	}

	@Test
	public void testAccountConstructor() {
		assertEquals(0.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(5000.0, checkingsAccount.getDepositLimit(), DELTA);
		assertEquals(500.0, checkingsAccount.getWithdrawLimit(), DELTA);

		assertEquals(0.0, savingsAccount.getBalance(), DELTA);
		assertEquals(5000.0, savingsAccount.getDepositLimit(), DELTA);
		assertEquals(100.0, savingsAccount.getTransferTotal(), DELTA);
	}

	@Test
	public void testAccountDeposit() {
		checkingsAccount.deposit(500.0);
		savingsAccount.deposit(500.0);

		assertEquals(500.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(500.0, savingsAccount.getBalance(), DELTA);
	}

	@Test
	public void testAccountWithdraw() {
		checkingsAccount.deposit(500.0);
		checkingsAccount.withdraw(100.0);

		assertEquals(400.0, checkingsAccount.getBalance(), DELTA);
	}

	@Test
	public void testAccountTransfer() {
		checkingsAccount.deposit(500.0);
		savingsAccount.deposit(500.0);

		checkingsAccount.transfer(100.0, checkingsAccount, savingsAccount);

		assertEquals(400.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(600.0, savingsAccount.getBalance(), DELTA);

		savingsAccount.transfer(50.0, savingsAccount, checkingsAccount);

		assertEquals(450.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(550.0, savingsAccount.getBalance(), DELTA);
	}

	@Test
	public void testAccountDepositInvalid() {
		boolean expectedChecking = checkingsAccount.deposit(50000.0);
		boolean expectedSavings = savingsAccount.deposit(50000.0);

		assertEquals(0.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(0.0, savingsAccount.getBalance(), DELTA);

		assertEquals(false, expectedChecking);
		assertEquals(false, expectedSavings);
	}

	@Test
	public void testAccountWithdrawInvalid() {
		checkingsAccount.deposit(500.0);
		boolean expectedChecking = checkingsAccount.withdraw(600.0);

		assertEquals(500.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(false, expectedChecking);
	}

	@Test
	public void testAccountTransferInvalid() {
		checkingsAccount.deposit(500.0);
		savingsAccount.deposit(500.0);

		boolean expectedChecking = checkingsAccount.transfer(600.0, checkingsAccount, savingsAccount);
		assertEquals(500.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(500.0, savingsAccount.getBalance(), DELTA);

		boolean expectedSavings = savingsAccount.transfer(600.0, savingsAccount, checkingsAccount);
		assertEquals(500.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(500.0, savingsAccount.getBalance(), DELTA);

		assertEquals(false, expectedChecking);
		assertEquals(false, expectedSavings);

		expectedSavings = savingsAccount.transfer(120.0, savingsAccount, checkingsAccount);
		assertEquals(false, expectedSavings);
	}

	@Test
	public void testAccountDepositNegative() {
		assertEquals(false, checkingsAccount.deposit(-500.0));
		assertEquals(false, savingsAccount.deposit(-500.0));
	}

	@Test
	public void testAccountWithdrawNegative() {
		assertEquals(false, checkingsAccount.withdraw(-500.0));
		assertEquals(false, savingsAccount.withdraw(-500.0));
	}

	@Test
	public void testAccountTransferNegative() {
		assertEquals(false, checkingsAccount.transfer(-500.0, checkingsAccount, savingsAccount));
		assertEquals(false, savingsAccount.transfer(-500.0, savingsAccount, checkingsAccount));
	}

	@Test
	public void testAccountGetTransferLimit() {
		savingsAccount.deposit(500.0);
		savingsAccount.transfer(50.0, savingsAccount, checkingsAccount);

		assertEquals(50.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(450.0, savingsAccount.getBalance(), DELTA);
		assertEquals(50.0, savingsAccount.getTransferTotal(), DELTA);
	}

	@Test
	public void testAccountResetDailyLimits() {
		checkingsAccount.deposit(500.0);
		savingsAccount.deposit(500.0);

		assertEquals(4500.0, checkingsAccount.getDepositLimit(), DELTA);
		assertEquals(4500.0, savingsAccount.getDepositLimit(), DELTA);

		checkingsAccount.withdraw(100.0);
		assertEquals(400.0, checkingsAccount.getWithdrawLimit(), DELTA);

		savingsAccount.transfer(100.0, savingsAccount, checkingsAccount);
		assertEquals(0.0, savingsAccount.getTransferTotal(), DELTA);

		checkingsAccount.resetDailyLimits(5000.0, 500.0);
		savingsAccount.resetDailyLimits(5000.0, 100.0);

		assertEquals(500.0, checkingsAccount.getWithdrawLimit(), DELTA);
		assertEquals(5000.0, checkingsAccount.getDepositLimit(), DELTA);
		assertEquals(5000.0, savingsAccount.getDepositLimit(), DELTA);
	}

	@Test
	public void testAccountGetWithdrawLimit() {
		checkingsAccount.deposit(500.0);
		assertEquals(500.0, checkingsAccount.getWithdrawLimit(), DELTA);

		checkingsAccount.withdraw(100.0);
		assertEquals(400.0, checkingsAccount.getWithdrawLimit(), DELTA);

		assertEquals(0.0, savingsAccount.getWithdrawLimit(), DELTA);
	}

	@Test
	public void testAccountGetDepositLimit() {
		assertEquals(5000.0, checkingsAccount.getDepositLimit(), DELTA);
		assertEquals(5000.0, savingsAccount.getDepositLimit(), DELTA);

		checkingsAccount.deposit(500.0);
		savingsAccount.deposit(500.0);

		assertEquals(4500.0, checkingsAccount.getDepositLimit(), DELTA);
		assertEquals(4500.0, savingsAccount.getDepositLimit(), DELTA);
	}

	@Test
	public void testAccountGetBalance() {
		assertEquals(0.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(0.0, savingsAccount.getBalance(), DELTA);

		checkingsAccount.deposit(500.0);
		savingsAccount.deposit(500.0);

		assertEquals(500.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(500.0, savingsAccount.getBalance(), DELTA);
	}

	@Test
	public void testAccountSetBalance() {
		checkingsAccount.setBalance(1000.0);
		savingsAccount.setBalance(1000.0);

		assertEquals(1000.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(1000.0, savingsAccount.getBalance(), DELTA);

		checkingsAccount.setBalance(10000.0);
		savingsAccount.setBalance(10000.0);

		assertEquals(10000.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(10000.0, savingsAccount.getBalance(), DELTA);
	}

	@Test
	public void testAccountSetBalanceInvalid() {
		boolean expectedChecking = checkingsAccount.setBalance(-1000.0);
		boolean expectedSavings = savingsAccount.setBalance(-1000.0);

		assertEquals(0.0, checkingsAccount.getBalance(), DELTA);
		assertEquals(0.0, savingsAccount.getBalance(), DELTA);
		assertEquals(false, expectedChecking);
		assertEquals(false, expectedSavings);
	}
}
