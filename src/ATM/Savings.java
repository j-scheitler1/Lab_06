package ATM;

public class Savings extends Account{
	
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
