package ATM;


//Authors: Joshua Scheitler, Ethan Mayer



import java.util.ArrayList;
import java.util.List;

public class Savings extends Account{
	

	private List<Payment> paymentHistory;
	
	protected int transferTotal;
	
	public Savings() {
		super();
		transferTotal = 0;
	}

	public boolean transferFunds(int amount) {
		if ((transferTotal + amount) > 100) return false;
		transferTotal += amount;
		balance -= amount;
		return true;
	}
	
	

}
