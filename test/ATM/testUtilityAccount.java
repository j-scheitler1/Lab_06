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
	public void testLogin() {
		
	}
	
	@Test
	public void testGetPaymentHistory() {
		
	}
	
	// PAYMENTS FILE STRUCTURE = AccountNumber,?Date|PaidAmount|DueAmount!, ...
	@Test
	public void testSavePayment() {
	    
	}
	
	@Test
	public void testPaymentFormat() {

	}
	
	@Test
	public void testDisplayPayment () {

	}
	
	@Test
	public void testGetNextBillPayment() {

	}
	
	@Test
	public void testGetNextBillDueDate() {
		
	}
	
	@Test
	public void testGetAccountNumber() {
		
	}
}
