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
	public void setPaidAmount (double paid) {
		this.paid = paid;
	}
	
	public double getDueAmount () {
		return due;
	}
	public void setDueAmount (double due) {
		this.due = due;
	}
	
	public String getdueDate() {
		return dueDate;
	}
	
	@Override
	public String toString() {
	    return "!" + dueDate + "|" + paid + "|" + due + "?";
	}
}
