package ATM;

//Authors: Joshua Scheitler, Ethan Mayer

public abstract class Account {
	
	protected int balance;
	private double depositLimit;
	private double withdrawLimit;
	
	
	public Account () {
		balance = 0;
		depositLimit = 5000;
		withdrawLimit = 500;
	}
	
	public boolean deposit(double amount) {
		
		if(amount > depositLimit) {
			//too big of a deposit, over daily limit!
			return false;
		}
		else {
			depositLimit =- amount;
			balance += amount;
		}
		
		return true;
	}
	
	public boolean withdraw(double amount) {
		
		if(amount > balance || amount > withdrawLimit) {
			//overdraft or over withdraw limit. Not successful.
			return false;
		}
		else {
			balance -= amount;
			withdrawLimit -= amount;
		}
		return true;
	}


	public boolean transfer(double amount, Account account1, Account account2) {
		
		if(amount > balance) {
			return false;
		}
		else {
			account1.balance -= amount;
			account2.balance += amount;
		}
		return true;
	}
	public double getDepositLimit() {
		return depositLimit;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public double getWithdrawLimit() {
		return withdrawLimit;
	}
}
