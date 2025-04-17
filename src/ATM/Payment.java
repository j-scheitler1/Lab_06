package ATM;

//Authors: Joshua Scheitler, Ethan Mayer

public class Payment {
    private double paidAmount;
    private double dueAmount;
    private String dueDate;

    public Payment(double paidAmount, double dueAmount, String dueDate) {
        this.paidAmount = paidAmount;
        this.dueAmount = dueAmount;
        this.dueDate = dueDate;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public double getDueAmount() {
        return dueAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return "!" + dueDate + "|" + paidAmount + "|" + dueAmount + "?";
    }

    public static Payment fromString(String str) {
        try {
            str = str.replace("!", "").replace("?", "");
            String[] parts = str.split("\\|");

            if (parts.length != 3) return null;

            String dueDate = parts[0];
            double paidAmount = Double.parseDouble(parts[1]);
            double dueAmount = Double.parseDouble(parts[2]);

            return new Payment(paidAmount, dueAmount, dueDate);
        } catch (Exception e) {
            System.out.println("Error parsing Payment from string: " + e.getMessage());
            return null;
        }
    }
}
