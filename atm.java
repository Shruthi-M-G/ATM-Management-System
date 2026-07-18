package atm;

import java.util.Scanner;

class ATM {

    private int pin = 1234;
    private double balance = 5000;

    private String[] transactions = new String[10];
    private int transCount = 0;

    private int transactionNumber = 0;

    Scanner sc = new Scanner(System.in);

    public void start() {

        int attempts = 0;

        while (attempts < 3) {

            System.out.print("Enter ATM PIN: ");
            int enteredPin = sc.nextInt();

            if (enteredPin == pin) {
                menu();
                return;
            } else {
                attempts++;
                System.out.println("Wrong PIN. Attempts left: " + (3 - attempts));
            }
        }

        System.out.println("Card blocked due to 3 incorrect attempts.");
    }

    public void menu() {

        int choice;

        do {

            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Change PIN");
            System.out.println("5. Mini Statement");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    checkBalance();
                    break;

                case 2:
                    deposit();
                    break;

                case 3:
                    withdraw();
                    break;

                case 4:
                    changePin();
                    break;

                case 5:
                    miniStatement();
                    break;

                case 6:
                    exitSummary();
                    break;

                default:
                    System.out.println("Invalid Choice");
            }

        } while (choice != 6);
    }

    private void checkBalance() {

        System.out.println("Current Balance: " + balance);

    }

    private void deposit() {

        System.out.print("Enter amount to deposit: ");
        double amount = sc.nextDouble();

        balance += amount;

        addTransaction("Deposited: " + amount);

        System.out.println("Amount deposited successfully");

    }

    private void withdraw() {

        System.out.print("Enter amount to withdraw: ");
        double amount = sc.nextDouble();

        if (amount > balance) {
            System.out.println("Insufficient Balance");
        } else {

            balance -= amount;

            addTransaction("Withdrawn: " + amount);

            System.out.println("Please collect your cash");
        }
    }

    private void changePin() {

        System.out.print("Enter old PIN: ");
        int oldPin = sc.nextInt();

        if (oldPin == pin) {

            System.out.print("Enter new PIN: ");
            int newPin = sc.nextInt();

            pin = newPin;

            System.out.println("PIN changed successfully");

        } else {

            System.out.println("Incorrect old PIN");

        }
    }

    private void miniStatement() {

        System.out.println("\n---- Mini Statement ----");

        if (transCount == 0) {
            System.out.println("No transactions yet");
        }

        for (int i = 0; i < transCount; i++) {
            System.out.println(transactions[i]);
        }

    }

    private void addTransaction(String record) {

        if (transCount < transactions.length) {
            transactions[transCount] = record;
            transCount++;
        }

        transactionNumber++;

    }

    private void exitSummary() {

        System.out.println("\nSession Summary");
        System.out.println("Transactions performed: " + transactionNumber);
        System.out.println("Final Balance: " + balance);
        System.out.println("Thank you for using ATM");

    }
}

public class Main {

    public static void main(String[] args) {

        ATM obj = new ATM();
        obj.start();

    }
}
