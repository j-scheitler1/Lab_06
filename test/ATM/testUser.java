package ATM;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class testUser {
	
	private User user;

	@Before
	public void initialize() {
		this.user = new User("none", "none");
	}
	
	@Test
	public void testGetCheckings() {

		Checkings checkings = user.getCheckings();
		
		assertNotEquals(null, checkings);
	}
	
	@Test
	public void testGetSavings() {

		Savings savings = user.getSavings();
		
		assertNotEquals(null, savings);
	}
	
	@Test
	public void testGetUtility() {

		UtilityAccount utilityAccount = user.getUtility();
		
		assertNotEquals(null, utilityAccount);
	}
}
