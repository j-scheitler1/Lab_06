package ATM;

import java.util.List;

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
		
		System.out.println("Hello, welcome to the ATM! Please input your username: ");
		
		String scanIn = scanner.next();
				
		username = scanIn;

		System.out.println("Please input your password to log in: ");
		
		scanIn = scanner.next();
		
		password = scanIn;
		
		User user = new User(username, password);
		Checkings checkingsAccount = user.getCheckings();
		Savings savingsAccount = user.getSavings();
		UtilityAccount utilityAccount = user.getUtility();
		
		while(true) {
			
			System.out.println("Which account would you like to access? Input 1, 2, or 3.");
	
			System.out.println("1: Checkings, 2: Savings, 3: Utility");
			
			int choice = scanner.nextInt();
			
			
			if(choice ==  1) {
				//Checkings
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
					//Pay bill
				}
				if(choice ==  5) {
					//Check balance
					System.out.println("Checking account balance: $" + checkingsAccount.getBalance());
				}
				if(choice ==  6) {
					//Back
				}
				
			}
			else if(choice ==  2) {
				//Savings
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
					}
					else {
						System.out.println("Too much to transfer. The most you can transfer is $" + savingsAccount.getBalance());
					}
				}
				if(choice ==  3) {
					//Check balance
					System.out.println("Savings account balance: $" + savingsAccount.getBalance());
				}
				if(choice ==  4) {
					//Back
				}
			}
			else if(choice ==  3) {
				//Utility
				System.out.println("Welcome to your utility account " + username + "!");
				System.out.println("Options: Input 1, 2 or 3");
				System.out.println("1: View payment history, 2: View next bill, 3: Back");
				
				choice = scanner.nextInt();
				
				if(choice ==  1) {
					//Payment history
					List<Payment> paymentHistory;
					
					paymentHistory = utilityAccount.getPaymentHistory(111111);
					
					System.out.println("Payment history: ");
					
					for(int i=0; i<paymentHistory.size(); i++) {
						System.out.println(paymentHistory.get(i));
					}
				}
				if(choice ==  2) {
					//Next bill payment
					System.out.println("Next bill payment: $" + utilityAccount.getNextBillPayment());
					System.out.println("Due date: " + utilityAccount.getNextBillDueDate());
				}
				if(choice ==  3) {
					//Back
				}
				
				
			}
		}
		
		
		
		
	}
}
