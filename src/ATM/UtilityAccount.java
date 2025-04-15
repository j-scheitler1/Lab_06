package ATM;

// Authors: Joshua Scheitler, Ethan Mayer

import java.util.ArrayList;

import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.StringBuilder;
import java.sql.Date;
import java.text.ParseException;


public class UtilityAccount {
	protected String username;
	protected String password;
	protected int accountNumber;
	protected List<Payment> paymentHistory;
	protected Payment nextPayment;
	Random rand = new Random();
	
	public static UtilityAccount createOrLogin (String username, String accountNum, String password) {
		if (login(username, accountNum, password)) {
			System.out.println("Logging in!");
			UtilityAccount ua = new UtilityAccount(username, accountNum, password);
			setAccountNumber(username, accountNum, password, ua);
			return ua;
		}
		System.out.println("Creating Account!");
		return new UtilityAccount(username, password);
	}
	
	public UtilityAccount (String username, String accountNum, String password) {
		this.username = username;
		this.password = password;
		this.accountNumber = getAccountNumber();
		this.paymentHistory = getPaymentHistory();
	}
	
	public UtilityAccount (String username, String password) {
		this.username = username;
		this.password = password;
		accountNumber = rand.nextInt(1_000_000) + 1;
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
	
	public static boolean login(String username, String accountNumber, String password) {
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
					if ((username.equals(currUsername) || String.valueOf(accountNumber).equals(currAccountNumber)) && password.equals(currPassword)) {
						return true;
					}
				}
			}
			
		} catch(IOException e) {
			System.out.println("Could not find user: " + e.getMessage());
		}
		return false;
	}
	
	public List<Payment> getPaymentHistory() {
		
		int accountNumber = getAccountNumber();
		
	    List<Payment> payments = new ArrayList<>();
	    String actNum = String.valueOf(accountNumber);

	    try (BufferedReader reader = new BufferedReader(new FileReader("payment_history.txt"))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(",", 2);
	            if (parts[0].equals(actNum) && parts.length > 1) {
	                String[] paymentEntries = parts[1].split("\\?");
	                for (String entry : paymentEntries) {
	                    if (entry.isEmpty()) continue;
	                    String cleaned = entry.replaceFirst("!", "");
	                    String[] details = cleaned.split("\\|");
	                    if (details.length == 3) {
	                        try {
	                            String date = details[0];
	                            double paid = Double.parseDouble(details[1]);
	                            double due = Double.parseDouble(details[2]);
	                            payments.add(new Payment(paid, due, date));
	                        } catch (IllegalArgumentException e) {
	                            System.out.println("Skipping malformed payment entry: " + entry);
	                        }
	                    }
	                }
	                break;
	            }
	        }
	    } catch (IOException e) {
	        System.out.println("Error reading payment history: " + e.getMessage());
	    }

	    int size = payments.size();
	    return payments.subList(Math.max(0, size - 3), size);
	}
	
	// PAYMENTS FILE STRUCTURE = AccountNumber,?Date|PaidAmount|DueAmount!, ...
	public Boolean savePayment(int accountNumber, Payment payment) {
	    File inputFile = new File("payment_history.txt");
	    File tempFile = new File("temp_payment_history.txt");
	    String actNum = String.valueOf(accountNumber);

	    boolean found = false; // Track if the account number was found

	    try (
	        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
	        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
	    ) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(",", 2);
	            System.out.println("GIVEN: " + actNum);
	            System.out.println("COMPARING WITH: " + parts[0]);
	            if (parts[0].equals(actNum)) {
	                String existingPayments = parts.length > 1 ? parts[1] : "";
	                String updatedLine = actNum + "," + existingPayments + paymentFormat(payment);
	                writer.write(updatedLine);
	                found = true;
	            } else {
	                writer.write(line);
	            }
	            writer.newLine();
	        }
	    } catch (IOException e) {
	        System.out.println("Error updating payment history: " + e.getMessage());
	        return false;
	    }

	    if (!found) {
	        System.out.println("No matching account found. No payment saved.");
	        tempFile.delete(); // clean up the temp file
	        return false;
	    }

	    if (inputFile.delete()) {
	        if (!tempFile.renameTo(inputFile)) {
	            System.out.println("Error: Could not rename temp file.");
	            return false;
	        }
	    } else {
	        System.out.println("Error: Could not delete original payment history file.");
	        return false;
	    }

	    return true;
	}

	
	public String paymentFormat(Payment payment) {
		StringBuilder sb = new StringBuilder();
		sb.append("!").append(payment.getdueDate())
			.append("|").append(payment.getPaidAmount())
			.append("|").append(payment.getDueAmount())
			.append("?");
		return sb.toString();
	}
	
	
	public String displayPayment (Payment paymentHistory) {
		StringBuilder sb = new StringBuilder();
			sb.append("Due Date: ").append(paymentHistory.getdueDate()).append("\n");
			sb.append("Paid Amount: ").append(paymentHistory.getPaidAmount()).append("\n");
			sb.append("Due Amount: ").append(paymentHistory.getDueAmount()).append("\n");
			sb.append("\n");
		System.out.println(sb.toString());
		
		return sb.toString();
	}
	
	public Payment getNextPayment() {
		return nextPayment;
	}
	
	public double getNextBillPayment() {
		return 100.0;
	}
	public String getNextBillDueDate() {
		return "July 4th";
	}
	public int getAccountNumber() {
		return accountNumber;
	}
	
	public static void setAccountNumber(String username, String accountNumber, String password, UtilityAccount ua) {
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
					if ((username.equals(currUsername) || String.valueOf(accountNumber).equals(currAccountNumber)) && password.equals(currPassword)) {
						ua.accountNumber = Integer.parseInt(currAccountNumber);
					}
				}
			}
		} catch(IOException e) {
			System.out.println("Could not find user: " + e.getMessage());
		}
	}
}
