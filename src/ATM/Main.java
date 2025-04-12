package ATM;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("---- Utility Account System ----");
        System.out.println("1. Create New Account");
        System.out.println("2. Login to Existing Account");
        System.out.print("Choose an option: ");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        UtilityAccount account = null;

        if (choice == 1) {
            System.out.print("Enter new username: ");
            String username = sc.nextLine();
            System.out.print("Enter new password: ");
            String password = sc.nextLine();
            account = new UtilityAccount(username, password);
            System.out.println("Account created successfully! Your account number is: " + account.accountNumber);
        } else if (choice == 2) {
            System.out.print("Enter your username: ");
            String username = sc.nextLine();
            System.out.print("Enter your account number: ");
            int accNum = sc.nextInt();
            sc.nextLine(); // consume newline
            System.out.print("Enter your password: ");
            String password = sc.nextLine();
            account = new UtilityAccount(username, password); // dummy to call login()
            account.login(username, accNum, password);
            System.out.println("Logged in. (Note: If nothing loaded, check credentials.)");
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        // Simulate a payment
        System.out.println("\nEnter a test payment to save:");
        System.out.print("Paid Amount: ");
        int paid = sc.nextInt();
        System.out.print("Due Amount: ");
        int due = sc.nextInt();
        sc.nextLine(); // consume newline
        System.out.print("Enter due date (yyyy-MM-dd): ");
        String dateStr = sc.nextLine();

        try {
            Date dueDate = Date.valueOf(dateStr);
            Payment payment = new Payment(paid, due, dueDate);
            account.savePayment(account.accountNumber, payment);
            System.out.println("Payment saved successfully!");

            // Fetch and display latest 3 payments
            List<Payment> recent = account.getPaymentHistory(account.accountNumber);
            System.out.println("\nLatest Payments:");
            for (Payment p : recent) {
                System.out.println(account.displayPayment(p));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd.");
        }

        sc.close();
    }
}
