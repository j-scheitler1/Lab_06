package ATM;

//Authors: Joshua Scheitler, Ethan Mayer

import java.sql.Date;

public class Payment {
	
	private int paid;
	private int due;
	private Date dueDate;
	
	public Payment (int paid, int due, Date data) {
		this.paid = paid;
		this.due = due;
		this.dueDate = data;
	}
	
	public int getPaidAmount () {
		return paid;
	}
	
	public int getDueAmount () {
		return due;
	}
	
	public Date getdueDate() {
		return dueDate;
	}
}
