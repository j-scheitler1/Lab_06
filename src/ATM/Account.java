package ATM;

//Authors: Joshua Scheitler, Ethan Mayer

public abstract class Account {
	
	protected int balance;
	private int depositTotal;
	
	public Account () {
		balance = 0;
		depositTotal = 0;
	}
	
	public void deposit (int amount) {
		if ((depositTotal + amount) > 5000) return;
		depositTotal += amount;
		balance += amount;
	}
	
	public boolean isOverDraft(int amount) {
		if ((balance - amount) < 0) return true;
		return false;
	}
}
