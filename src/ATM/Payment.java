package ATM;

import java.sql.Date;

public class Payment {
	
	private int paid;
	private int due;
	private Date data;
	
	public Payment (int paid, int due, Date data) {
		this.paid = paid;
		this.due = due;
		this.data = data;
	}
}
