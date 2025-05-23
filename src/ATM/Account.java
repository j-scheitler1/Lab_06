package ATM;

//Authors: Joshua Scheitler, Ethan Mayer

public abstract class Account {
	
	protected double balance;
	protected double depositLimit;
	protected double withdrawLimit;
	
	
	public Account (double depositLimit, double withdrawLimit) {
		balance = 0;
		this.depositLimit = depositLimit;
		this.withdrawLimit = withdrawLimit;
	}
	
	public boolean deposit(double amount) {
		
		if(amount > depositLimit || amount < 0) {
			//too big of a deposit, over daily limit! or negative
			return false;
		}
		else {
			//deposit into account, adjust daily deposit limit
			depositLimit -= amount;
			balance += amount;
		}
		
		return true;
	}
	
	public boolean withdraw(double amount) {
		
		if(amount > balance || amount > withdrawLimit || amount < 0) {
			//overdraft or over withdraw limit or negative. Not successful.
			return false;
		}
		else {
			//withdraw from account, adjust daily withdraw limit
			balance -= amount;
			withdrawLimit -= amount;
		}
		return true;
	}


	public boolean transfer(double amount, Account account1, Account account2) {
		
		if(amount > balance || amount < 0) {
			//no overdrafts allowed. no negative amounts
			return false;
		}
		else {
			//transfer amount
			account1.balance -= amount;
			account2.balance += amount;
		}
		return true;
	}
	public void resetDailyLimits(double depositLimit, double withdrawLimit) {
		this.depositLimit = depositLimit;
		this.withdrawLimit = withdrawLimit;
	}
	public double getDepositLimit() {
		return depositLimit;
	}
	
	public boolean setBalance(double balance) {
		
		if(balance < 0) {
			//cant have negative balance
			return false;
		}
		
		this.balance = balance;
		return true;
	}
	public double getBalance() {
		return balance;
	}
	
	public double getWithdrawLimit() {
		return withdrawLimit;
	}
}
