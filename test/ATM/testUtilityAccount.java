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
import java.util.Random;

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
	@Test
	public void testGetPaymentHistory() {
		String userName = generateRandomString(8); 
		String passWord = generateRandomString(12); 
		User newUser = new User(userName, passWord);
		UtilityAccount newUA = newUser.utilityAccount;
		List<Payment> payments = new ArrayList<Payment>();
		payments = newUA.getPaymentHistory();
		
		assertEquals(0, payments.size());
		
		Payment test = new Payment(0, 100, "July 3, 2025");
		newUA.savePayment(newUA.getAccountNumber(), 50);
		System.out.println(newUA.getAccountNumber());
		payments = newUA.getPaymentHistory();
		
		assertEquals(1, payments.size());
		
	}
	
	@Test
	public void testSavePayment() {
		boolean saved = utilityAccount.savePayment(utilityAccount.getAccountNumber(), 50.0);
		assertEquals(true, saved);
		
		// Assert Invalid Payment
		saved = utilityAccount.savePayment(500000000, 50.0);
		assertEquals(false, saved);
	}
	
	@Test
	public void testPaymentFormat() {
		String expected = "!July 4th|50.0|50.0?";
		utilityAccount.nextPayment = new Payment(0, 100, "July 4th");

		utilityAccount.savePayment(utilityAccount.getAccountNumber(), 50);
		List<Payment> payment = new ArrayList<Payment>();
		payment = utilityAccount.getPaymentHistory();
		
		assertEquals(payment.get(payment.size()-1).toString(), expected);
	}
	
	@Test
	public void testDisplayPayment () {
		// TODO - FIX OUTPUT
		String expected = "Due Date: July 4th\n" +
							"Paid Amount: 50.0\n" +
							"Due Amount: 50.0" +
							"\n";
		
		utilityAccount.nextPayment = new Payment(0, 100, "July 4th");
		utilityAccount.savePayment(utilityAccount.getAccountNumber(), 50);
		List<Payment> payment = new ArrayList<Payment>();
		payment = utilityAccount.getPaymentHistory();
		
		
		String actual = utilityAccount.displayPayment(payment.get(0));
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetNextBillPayment() {
		String expected = "Due Date: July 4th\n" +
				"Paid Amount: 50.0\n" +
				"Due Amount: 50.0" +
				"\n";
		utilityAccount.nextPayment = new Payment(0, 100, "July 4th");
		utilityAccount.savePayment(utilityAccount.getAccountNumber(), 500);
		Payment p = utilityAccount.getNextPayment();
		
		String actual = utilityAccount.displayPayment(p);
		
		assertNotEquals(expected, actual);
	}
	
	@Test
	public void testGetAccountNumber() {
		String expected = "367602";
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
