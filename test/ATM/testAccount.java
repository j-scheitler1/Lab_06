package ATM;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class testAccount {
	
	public User user;
	public Checkings checkingsAccount;
	public Savings savingsAccount;
	//public UtilityAccount utilityAccount;
	
	private static final double DELTA = 1e-15;
	
	@Before
	public void initialize() {
		String username = "none";
		String password = "none";
		
		this.user = new User(username, password);
		this.checkingsAccount = user.getCheckings();
		this.savingsAccount = user.getSavings();
		//this.utilityAccount = user.getUtility();
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
 		
 		assertEquals(expectedChecking, false);
 		assertEquals(expectedSavings, false);
 	}
 
 	@Test
 	public void testAccountWithdrawInvalid() {
 		
 		checkingsAccount.deposit(500.0);
 		
 		boolean expectedChecking = checkingsAccount.withdraw(600.0);
 		
 		assertEquals(500.0, checkingsAccount.getBalance(), DELTA);
 
 		assertEquals(expectedChecking, false);
 	}
 	
 	@Test
 	public void testAccountTransferInvalid() {
 		
 		checkingsAccount.deposit(500.0);
 		savingsAccount.deposit(500.0);
 		
 		//test overdraft transfer to savings
 		boolean expectedChecking = checkingsAccount.transfer(600.0, checkingsAccount, savingsAccount);
 		
 		assertEquals(500.0, checkingsAccount.getBalance(), DELTA);
 		assertEquals(500.0, savingsAccount.getBalance(), DELTA);
 		
 		//test overdraft transfer to checkings
 		boolean expectedSavings = savingsAccount.transfer(600.0, savingsAccount, checkingsAccount);
 		
 		assertEquals(500.0, checkingsAccount.getBalance(), DELTA);
 		assertEquals(500.0, savingsAccount.getBalance(), DELTA);
 		
 		//both transfers were invalid
 		assertEquals(expectedChecking, false);
 		assertEquals(expectedSavings, false);
 		
 		//test over transfer limit
 		expectedSavings = savingsAccount.transfer(120.0, savingsAccount, checkingsAccount);
 		assertEquals(expectedSavings, false);
 		
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
		
		System.out.println(checkingsAccount.getWithdrawLimit());
		System.out.println(checkingsAccount.getDepositLimit());
		System.out.println(savingsAccount.getDepositLimit());
		System.out.println(savingsAccount.getTransferTotal());
		
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
}
