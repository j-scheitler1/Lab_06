package ATM;

// Authors: Joshua Scheitler, Ethan Mayer

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.StringBuilder;


public class UtilityAccount {
	protected String username;
	protected String password;
	protected int accountNumber;
	protected List<Payment> paymentHistory;
	Random rand = new Random();
	
	
	public UtilityAccount (String username, String password) {
		this.username = username;
		this.password = password;
		accountNumber = rand.nextInt();
		paymentHistory = new ArrayList<Payment>();
		saveUser();
	}
	
	public void saveUser() {
		// Save username, password, and accountNumber to file
	    try (PrintWriter out = new PrintWriter(new FileWriter("utility_users.txt", true))) {
	        out.println(username + "," + password + "," + accountNumber);
	    } catch (IOException e) {
	        System.out.println("Error saving user: " + e.getMessage());
	    }
	    
	    // Save Account number to oayment history so we can add to payment history in future
	    try (PrintWriter out = new PrintWriter(new FileWriter("payment_history.txt", true))) {
	        out.println(accountNumber + ",");
	    } catch (IOException e) {
	        System.out.println("Error saving user: " + e.getMessage());
	    }
	}
	
	public void login(String username, int accountNumber, String password) {
		try (BufferedReader reader = new BufferedReader(new FileReader("utility_users.txt"))) {
			String line;
			String currUsername;
			String currPassword;
			String currAccountNumber;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 3) {
					currUsername = parts[0];
					currPassword = parts[1];
					currAccountNumber = parts[2];
					if ((username == currUsername || String.valueOf(accountNumber) == currAccountNumber)&& password == currPassword) {
						this.username = username;
						this.password = password;
						this.paymentHistory = getPaymentHistory(Integer.parseInt(currAccountNumber));
					}
				}
			}
			
		} catch(IOException e) {
			System.out.println("Could not find user: " + e.getMessage());
		}
	}
	
	// TODO - Make this get the payment history from the Account Number
	public List<Payment> getPaymentHistory(int accountNumber) {
		try (BufferedReader reader = new BufferedReader(new FileReader("payment_history.txt"))) {
			String line;
			String actNum = String.valueOf(accountNumber);
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts[0].equals(actNum)) {
					// ACCOUNT NUMBER FOUND NEED TO FETCH HISTORY
					
				}
			}
			
		} catch (IOException e) {
			
		}
		
		return new ArrayList<>();
	}
	
	// PAYMENTS FILE STRUCTURE = AccountNumber,?Date|PaidAmount|DueAmount!, ...
	
	public void savePayment(int accountNumber, Payment payment) {
		try (BufferedReader reader = new BufferedReader(new FileReader(""))) {
			String line;
			String actNum = String.valueOf(accountNumber);
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts[0].equals(actNum)) {
					// FOUND ACCOUNT, NOW GET PAYMENT AND SAVE
					String paymentWriting = paymentFormat(payment);
				}	
			}
		} catch (IOException e) {
			System.out.println("Could not find the user: " + e.getMessage());
		}
	}
	
	public String paymentFormat(Payment payment) {
		StringBuilder sb = new StringBuilder();
		sb.append("!").append(payment.getdueDate()).append("|").append(payment.getPaidAmount()).append("|").append(payment.getDueAmount()).append("!");
		return sb.toString();
	}
	
	
	public String displayPayment (Payment paymentHistory) {
		StringBuilder sb = new StringBuilder();
			sb.append("Due Date: ").append(paymentHistory.getdueDate()).append("\n");
			sb.append("Paid Amount: ").append(paymentHistory.getPaidAmount()).append("\n");
			sb.append("Due Amount: ").append(paymentHistory.getDueAmount()).append("\n");
			sb.append("\n");
		return sb.toString();
	}
	
	
	

}
