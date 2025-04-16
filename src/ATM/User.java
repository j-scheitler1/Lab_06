package ATM;

import java.io.*;

public class User {

    private Checkings checkingsAccount;
    private Savings savingsAccount;
    protected UtilityAccount utilityAccount;

    public User(String username, String password) {
        if (!loadAccounts(username)) {
            this.checkingsAccount = new Checkings(5000.0, 500.0);
            this.savingsAccount = new Savings(5000.0, 0);
            this.checkingsAccount.setBalance(0.0);
            this.savingsAccount.setBalance(0.0);
            saveAccounts(username);
        }
        this.utilityAccount = UtilityAccount.createOrLogin(username, "0", password);
    }

    public User(String username, String accountNumber, String password) {
        String nameToUse = (username != null) ? username : accountNumber;
        if (!loadAccounts(nameToUse)) {
            this.checkingsAccount = new Checkings(5000.0, 500.0);
            this.savingsAccount = new Savings(5000.0, 0);
            this.checkingsAccount.setBalance(0.0);
            this.savingsAccount.setBalance(0.0);
            saveAccounts(nameToUse);
        }
        this.utilityAccount = UtilityAccount.createOrLogin(nameToUse, accountNumber, password);
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
                if (parts.length >= 6 && parts[0].equals(username)) {
                    writer.write(username + "," +
                            checkingsAccount.getBalance() + "," +
                            checkingsAccount.getWithdrawLimit() + "," +
                            savingsAccount.getBalance() + "," +
                            savingsAccount.getDepositLimit() + "," +
                            savingsAccount.getWithdrawLimit());
                    updated = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

            if (!updated) {
                writer.write(username + "," +
                        checkingsAccount.getBalance() + "," +
                        checkingsAccount.getWithdrawLimit() + "," +
                        savingsAccount.getBalance() + "," +
                        savingsAccount.getDepositLimit() + "," +
                        savingsAccount.getWithdrawLimit());
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
                if (parts.length >= 6 && parts[0].equals(username)) {
                    double checkBal = Double.parseDouble(parts[1]);
                    double checkLimit = Double.parseDouble(parts[2]);
                    double saveBal = Double.parseDouble(parts[3]);
                    double saveDepositLimit = Double.parseDouble(parts[4]);
                    double saveWithdrawLimit = Double.parseDouble(parts[5]);

                    this.checkingsAccount = new Checkings(checkLimit, checkLimit);
                    this.checkingsAccount.setBalance(checkBal);

                    this.savingsAccount = new Savings(saveDepositLimit, saveWithdrawLimit);
                    this.savingsAccount.setBalance(saveBal);
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
        return false;
    }
}
