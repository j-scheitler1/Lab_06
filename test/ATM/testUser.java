package ATM;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class testUser {

	private User user;
	private String testUsername = "testUser";

	@Before
	public void initialize() {
		this.user = new User(testUsername);
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
	public void testUtilityLazyInit() {
		// By default, utilityAccount should be null before login
		assertEquals(null, user.getUtility());
	}

	@Test
	public void testSaveAccounts() {
		boolean expectedUser = user.saveAccounts(testUsername);
		assertEquals(true, expectedUser);
	}

	@Test
	public void testLoadAccounts() {
		user.saveAccounts(testUsername);  // ensure the file exists before loading
		boolean expectedUser = user.loadAccounts(testUsername);
		assertEquals(true, expectedUser);
	}
}
