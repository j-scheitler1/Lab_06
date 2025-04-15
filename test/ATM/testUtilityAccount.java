package ATM;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class testUtilityAccount {

	private User user;
	private UtilityAccount utilityAccount;
	
	
	//TODO Josh
	
	@Before
	public void initialize() {
		this.user = new User("Josh", "S");
		this.utilityAccount = user.getUtility();
	}
	
	@Test
	public void testSaveUser() {
		
	}
	
	@Test
	public void testCorrectUserNameLogin() {
		boolean attemptLogin = utilityAccount.login("Josh", "0", "S");
		assertEquals(attemptLogin, true);
	}
	
	@Test
	public void testAccountNumberLogin() {
		String  num = Integer.toString(utilityAccount.getAccountNumber());
		boolean attemptLogin = utilityAccount.login("", num, "S");
		assertEquals(attemptLogin, true);
	}
	
	public void testWrongLogin() {
		boolean wrongNum = utilityAccount.login("", "99999", "S");
		boolean wrongUsername = utilityAccount.login("Josh", "", "WRONG PASSWORD");
		assertEquals(wrongNum, false);
		assertEquals(wrongUsername, false);
	}
	
	// PAYMENTS FILE STRUCTURE = AccountNumber,?Date|PaidAmount|DueAmount!, ...
//	@Test
//	public void testGetPaymentHistory() {
//		// TODO - Make this delete afterwards
//		List<Payment> payments = new ArrayList<Payment>();
//		payments = utilityAccount.getPaymentHistory();
//		
//		assertEquals(0, payments.size());
//		
//		Payment test = new Payment(0, 100, "July 3, 2025");
//		utilityAccount.savePayment(utilityAccount.getAccountNumber(), test);
//		System.out.println(utilityAccount.getAccountNumber());
//		payments = utilityAccount.getPaymentHistory();
//		
//		// FIX
//		assertEquals(1, payments.size());
//		
//	}
	
	@Test
	public void testSavePayment() {
		Payment test = new Payment(0, 100, "July 4, 2025");
		boolean saved = utilityAccount.savePayment(utilityAccount.getAccountNumber(), test);
		assertEquals(true, saved);
		
		// Assert Invalid Payment
		saved = utilityAccount.savePayment(500000000, test);
		assertEquals(false, saved);
	}
	
	@Test
	public void testPaymentFormat() {
		String expected = "!July 4th|50.0|100.0?";
		
		Payment test = new Payment(50, 100, "July 4th");
		utilityAccount.savePayment(utilityAccount.getAccountNumber(), test);
		List<Payment> payment = new ArrayList<Payment>();
		payment = utilityAccount.getPaymentHistory();
		
		
		//TODO fix
		//!July 4, 2025|0.0|100.0?
		//!July 4th|50.0|100.0?
		System.out.println(payment.get(0).toString());
		System.out.println(expected);
		assertEquals(payment.get(0).toString(), expected);
	}
	
	@Test
	public void testDisplayPayment () {
		// TODO - FIX OUTPUT
		String expected = "Due Date: July 4th\n" +
							"Paid Amount: 50.0\n" +
							"Due Amount: 100.0" +
							"\n";
		
		Payment test = new Payment(50, 100, "July 4th");
		utilityAccount.savePayment(utilityAccount.getAccountNumber(), test);
		List<Payment> payment = new ArrayList<Payment>();
		payment = utilityAccount.getPaymentHistory();
		
		
		String actual = utilityAccount.displayPayment(payment.get(0));
		
		System.out.println("First " + utilityAccount.displayPayment(payment.get(0)));
		System.out.println("Second " + actual);
		
		assertEquals(actual, expected);
	}
	
	@Test
	public void testGetNextBillPayment() {
		
	}
	
	@Test
	public void testGetAccountNumber() {
		
	}
}
