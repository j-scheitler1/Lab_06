//package ATM;
//
//import java.util.List;
package ATM;

import java.util.List;
import java.util.Scanner;


import java.sql.Date;
import java.text.SimpleDateFormat;

//Authors: Joshua Scheitler, Ethan Mayer

import java.util.Scanner;


public class ATM {

	private Checkings checkingsAccount;
	
	private Savings savingsAccount;
	
	private UtilityAccount utilityAccount;
	
	private User user;

	
	public static void main(String[] args) {
	
		Scanner scanner = new Scanner(System.in);
		String username = "none";
		String password = "none";
		String accountNum = "";
		User user = null;
		
		System.out.println("Hello, welcome to the ATM! Please input 1 to login with username or 2 to login with account number ");
		int scanLogin = scanner.nextInt();
		String scanIn;
		
		if (scanLogin == 1) {
			System.out.println("Please input your username: ");
			scanIn = scanner.next();
			username = scanIn;			
			user = new User(username, password);
		} 
		else if (scanLogin == 2) {
			System.out.println("Please input your account number: ");
			scanIn = scanner.next();
			accountNum = scanIn;			
			user = new User(null, accountNum, password);
		}
				

		System.out.println("Please input your password to log in: ");
		scanIn = scanner.next();
		password = scanIn;
		
		Checkings checkingsAccount = user.getCheckings();
		Savings savingsAccount = user.getSavings();
		UtilityAccount utilityAccount = user.getUtility();
		
		boolean exit = true;
		
		while(exit) {
			
			System.out.println("Which account would you like to access? Input 1, 2, or 3.");
	
			System.out.println("1: Checkings, 2: Savings, 3: Utility");
			
			System.out.println("To advance to the next day: 0");
			
			System.out.println("To exit ATM: 9");
			
			int choice = scanner.nextInt();
			
			
			if(choice ==  1) {
				//Checking
				for (int i = 0; i < 50; ++i) System.out.println();
				
				System.out.println("Welcome to your checking account " + username + "!");
				System.out.println("Options: Input 1, 2, 3, 4, 5 or 6");
				System.out.println("1: Deposit, 2: Withdraw, 3: Transfer, 4: Pay bill, 5: Check balance, 6: Back");
				
				choice = scanner.nextInt();
				
				if(choice ==  1) {
					//Deposit
					System.out.println("How much would you like to deposit?");
					
					double amount = scanner.nextDouble();
					
					if(checkingsAccount.deposit(amount) ==  false) {
						System.out.println("Failure to deposit. You have only $" + checkingsAccount.getDepositLimit() + " daily deposit limit remaining!");
					}
					else {
						System.out.println("Successfully deposited $" + amount);
						System.out.println("You have $" + checkingsAccount.getDepositLimit() + " daily deposit limit remaining!");
					}
				}
				if(choice ==  2) {
					//Withdraw
					System.out.println("How much would you like to withdraw?");
					
					double amount = scanner.nextDouble();
					
					if(checkingsAccount.withdraw(amount) ==  false) {
						System.out.println("Failure to withdraw.");
						
						if(amount > checkingsAccount.getBalance()) {
							System.out.println("Oops! You tried to overdraft your account.");
						}
						else {
							System.out.println("You have only $" + checkingsAccount.getWithdrawLimit() + " daily withdraw limit remaining!");
						}
					}
					else {
						System.out.println("Successfully withdrew $" + amount);
						System.out.println("You have $" + checkingsAccount.getWithdrawLimit() + " daily withdraw limit remaining!");
					}
				}
				if(choice ==  3) {
					//Transfer
					System.out.println("How much would you like to transfer?");
					
					double amount = scanner.nextDouble();
					
					if(checkingsAccount.transfer(amount, checkingsAccount, savingsAccount)) {
						System.out.println("Successfully transferred $" + amount + " from checking to savings");
					}
					else {
						System.out.println("Too much to transfer. The most you can transfer is $" + checkingsAccount.getBalance());
					}
				}
				if(choice ==  4) {
					// TODO - get bill pay to work
					Scanner bills = new Scanner(System.in);
					System.out.println("Please enter how much you would like to pay towards it: ");
					double amount = bills.nextDouble();
					
					if(checkingsAccount.withdraw(amount) ==  false) {
						System.out.println("Failure to Pay");
						
						if(amount > checkingsAccount.getBalance()) {
							System.out.println("Oops! You tried to overdraft your account.");
						}
						else {
							System.out.println("You have only $" + checkingsAccount.getWithdrawLimit() + " daily withdraw limit remaining!");
						}
					}
					else {
						System.out.println("Successfully withdrew $" + amount);
						System.out.println("You have $" + checkingsAccount.getWithdrawLimit() + " daily withdraw limit remaining!");
						Payment p = new Payment(amount, utilityAccount.getNextBillPayment(), utilityAccount.getNextBillDueDate());
						int accountNumber = utilityAccount.getAccountNumber();
						utilityAccount.savePayment(accountNumber, p);
						System.out.println("Utility Company Says 'Thanks for your money chump!'");
					}	
				}
				if(choice ==  5) {
					//Check balance
					System.out.println("Checking account balance: $" + checkingsAccount.getBalance());
				}
				if(choice ==  6) {
					//Back
					for (int i = 0; i < 50; ++i) System.out.println();
				}
				
			}
			else if(choice ==  2) {
				//Savings
				for (int i = 0; i < 50; ++i) System.out.println();
				
				System.out.println("Welcome to your savings account " + username + "!");
				System.out.println("Options: Input 1, 2, 3 or 4");
				System.out.println("1: Deposit, 2: Transfer, 3: Check balance, 4: Back");
				
				choice = scanner.nextInt();
				
				if(choice ==  1) {
					//Deposit
					System.out.println("How much would you like to deposit?");
					
					double amount = scanner.nextDouble();
					
					if(savingsAccount.deposit(amount) ==  false) {
						System.out.println("Failure to deposit. You have only $" + savingsAccount.getDepositLimit() + " daily deposit limit remaining!");
					}
					else {
						System.out.println("Successfully deposited $" + amount);
						System.out.println("You have $" + savingsAccount.getDepositLimit() + " daily deposit limit remaining!");
					}
				}
				if(choice ==  2) {
					//Transfer
					System.out.println("How much would you like to transfer?");
					
					double amount = scanner.nextDouble();
					
					if(savingsAccount.transfer(amount, savingsAccount, checkingsAccount)) {
						System.out.println("Successfully transferred $" + amount + " from savings to checking");
						System.out.println("You have $" + savingsAccount.getTransferTotal() + " daily transfer limit remaining!");
					}
					else {
						System.out.println("Too much to transfer. The most you can transfer is $" + savingsAccount.getTransferTotal());
						System.out.println("You have $" + savingsAccount.getBalance() + " in your account.");
					}
				}
				if(choice ==  3) {
					//Check balance
					System.out.println("Savings account balance: $" + savingsAccount.getBalance());
				}
				if(choice ==  4) {
					//Back
					for (int i = 0; i < 50; ++i) System.out.println();
				}
			}
			else if(choice ==  3) {
				//Utility
				for (int i = 0; i < 50; ++i) System.out.println();
				
				System.out.println("Welcome to Utility Company Incorporated! ");
				System.out.println("Options: Input 1, 2 or 3");
				System.out.println("1: View payment history, 2: View next bill, 3: Back");
				
				choice = scanner.nextInt();
				
				if(choice ==  1) {
					//Payment history
					List<Payment> paymentHistory;
					paymentHistory = utilityAccount.getPaymentHistory();
					
					System.out.print("Payment history: ");
					
					if (paymentHistory.size() == 0) {
						System.out.println("No payment history at this time, make a payment to see it!");
					} else {
						for (Payment p : paymentHistory) {
							System.out.println("");
							utilityAccount.displayPayment(p);
						}
					}
				}
				if(choice ==  2) {
					//Next bill payment
					System.out.println("Next bill payment: $" + utilityAccount.getNextBillPayment());
					System.out.println("Due date: " + utilityAccount.getNextBillDueDate());
				}
				if(choice ==  3) {
					//Back
					for (int i = 0; i < 50; ++i) System.out.println();
				}
				
				
			}
			else if(choice ==  0) {
				//Advance to next day, reset daily limits.
				for (int i = 0; i < 50; ++i) System.out.println();
				System.out.println("Advancing to the next day");
				checkingsAccount.resetDailyLimits(5000.0, 500.0);
				savingsAccount.resetDailyLimits(5000.0,  100.0);
			}
			else if(choice ==  9) {
				exit = false;

				for (int i = 0; i < 50; ++i) System.out.println();
				
				System.out.println("See you later!");
				


			}
		}
		
		
		
		
	}
}
