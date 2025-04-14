package ATM;

import java.util.List;
import java.util.Scanner;

public class AccountMenu {
	public static void handleCheckings(Scanner scanner, Checkings checkings, Savings savings, UtilityAccount utility) {
		System.out.println("Welcome to your checking account!");
		System.out.println("1: Deposit | 2: Withdraw | 3: Transfer | 4: Pay Bill | 5: Balance | 6: Back");

		switch (scanner.nextInt()) {
			case 1 -> deposit(scanner, checkings);
			case 2 -> withdraw(scanner, checkings);
			case 3 -> transfer(scanner, checkings, savings);
			case 4 -> payBill(scanner, checkings, utility);
			case 5 -> System.out.println("Balance: $" + checkings.getBalance());
		}
	}

	public static void handleSavings(Scanner scanner, Savings savings, Checkings checkings) {
		System.out.println("Welcome to your savings account!");
		System.out.println("1: Deposit | 2: Transfer | 3: Balance | 4: Back");

		switch (scanner.nextInt()) {
			case 1 -> deposit(scanner, savings);
			case 2 -> transfer(scanner, savings, checkings);
			case 3 -> System.out.println("Balance: $" + savings.getBalance());
		}
	}

	public static void handleUtility(Scanner scanner, UtilityAccount utility) {
		System.out.println("Welcome to Utility Co!");
		System.out.println("1: Payment History | 2: View Next Bill | 3: Back");

		switch (scanner.nextInt()) {
			case 1 -> {
			 List<Payment> payments = utility.getPaymentHistory();
			 if (payments.isEmpty()) System.out.println("No history yet.");
			 else payments.forEach(utility::displayPayment);
			}
			case 2 -> {
			 System.out.println("Next bill: $" + utility.getNextBillPayment());
			 System.out.println("Due: " + utility.getNextBillDueDate());
			}
		}
	}

	private static void deposit(Scanner scanner, Account account) {
		System.out.print("Deposit amount: ");
		double amount = scanner.nextDouble();
		if (account.deposit(amount)) {
			System.out.println("Deposited $" + amount);
		} else {
			System.out.println("Deposit failed. Limit: $" + account.getDepositLimit());
		}
	}

	private static void withdraw(Scanner scanner, Checkings account) {
		System.out.print("Withdraw amount: ");
		double amount = scanner.nextDouble();
		if (account.withdraw(amount)) {
			System.out.println("Withdrew $" + amount);
		} else {
			System.out.println("Withdraw failed. Limit: $" + account.getWithdrawLimit());
		}
	}

	private static void transfer(Scanner scanner, Account from, Account to) {
		System.out.print("Transfer amount: ");
		double amount = scanner.nextDouble();
		if (from.transfer(amount, from, to)) {
			System.out.println("Transferred $" + amount);
		} else {
			System.out.println("Transfer failed.");
		}
	}

	private static void payBill(Scanner scanner, Checkings checkings, UtilityAccount utility) {
		System.out.print("Amount to pay: ");
		double amount = scanner.nextDouble();
		if (!checkings.withdraw(amount)) {
			System.out.println("Bill payment failed. Check balance and limit.");
			return;
		}
		Payment payment = new Payment(amount, utility.getNextBillPayment(), utility.getNextBillDueDate());
		utility.savePayment(utility.getAccountNumber(), payment);
		System.out.println("Utility Company says: Thanks for your money chump!");
	}
}
