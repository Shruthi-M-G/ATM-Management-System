package atm;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ATMService atmService = new ATMService();

        System.out.println("===========================================");
        System.out.println("       AUTOMATED TELLER MACHINE            ");
        System.out.println("===========================================");

        int attempts = 0;
        boolean isAuthenticated = false;

        while (attempts < 3) {
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.next();

            System.out.print("Enter PIN: ");
            int pin = scanner.nextInt();

            if (atmService.login(accountNumber, pin)) {
                isAuthenticated = true;
                System.out.println("\nAuthentication successful. Welcome, " + atmService.getCurrentAccount().getHolderName() + ".");
                break;
            } else {
                attempts++;
                System.out.println("Invalid credentials. Remaining attempts: " + (3 - attempts) + "\n");
            }
        }

        if (!isAuthenticated) {
            System.out.println("Account locked due to consecutive failed attempts. Please contact support.");
            scanner.close();
            return;
        }

        int choice;
        do {
            System.out.println("\n================ MAIN MENU ================");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Funds");
            System.out.println("3. Withdraw Funds");
            System.out.println("4. Mini Statement");
            System.out.println("5. Change Security PIN");
            System.out.println("6. Terminate Session");
            System.out.print("Select an option (1-6): ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.printf("Available Balance: $%.2f%n", atmService.getBalance());
                    break;

                case 2:
                    System.out.print("Enter deposit amount: $");
                    double depositAmount = scanner.nextDouble();
                    if (atmService.deposit(depositAmount)) {
                        System.out.printf("Deposit confirmed. New balance: $%.2f%n", atmService.getBalance());
                    }
                    break;

                case 3:
                    System.out.print("Enter withdrawal amount: $");
                    double withdrawalAmount = scanner.nextDouble();
                    if (atmService.withdraw(withdrawalAmount)) {
                        System.out.printf("Dispensing cash. Remaining balance: $%.2f%n", atmService.getBalance());
                    }
                    break;

                case 4:
                    atmService.displayMiniStatement();
                    break;

                case 5:
                    System.out.print("Enter new 4-digit PIN: ");
                    int newPin = scanner.nextInt();
                    if (atmService.changePin(newPin)) {
                        System.out.println("Security PIN successfully updated.");
                    }
                    break;

                case 6:
                    atmService.logout();
                    System.out.println("Session ended. Please remove your card.");
                    break;

                default:
                    System.out.println("Invalid selection. Please choose an option from 1 to 6.");
            }
        } while (choice != 6);

        scanner.close();
    }
}