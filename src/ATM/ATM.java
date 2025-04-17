package ATM;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ATM {
	
	public static String utilUsername;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // === PIN LOGIN ===
        System.out.println("Welcome to the ATM! Please input your 4-digit PIN:");
        String pin = "0000";
        boolean exit = true;
        
        try {
        	pin = scanner.next();
        }
        catch(InputMismatchException e){
        	System.out.println("Invalid input. Please enter a string.");
        	scanner.nextLine();
        	try {
        		pin = scanner.next();
        	}
        	catch(InputMismatchException g) {
        		scanner.nextLine();
        		pin = "0000";
        		exit = false;
        	}
        }
        
        String username = User.getUsernameFromPin(pin);

        if (username == null) {
            System.out.println("PIN not recognized. Let's create a new account.");
            System.out.print("Please enter a username to register: ");
            
            try {
            	username = scanner.next();
            }
            catch(InputMismatchException e) {
            	System.out.println("Invalid input. Please enter a string.");
            	scanner.nextLine();
            	try {
            		username = scanner.next();
            	}
            	catch(InputMismatchException g) {
            		scanner.nextLine();
            		username = "none";
            		exit = false;
            	}
            }
            
            System.out.print("Please choose a new 4-digit PIN: ");
            String newPin = "0000";
            
            try {
            	newPin = scanner.next();
            }
            catch(InputMismatchException e) {
            	System.out.println("Invalid input. Please enter a string.");
            	scanner.nextLine();
            	try {
            		newPin = scanner.next();
            	}
            	catch(InputMismatchException g) {
            		scanner.nextLine();
            		newPin = "0000";
            		exit = false;
            	}
            }

            if (User.savePinMapping(newPin, username)) {
            	utilUsername = username;
                System.out.println("PIN registered successfully!");
            } else {
                System.out.println("Failed to register PIN. Exiting.");
                return;
            }

            // Save initial bank account state
            User newUser = new User(username);
            newUser.saveAccounts(username);
        }

        User user = new User(username);
        Checkings checkingsAccount = user.getCheckings();
        Savings savingsAccount = user.getSavings();
        UtilityAccount utilityAccount = null;
        
        int choice = 99;

        

        while (exit) {
            System.out.println("\nWhich account would you like to access? Input 1, 2, or 3.");
            System.out.println("1: Checkings, 2: Savings, 3: Utility");
            System.out.println("To advance to the next day: 0");
            System.out.println("To exit ATM: 9");

            try {
            	choice = scanner.nextInt();
            }
            catch (InputMismatchException e){
            	System.out.println("Invalid input. Please enter an integer.");
            	scanner.nextLine();
            	try {
            		choice = scanner.nextInt();
            	}
            	catch(InputMismatchException g) {
            		scanner.nextLine();
            		choice = 9;
            	}
            }

            if (choice == 1) {
                System.out.println("Welcome to your checking account " + username + "!");
                System.out.println("Options: 1: Deposit, 2: Withdraw, 3: Transfer, 4: Pay bill, 5: Check balance, 6: Back");
                try {
                	choice = scanner.nextInt();
                }
                catch (InputMismatchException e){
                	System.out.println("Invalid input. Please enter an integer.");
                	scanner.nextLine();
                	try {
                		choice = scanner.nextInt();
                	}
                	catch(InputMismatchException g) {
                		scanner.nextLine();
                		choice = 6;
                	}
                }

                if (choice == 1) {
                    System.out.println("How much would you like to deposit?");
                    
                    double amount = -1;
                    
                    try{
                    	amount = scanner.nextDouble();
                    }
                    catch(InputMismatchException e) {
                    	System.out.println("Invalid input. Please enter a double.");
                    	scanner.nextLine();
                    	try {
                    		amount = scanner.nextDouble();
                    	}
                    	catch(InputMismatchException g) {
                    		scanner.nextLine();
                    		amount = -1;
                    	}
                    }
                    

                    if (!checkingsAccount.deposit(amount)) {
                        System.out.println("Failure to deposit. You have only $" + checkingsAccount.getDepositLimit() + " daily deposit limit remaining!");
                    } else {
                        System.out.println("Successfully deposited $" + amount);
                        System.out.println("Remaining deposit limit: $" + checkingsAccount.getDepositLimit());
                        user.saveAccounts(username);
                    }
                }

                if (choice == 2) {
                    System.out.println("How much would you like to withdraw?");
                    double amount = -1;
                    
                    try{
                    	amount = scanner.nextDouble();
                    }
                    catch(InputMismatchException e) {
                    	System.out.println("Invalid input. Please enter a double.");
                    	scanner.nextLine();
                    	try {
                    		amount = scanner.nextDouble();
                    	}
                    	catch(InputMismatchException g) {
                    		scanner.nextLine();
                    		amount = -1;
                    	}
                    }

                    if (!checkingsAccount.withdraw(amount)) {
                        System.out.println("Failure to withdraw.");
                        if (amount > checkingsAccount.getBalance()) {
                            System.out.println("Oops! You tried to overdraft your account.");
                        } else {
                            System.out.println("You have only $" + checkingsAccount.getWithdrawLimit() + " daily withdraw limit remaining!");
                        }
                    } else {
                        System.out.println("Successfully withdrew $" + amount);
                        System.out.println("Remaining withdraw limit: $" + checkingsAccount.getWithdrawLimit());
                        user.saveAccounts(username);
                    }
                }

                if (choice == 3) {
                    System.out.println("How much would you like to transfer?");
                    double amount = -1;
                    
                    try{
                    	amount = scanner.nextDouble();
                    }
                    catch(InputMismatchException e) {
                    	System.out.println("Invalid input. Please enter a double.");
                    	scanner.nextLine();
                    	try {
                    		amount = scanner.nextDouble();
                    	}
                    	catch(InputMismatchException g) {
                    		scanner.nextLine();
                    		amount = -1;
                    	}
                    }

                    if (checkingsAccount.transfer(amount, checkingsAccount, savingsAccount)) {
                        System.out.println("Transferred $" + amount + " to savings");
                        user.saveAccounts(username);
                    } else {
                        System.out.println("Too much to transfer. Balance: $" + checkingsAccount.getBalance());
                    }
                }

                if (choice == 4) {
                    System.out.println("Please enter how much you would like to pay:");
                    double amount = -1;
                    
                    try{
                    	amount = scanner.nextDouble();
                    }
                    catch(InputMismatchException e) {
                    	System.out.println("Invalid input. Please enter a double.");
                    	scanner.nextLine();
                    	try {
                    		amount = scanner.nextDouble();
                    	}
                    	catch(InputMismatchException g) {
                    		scanner.nextLine();
                    		amount = -1;
                    	}
                    }

                    if (!checkingsAccount.withdraw(amount)) {
                        System.out.println("Failure to Pay");
                        if (amount > checkingsAccount.getBalance()) {
                            System.out.println("Overdraft attempt detected.");
                        } else {
                            System.out.println("Withdraw limit exceeded.");
                        }
                    } else {
                        if (utilityAccount == null) {
                            System.out.println("Please login to your Utility Account.");
                            System.out.print("Enter username (or blank to use account number): ");
                            String utilUsername = "none";
                            try{
                            	utilUsername = scanner.next();
                            }
                            catch(InputMismatchException e) {
                            	System.out.println("Invalid input. Please enter a string.");
                            	scanner.nextLine();
                            	try {
                            		utilUsername = scanner.next();
                            	}
                            	catch(InputMismatchException g) {
                            		scanner.nextLine();
                            		utilUsername = "none";
                            	}
                            }

                            System.out.print("Enter account number (or blank if using username): ");
                            String utilAccountNum = "none";
                            try{
                            	utilAccountNum = scanner.next();
                            }
                            catch(InputMismatchException e) {
                            	System.out.println("Invalid input. Please enter a string.");
                            	scanner.nextLine();
                            	try {
                            		utilAccountNum = scanner.next();
                            	}
                            	catch(InputMismatchException g) {
                            		scanner.nextLine();
                            		utilAccountNum = "none";
                            	}
                            }

                            System.out.print("Enter password: ");
                            String utilPassword = "none";
                            try{
                            	utilPassword = scanner.next();
                            }
                            catch(InputMismatchException e) {
                            	System.out.println("Invalid input. Please enter a string.");
                            	scanner.nextLine();
                            	try {
                            		utilPassword = scanner.next();
                            	}
                            	catch(InputMismatchException g) {
                            		scanner.nextLine();
                            		utilPassword = "none";
                            	}
                            }

                            utilityAccount = UtilityAccount.createOrLogin(
                                utilUsername.isEmpty() ? null : utilUsername,
                                utilAccountNum.isEmpty() ? "0" : utilAccountNum,
                                utilPassword
                            );

                            if (utilityAccount == null) {
                                System.out.println("Utility login failed.");
                                continue;
                            }

                            user.utilityAccount = utilityAccount;
                        }

                        utilityAccount.savePayment(utilityAccount.getAccountNumber(), amount);
                        System.out.println("Utility Company Says 'Thanks for your money chump!'");
                        user.saveAccounts(username);
                    }
                }

                if (choice == 5) {
                    System.out.println("Checking account balance: $" + checkingsAccount.getBalance());
                }
            }

            else if (choice == 2) {
                System.out.println("Welcome to your savings account " + username + "!");
                System.out.println("Options: 1: Deposit, 2: Transfer, 3: Check balance, 4: Back");
                try {
                	choice = scanner.nextInt();
                }
                catch (InputMismatchException e){
                	System.out.println("Invalid input. Please enter an integer.");
                	scanner.nextLine();
                	try {
                		choice = scanner.nextInt();
                	}
                	catch(InputMismatchException g) {
                		scanner.nextLine();
                		choice = 4;
                	}
                }

                if (choice == 1) {
                    System.out.println("How much would you like to deposit?");
                    double amount = -1;
                    
                    try{
                    	amount = scanner.nextDouble();
                    }
                    catch(InputMismatchException e) {
                    	System.out.println("Invalid input. Please enter a double.");
                    	scanner.nextLine();
                    	try {
                    		amount = scanner.nextDouble();
                    	}
                    	catch(InputMismatchException g) {
                    		scanner.nextLine();
                    		amount = -1;
                    	}
                    }

                    if (!savingsAccount.deposit(amount)) {
                        System.out.println("Failure to deposit. You have only $" + savingsAccount.getDepositLimit() + " daily deposit limit remaining!");
                    } else {
                        System.out.println("Successfully deposited $" + amount);
                        System.out.println("Remaining deposit limit: $" + savingsAccount.getDepositLimit());
                        user.saveAccounts(username);
                    }
                }

                if (choice == 2) {
                    System.out.println("How much would you like to transfer to checking?");
                    double amount = -1;
                    
                    try{
                    	amount = scanner.nextDouble();
                    }
                    catch(InputMismatchException e) {
                    	System.out.println("Invalid input. Please enter a double.");
                    	scanner.nextLine();
                    	try {
                    		amount = scanner.nextDouble();
                    	}
                    	catch(InputMismatchException g) {
                    		scanner.nextLine();
                    		amount = -1;
                    	}
                    }

                    if (savingsAccount.transfer(amount, savingsAccount, checkingsAccount)) {
                        System.out.println("Transferred $" + amount + " to checking");
                        System.out.println("Remaining transfer limit: $" + savingsAccount.getTransferTotal());
                        user.saveAccounts(username);
                    } else {
                        System.out.println("Transfer failed. Limit or balance exceeded.");
                    }
                }

                if (choice == 3) {
                    System.out.println("Savings account balance: $" + savingsAccount.getBalance());
                }
            }

            else if (choice == 3) {
                if (utilityAccount == null) {
                	System.out.println("Please login to your Utility Account. (If you do not have a login just fill in info and it will be created)");

                	System.out.print("Enter username (Same as ATM username or blank to use account number): ");
                	scanner.nextLine();
                	String utilUsername = "none";
                    try{
                    	utilUsername = scanner.nextLine().trim();
                    }
                    catch(InputMismatchException e) {
                    	System.out.println("Invalid input. Please enter a string.");
                    	scanner.nextLine();
                    	try {
                    		utilUsername = scanner.nextLine();
                    	}
                    	catch(InputMismatchException g) {
                    		scanner.nextLine();
                    		exit = false;
                    	}
                    }

                	System.out.print("Enter Account number (or blank if using username): ");
                	String utilAccountNum = "none";
                    try{
                    	utilAccountNum = scanner.nextLine().trim();
                    }
                    catch(InputMismatchException e) {
                    	System.out.println("Invalid input. Please enter a string.");
                    	scanner.nextLine();
                    	try {
                    		utilAccountNum = scanner.nextLine().trim();
                    	}
                    	catch(InputMismatchException g) {
                    		scanner.nextLine();
                    		exit = false;
                    	}
                    }

                	System.out.print("Enter password: ");
                	String utilPassword = "none";
                    try{
                    	utilPassword = scanner.next();
                    }
                    catch(InputMismatchException e) {
                    	System.out.println("Invalid input. Please enter a string.");
                    	scanner.nextLine();
                    	try {
                    		utilPassword = scanner.next();
                    	}
                    	catch(InputMismatchException g) {
                    		scanner.nextLine();
                    		exit = false;
                    	}
                    }

                    utilityAccount = UtilityAccount.createOrLogin(
                        utilUsername.isEmpty() ? "" : utilUsername,
                        utilAccountNum.isEmpty() ? "0" : utilAccountNum,
                        utilPassword
                    );
                    
                    

                    if (utilityAccount == null) {
                        System.out.println("Utility login failed.");
                        continue;
                    }

                    user.utilityAccount = utilityAccount;
                }

                System.out.println("Welcome to Utility Company Incorporated! Customer: " + utilityAccount.getAccountNumber());
                System.out.println("Options: 1: View payment history, 2: View next bill, 3: Back");
                try {
                	choice = scanner.nextInt();
                }
                catch (InputMismatchException e){
                	System.out.println("Invalid input. Please enter an integer.");
                	scanner.nextLine();
                	try {
                		choice = scanner.nextInt();
                	}                    	
            		catch(InputMismatchException g) {
            			scanner.nextLine();
            			choice = 3;
            		}
                }

                if (choice == 1) {
                    List<Payment> history = utilityAccount.getPaymentHistory();
                    if (history.isEmpty()) {
                        System.out.println("No payment history.");
                    } else {
                        for (Payment p : history) {
                            System.out.print(utilityAccount.displayPayment(p));
                        }
                    }
                }

                if (choice == 2) {
                	System.out.println("Next bill payment: $" + String.format("%.2f", utilityAccount.getNextBillPayment()));
                    System.out.println("Due date: " + utilityAccount.getNextBillDueDate());
                }
            }

            else if (choice == 0) {
                System.out.println("Advancing to the next day...");
                checkingsAccount.resetDailyLimits(5000.0, 500.0);
                savingsAccount.resetDailyLimits(5000.0, 100.0);
            }

            else if (choice == 9) {
                user.saveAccounts(username);
                System.out.println("See you later!");
                exit = false;
            }
        }
    }
}
