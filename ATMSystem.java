import java.util.Scanner;
import java.util.ArrayList;

class User {
    private String userID;
    private int pin;
    private double balance;
    
    public User(String userID, int pin, double initialBalance) {
        this.userID = userID;
        this.pin = pin;
        this.balance = initialBalance;
    }
    
    public String getUserID() {
        return userID;
    }
    
    public boolean validatePin(int enteredPin) {
        return this.pin == enteredPin;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void deposit(double amount) {
        balance += amount;
    }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
    
    public void transfer(User recipient, double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            recipient.deposit(amount);
        }
    }
}

class Transaction {
    private String type;
    private double amount;
    
    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }
    
    public String getType() {
        return type;
    }
    
    public double getAmount() {
        return amount;
    }
}

public class ATMSystem {
    public static void main(String[] args) {
        User user = new User("12345", 1234, 1000.0); // Sample user with ID "12345" and PIN "1234"
        ArrayList<Transaction> transactionHistory = new ArrayList<>();
        int attempts = 3;
        boolean authenticated = false;
        
        Scanner scanner = new Scanner(System.in);
        
        for (int i = 0; i < attempts; i++) {
            System.out.print("Enter User ID: ");
            String userID = scanner.nextLine();
            
            System.out.print("Enter PIN: ");
            int enteredPin = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            if (user.getUserID().equals(userID) && user.validatePin(enteredPin)) {
                authenticated = true;
                break;
            } else {
                System.out.println("Invalid User ID or PIN. " + (attempts - (i + 1)) + " attempts remaining.");
            }
        }
        
        if (authenticated) {
            while (true) {
                System.out.println("\nATM Menu:");
                System.out.println("1. Check Balance");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Transfer");
                System.out.println("5. Quit");
                
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1:
                        System.out.println("Current Balance: $" + user.getBalance());
                        break;
                    case 2:
                        System.out.print("Enter deposit amount: $");
                        double depositAmount = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline
                        user.deposit(depositAmount);
                        transactionHistory.add(new Transaction("Deposit", depositAmount));
                        System.out.println("Deposit successful. Current Balance: $" + user.getBalance());
                        break;
                    case 3:
                        System.out.print("Enter withdrawal amount: $");
                        double withdrawalAmount = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline
                        if (user.withdraw(withdrawalAmount)) {
                            transactionHistory.add(new Transaction("Withdrawal", withdrawalAmount));
                            System.out.println("Withdrawal successful. Current Balance: $" + user.getBalance());
                        } else {
                            System.out.println("Insufficient funds or invalid amount.");
                        }
                        break;
                    case 4:
                        System.out.print("Enter recipient's User ID: ");
                        String recipientID = scanner.nextLine();
                        
                        System.out.print("Enter transfer amount: $");
                        double transferAmount = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline
                        
                        if (recipientID.equals(user.getUserID())) {
                            System.out.println("Cannot transfer to yourself.");
                        } else {
                            User recipient = new User("54321", 4321, 0.0); // Sample recipient with ID "54321" and PIN "4321"
                            user.transfer(recipient, transferAmount);
                            transactionHistory.add(new Transaction("Transfer to " + recipientID, transferAmount));
                            System.out.println("Transfer successful. Current Balance: $" + user.getBalance());
                        }
                        break;
                    case 5:
                        System.out.println("Thank you for using the ATM. Goodbye!");
                        // Print transaction history
                        System.out.println("Transaction History:");
                        for (Transaction transaction : transactionHistory) {
                            System.out.println(transaction.getType() + " $" + transaction.getAmount());
                        }
                        return;
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                        break;
                }
            }
        } else {
            System.out.println("Authentication failed. Exiting...");
        }
    }
}
