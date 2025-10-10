package com.pluralsight;

import java.util.Scanner;

public class AccountingLedgerApp {
    public static void main(String[] args) {
        //We'll start here:
        System.out.println("=== Welcome to the Accounting Ledger ===");

        while (true){
            mainMenu();
            menuSelector();
        }

    }
    public static void mainMenu(){
        System.out.println();
        System.out.println("     ==[Main Menu]==");
        System.out.println("What would you like to do?");
        System.out.println("  [D] Add Deposit");
        System.out.println("  [P] Make Payment (Debit)");
        System.out.println("  [L] Display Ledger");
        System.out.println("  [X] Exit the Application");
    }

    public static void menuSelector(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Type Here: ");
        String choice = scanner.nextLine();
        switch (choice){
            case "D":
                //add deposit!
                break;
            case "P":
                //make payment!
                break;
            case "L":
                //display ledger!
                break;
            case "X":
                System.out.println("Exiting - Have a Nice Day!");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Input not recognized, try again!");
        }
        System.out.println("----");
        System.out.println("Press [ENTER] to continue..");
    }
}
