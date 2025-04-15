package ATM;

// Authors: Joshua Scheitler, Ethan Mayer

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class UtilityAccount {
    protected String username;
    protected String password;
    protected int accountNumber;
    protected List<Payment> paymentHistory;
    protected Payment nextPayment;
    Random rand = new Random();

    public static UtilityAccount createOrLogin(String username, String accountNum, String password) {
        if (login(username, accountNum, password)) {
            System.out.println("Logging in!");
            UtilityAccount ua = new UtilityAccount(username, accountNum, password);
            setAccountNumber(username, accountNum, password, ua);
            return ua;
        }
        System.out.println("Creating Account!");
        return new UtilityAccount(username, password);
    }

    public UtilityAccount(String username, String accountNum, String password) {
        this.username = username;
        this.password = password;
        this.accountNumber = getAccountNumber();
        this.paymentHistory = getPaymentHistory();
        setNextPayment();
    }

    public UtilityAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.accountNumber = rand.nextInt(1_000_000) + 1;
        this.paymentHistory = new ArrayList<>();
        setNextPayment();
        saveUser();
    }

    public void saveUser() {
        appendToFile("utility_users.txt", username + "," + password + "," + accountNumber);
        appendToFile("payment_history.txt", accountNumber + ",");
    }

    private void appendToFile(String filename, String content) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename, true))) {
            out.println(content);
        } catch (IOException e) {
            System.out.println("Error writing to " + filename + ": " + e.getMessage());
        }
    }

    public static boolean login(String username, String accountNumber, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("utility_users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    if ((username.equals(parts[0]) || accountNumber.equals(parts[2])) && password.equals(parts[1])) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not find user: " + e.getMessage());
        }
        return false;
    }

    public List<Payment> getPaymentHistory() {
        List<Payment> payments = new ArrayList<>();
        String actNum = String.valueOf(getAccountNumber());

        try (BufferedReader reader = new BufferedReader(new FileReader("payment_history.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts[0].equals(actNum) && parts.length > 1) {
                    String[] entries = parts[1].split("\\?");
                    for (String entry : entries) {
                        if (entry.isEmpty()) continue;
                        String[] details = entry.replaceFirst("!", "").split("\\|");
                        if (details.length == 3) {
                            try {
                                payments.add(new Payment(
                                    Double.parseDouble(details[1]),
                                    Double.parseDouble(details[2]),
                                    details[0]
                                ));
                            } catch (IllegalArgumentException e) {
                                System.out.println("Skipping malformed payment entry: " + entry);
                            }
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading payment history: " + e.getMessage());
        }

        int size = payments.size();
        return payments.subList(Math.max(0, size - 3), size);
    }

    public Boolean savePayment(int accountNumber, double amount) {
        File inputFile = new File("payment_history.txt");
        File tempFile = new File("temp_payment_history.txt");
        String actNum = String.valueOf(accountNumber);
        boolean found = false;

        try (
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts[0].equals(actNum)) {
                    updatePayment(amount);
                    String existing = parts.length > 1 ? parts[1] : "";
                    writer.write(actNum + "," + existing + paymentFormat(nextPayment));
                    found = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating payment history: " + e.getMessage());
            return false;
        }

        if (!found) {
            System.out.println("No matching account found. No payment saved.");
            tempFile.delete();
            return false;
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            System.out.println("Error: Could not replace payment history file.");
            return false;
        }

        return true;
    }

    private void updatePayment(double amount) {
        double paid = nextPayment.getPaidAmount() + amount;
        double remaining = nextPayment.getDueAmount() - amount;

        nextPayment.setPaidAmount(paid);
        nextPayment.setDueAmount(Math.max(0, remaining));

        if (remaining <= 0) {
            setNextPayment();
        }
    }

    public String paymentFormat(Payment payment) {
        return "!" + payment.getdueDate() + "|" + payment.getPaidAmount() + "|" + payment.getDueAmount() + "?";
    }

    public String displayPayment(Payment payment) {
        return "Due Date: " + payment.getdueDate() + "\n" +
               "Paid Amount: " + payment.getPaidAmount() + "\n" +
               "Due Amount: " + payment.getDueAmount() + "\n";
    }

    public Payment getNextPayment() {
        return nextPayment;
    }

    public void setNextPayment() {
        String date = getRandomDate();
        double due = ThreadLocalRandom.current().nextDouble(100.0, 500.0);
        nextPayment = new Payment(0, due, date);
    }

    public double getNextBillPayment() {
        return nextPayment.getDueAmount();
    }

    public String getNextBillDueDate() {
        return nextPayment.getdueDate();
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public static void setAccountNumber(String username, String accountNumber, String password, UtilityAccount ua) {
        try (BufferedReader reader = new BufferedReader(new FileReader("utility_users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    if ((username.equals(parts[0]) || accountNumber.equals(parts[2])) && password.equals(parts[1])) {
                        ua.accountNumber = Integer.parseInt(parts[2]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not find user: " + e.getMessage());
        }
    }

    public static String getRandomDate() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2030, 12, 31);
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        long randomDays = ThreadLocalRandom.current().nextLong(daysBetween + 1);
        return startDate.plusDays(randomDays).format(DateTimeFormatter.ofPattern("MMMM d yyyy"));
    }
}

