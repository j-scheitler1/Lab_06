package ATM;

import java.io.*;

//Authors: Joshua Scheitler, Ethan Mayer


public class User {

    private Checkings checkingsAccount;
    private Savings savingsAccount;
    public UtilityAccount utilityAccount;

    public User(String username) {
        if (!loadAccounts(username)) {
            this.checkingsAccount = new Checkings(5000.0, 500.0);
            this.savingsAccount = new Savings(5000.0, 0);
            this.checkingsAccount.setBalance(0.0);
            this.savingsAccount.setBalance(0.0);
            saveAccounts(username);
        }
        this.utilityAccount = null; // Lazy-loaded via login
    }

    public Checkings getCheckings() {
        return checkingsAccount;
    }

    public Savings getSavings() {
        return savingsAccount;
    }

    public UtilityAccount getUtility() {
        return utilityAccount;
    }

    public boolean saveAccounts(String username) {
        File inputFile = new File("bank_accounts.txt");
        File tempFile = new File("temp_bank_accounts.txt");
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(username)) {
                    writer.write(username + "," +
                            checkingsAccount.getBalance() + "," +
                            savingsAccount.getBalance());
                    updated = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

            if (!updated) {
                writer.write(username + "," +
                        checkingsAccount.getBalance() + "," +
                        savingsAccount.getBalance());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
            return false;
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            System.out.println("Error replacing bank_accounts.txt");
            return false;
        }
        return true;
    }

    public boolean loadAccounts(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("bank_accounts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(username)) {
                    double checkBal = Double.parseDouble(parts[1]);
                    double saveBal = Double.parseDouble(parts[2]);

                    this.checkingsAccount = new Checkings(5000.0, 500.0);
                    this.checkingsAccount.setBalance(checkBal);

                    this.savingsAccount = new Savings(5000.0, 100.0);
                    this.savingsAccount.setBalance(saveBal);
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
        return false;
    }

    public static boolean savePinMapping(String pin, String username) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("pin_map.txt", true))) {
            writer.write(pin + "," + username);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving pin: " + e.getMessage());
            return false;
        }
    }

    public static String getUsernameFromPin(String pin) {
        try (BufferedReader reader = new BufferedReader(new FileReader("pin_map.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(pin)) {
                    return parts[1];
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading pin_map.txt: " + e.getMessage());
        }
        return null;
    }
}