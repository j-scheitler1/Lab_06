package ATM;

public class Checkings extends Account{
	
	protected int withdrawlTotal;
	protected int transferTotal;
	
	public Checkings () {
		super();
		withdrawlTotal = 0;
		transferTotal = 0;
	}
	
	public void withdrawlFunds(int amount) {
		if (canWithdrawl(amount) && !isOverDraft(amount)) return; 
		withdrawlTotal += amount;
		balance -= amount;
	}
	
	public boolean canWithdrawl(int amount) {
		if (withdrawlTotal + amount > 500 ) return false;
		return true;
	}

	public boolean transferFunds(int amount) {
		if(isOverDraft(amount)) return false;
		balance -= amount;
		return true;
	}
	
//	public boolean canPayBill(int amount) {
//		if()
//		
//	}
}