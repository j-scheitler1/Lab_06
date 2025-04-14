package ATM;

//Authors: Joshua Scheitler, Ethan Mayer

import java.sql.Date;

public class Payment {
	
	private double paid;
	private double due;
	private String dueDate;
	
	public Payment (double paid, double due, String date) {
		this.paid = paid;
		this.due = due;
		this.dueDate = date;
	}
	
	public double getPaidAmount () {
		return paid;
	}
	
	public double getDueAmount () {
		return due;
	}
	
	public String getdueDate() {
		return dueDate;
	}
}
