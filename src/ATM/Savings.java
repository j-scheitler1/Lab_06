package ATM;


//Authors: Joshua Scheitler, Ethan Mayer



import java.util.ArrayList;
import java.util.List;

public class Savings extends Account{
	
	protected double transferLimit;
	protected double depositLimit;
	protected double withdrawLimit;
	
	private List<Payment> paymentHistory;
	
	
	protected int transferTotal;
	
	public Savings(double depositLimit, double withdrawLimit) {
		
		
		super(depositLimit, withdrawLimit);
		this.transferLimit = 100;

	}
	
	@Override
	public boolean transfer(double amount, Account account1, Account account2) {
		if(amount > balance || amount > transferLimit) {
			return false;
		}
		else {
			account1.balance -= amount;
			account2.balance += amount;
			transferLimit -= amount;
		}
		return true;
	}
	
	public double getTransferTotal() {
		
		return transferLimit;
	}
	
	@Override
	public void resetDailyLimits(double depositLimit, double transferLimit) {
		this.depositLimit = depositLimit;
		this.transferLimit = transferLimit;
	}

}
