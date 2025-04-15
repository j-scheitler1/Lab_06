package ATM;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class testPayment {

	public Payment payment;

	private static final double DELTA = 1e-15;
	
	@Before 
	public void initialize(){
		this.payment = new Payment(10.0, 100.0, "July 3rd");
	}
	
	@Test
	public void testGetPaidAmount () {
		
		assertEquals(10.0, payment.getPaidAmount(), DELTA);
	}
	
	@Test
	public void testGetDueAmount () {

		assertEquals(100.0, payment.getDueAmount(), DELTA);
	}
	
	@Test
	public void testGetDueDate() {

		assertEquals("July 3rd", payment.getDueDate());
	}
}
