package ATM;


//Authors: Joshua Scheitler, Ethan Mayer



import java.util.ArrayList;
import java.util.List;

public class Savings extends Account{
	

	private List<Payment> paymentHistory;
	
	
	protected int transferTotal;
	
	public Savings() {
		super();
		transferTotal = 100;
	}

	@Override
	public boolean transfer(double amount, Account account1, Account account2) {
		if(amount > balance || amount > transferTotal) {
			return false;
		}
		else {
			account1.balance -= amount;
			account2.balance += amount;
			transferTotal -= amount;
		}
		return true;
	}
	
	public double getTransferTotal() {
		
		return transferTotal;
	}

}
