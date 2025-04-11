package ATM;

//Authors: Joshua Scheitler, Ethan Mayer



public class User {
	
	private Checkings checkingsAccount;
	
	private Savings savingsAccount;
	
	private UtilityAccount utilityAccount;
	
	public User(String username, String password) {
		
		this.checkingsAccount = new Checkings();
		this.savingsAccount = new Savings();
		this.utilityAccount = new UtilityAccount(username, password);
	}
	
	
	public Checkings getCheckings() {
		return checkingsAccount;
	}
	public Savings getSavings() {
		return savingsAccount;
	}
	public UtilityAccount getUtility() {
		return utilityAccount;
	}
	
}
